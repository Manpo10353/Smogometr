package agh.ms.smogometr_1.data.measurement

import agh.ms.smogometr_1.util.Resource
import kotlinx.coroutines.flow.MutableSharedFlow

interface MeasurementReceiverManager {

    val data: MutableSharedFlow<Resource<MeasurementResult>>

    fun reconnect()

    fun disconnect()

    fun startReceiving()

    fun closeConnection()
}