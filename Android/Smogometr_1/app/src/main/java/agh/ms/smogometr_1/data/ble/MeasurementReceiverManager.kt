package agh.ms.smogometr_1.data.ble

import agh.ms.smogometr_1.data.measurement.MeasurementResult
import kotlinx.coroutines.flow.MutableSharedFlow

interface MeasurementReceiverManager {

    val data: MutableSharedFlow<Resource<MeasurementResult>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun sendMessage(byteArray: ByteArray)

    fun closeConnection()
}