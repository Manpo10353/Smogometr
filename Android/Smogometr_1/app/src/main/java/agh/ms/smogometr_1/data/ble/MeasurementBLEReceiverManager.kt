@file:Suppress("DEPRECATION") @file:OptIn(ExperimentalStdlibApi::class)

package agh.ms.smogometr_1.data.ble

import agh.ms.smogometr_1.data.ConnectionState
import agh.ms.smogometr_1.data.measurement.MeasurementResult
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.nio.ByteBuffer
import java.util.UUID
import javax.inject.Inject

@SuppressLint("MissingPermission")
class MeasurementBLEReceiverManager @Inject constructor(
    private val bluetoothAdapter: BluetoothAdapter, private val context: Context
) : MeasurementReceiverManager {

    private val DEVICE_NAME = "Smogometr"
    private val SMOGOMETR_SERVICE_UUID = "4fafc201-1fb5-459e-8fcc-c5c9c331914b"
    //private val SMOGOMETR_INITIAL_SERVICE_UUID = "2cb1fc41-c000-4fb9-9a8a-d4a4c6593519"
    private val RX_CHARACTERISTIC = "fb058520-c3f1-4c25-bb2b-e34340c23571"
    private val TX_CHARACTERISTIC = "6E400002-B5A3-F393-E0A9-E50E24DCCA9E"
    //private val INITIAL_CHARACTERISTIC = "8988eff5-412a-4ce9-95fe-6f311434133d"

    private var currentConnectionAttempt = 1
    private var MAXIMUM_CONNECTION_ATTEMPTS = 2

    override val data: MutableSharedFlow<Resource<MeasurementResult>> = MutableSharedFlow()

    private val bleScanner by lazy {
        bluetoothAdapter.bluetoothLeScanner
    }

    private val scanSettings =
        ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build()

    private var gatt: BluetoothGatt? = null

    private var isScanning: Boolean = false

    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            if (result.device.name == DEVICE_NAME) {
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Łączenie z urządzeniem"))
                }
                if (isScanning) {
                    result.device.connectGatt(
                        context, false, gattCallback, BluetoothDevice.TRANSPORT_LE
                    )
                    isScanning = false
                    bleScanner.stopScan(this)
                }
            }
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    coroutineScope.launch {
                        data.emit(Resource.Loading(message = "Wyszukiwanie serwisów"))
                    }
                    gatt.discoverServices()
                    this@MeasurementBLEReceiverManager.gatt = gatt
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    coroutineScope.launch {
                        data.emit(
                            Resource.Success(
                                data = MeasurementResult(
                                    0, 0, 0f,0f,ConnectionState.Disconnected
                                )
                            )
                        )
                    }
                    gatt.close()
                }
            } else {
                gatt.close()
                currentConnectionAttempt += 1
                coroutineScope.launch {
                    data.emit(
                        Resource.Loading(
                            message = "Próba połączenia"
                        )
                    )
                }
                if (currentConnectionAttempt <= MAXIMUM_CONNECTION_ATTEMPTS) {
                    startReceiving()
                } else {
                    coroutineScope.launch {
                        data.emit(Resource.Error(errorMessage = "Nie udało się połączyć"))
                    }
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            with(gatt) {
                //printGattTable()
                coroutineScope.launch {
                    data.emit(Resource.Loading(message = "Łączenie, ustalanie wielkości komunikatów"))
                }
                gatt.requestMtu(517)
            }
        }

        override fun onMtuChanged(gatt: BluetoothGatt, mtu: Int, status: Int) {
            val characteristic = findCharacteristics(SMOGOMETR_SERVICE_UUID, RX_CHARACTERISTIC)
            //val initialCharacteristic = findCharacteristics(SMOGOMETR_INITIAL_SERVICE_UUID, INITIAL_CHARACTERISTIC)
            if (characteristic == null) {
                coroutineScope.launch {
                    data.emit(Resource.Error(errorMessage = "Nie znaleziono charakterystyk"))
                }
                return
            }
            enableNotification(characteristic)
            //initialCharacteristic?.let { enableNotification(it) }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic
        ) {

            with(characteristic) {
                when (uuid) {
                    UUID.fromString(RX_CHARACTERISTIC) -> {
                        val bytepm25 = byteArrayOf(0x00.toByte(), 0x00.toByte(), value[1], value[0])
                        val pm25Value = ByteBuffer.wrap(bytepm25).int
                        val bytepm10 = byteArrayOf(0x00.toByte(), 0x00.toByte(), value[3], value[2])
                        val pm10Value = ByteBuffer.wrap(bytepm10).int
                        val bytenox = byteArrayOf(0x00.toByte(), 0x00.toByte(), value[5], value[4])
                        val noxValue = ByteBuffer.wrap(bytenox).int / 100f
                        val bytehumidity = byteArrayOf(0x00.toByte(), 0x00.toByte(), value[7], value[6])
                        val humidityValue = ByteBuffer.wrap(bytehumidity).int / 100f

                        val measurementResult = MeasurementResult(
                            pm25Value, pm10Value, noxValue,humidityValue, ConnectionState.Connected
                        )
                        coroutineScope.launch {
                            data.emit(
                                Resource.Success(data = measurementResult)
                            )
                        }
                    }
                    else -> Unit
                }
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            super.onCharacteristicWrite(gatt, characteristic, status)
        }
    }


    private fun enableNotification(characteristic: BluetoothGattCharacteristic) {
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        val payload = when {
            characteristic.isIndicatable() -> BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
            characteristic.isNotifiable() -> BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            else -> return
        }
        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if (gatt?.setCharacteristicNotification(characteristic, true) == false) {
                return
            }
            writeDescription(cccdDescriptor, payload)
        }
    }

    private fun writeDescription(descriptor: BluetoothGattDescriptor, payload: ByteArray) {
        gatt?.let { gatt ->
            descriptor.value = payload
            gatt.writeDescriptor(descriptor)
        } ?: error("Nie połączono z urządzeniem")
    }

    private fun findCharacteristics(
        serviceUUID: String, characteristicsUUID: String
    ): BluetoothGattCharacteristic? {
        return gatt?.services?.find { service ->
            service.uuid.toString() == serviceUUID
        }?.characteristics?.find { characteristics ->
            characteristics.uuid.toString() == characteristicsUUID
        }
    }

    override fun startReceiving() {
        coroutineScope.launch {
            data.emit(Resource.Loading(message = "Skanowanie"))
        }
        isScanning = true
        bleScanner.startScan(null, scanSettings, scanCallback)
    }

    override fun sendMessage(byteArray: ByteArray) {
        val characteristic: BluetoothGattCharacteristic? =
            gatt?.getService(UUID.fromString(SMOGOMETR_SERVICE_UUID))
                ?.getCharacteristic(UUID.fromString(TX_CHARACTERISTIC))

        characteristic?.let { char ->
            char.value = byteArray
            val success = gatt?.writeCharacteristic(char)

            if (success == true) {
                Log.d("onWrite", "wyslano")
            } else {
                Log.d("onWrite", "nie wyslano")
            }
        } ?: run {
            Log.d("onWrite", "brak charakterystyki")
        }
    }

    override fun reconnect() {
        gatt?.connect()
    }

    override fun disconnect() {
        gatt?.disconnect()
    }


    override fun closeConnection() {
        bleScanner.stopScan(scanCallback)
        val characteristic = findCharacteristics(SMOGOMETR_SERVICE_UUID, RX_CHARACTERISTIC)
        //val initialCharacteristic = findCharacteristics(SMOGOMETR_INITIAL_SERVICE_UUID, INITIAL_CHARACTERISTIC)
        if (characteristic != null) {
            disconnectCharacteristic(characteristic)
        }
        gatt?.close()
    }

    private fun disconnectCharacteristic(characteristic: BluetoothGattCharacteristic) {
        val cccdUuid = UUID.fromString(CCCD_DESCRIPTOR_UUID)
        characteristic.getDescriptor(cccdUuid)?.let { cccdDescriptor ->
            if (gatt?.setCharacteristicNotification(characteristic, false) == false) {
                return
            }
            writeDescription(cccdDescriptor, BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE)
        }
    }
}