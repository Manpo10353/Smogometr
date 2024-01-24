package agh.ms.smogometr_1.di

import agh.ms.smogometr_1.data.ble.MeasurementBLEReceiverManager
import agh.ms.smogometr_1.data.location.LocationClient
import agh.ms.smogometr_1.data.location.SmogometrLocationClient
import agh.ms.smogometr_1.data.measurement.MeasurementReceiverManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothAdapter(
        @ApplicationContext context: Context
    ):BluetoothAdapter
    {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        return manager.adapter
    }

    @Provides
    @Singleton
    fun provideMeasurementReceiver(
        @ApplicationContext context: Context,
        bluetoothAdapter: BluetoothAdapter
    ): MeasurementReceiverManager {
        return MeasurementBLEReceiverManager(bluetoothAdapter,context)
    }
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }
    @Provides
    @Singleton
    fun provideLocationClient(
        @ApplicationContext context: Context,
    ): LocationClient {
        return SmogometrLocationClient(context)
    }

}