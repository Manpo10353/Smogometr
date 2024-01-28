package agh.ms.smogometr_1.data.measurement

import agh.ms.smogometr_1.data.ConnectionState

data class MeasurementResult(
    val pm25: Int = 0,
    val pm10: Int = 0,
    val nox: Float = 0f,
    val humidity: Float =0f,
    val connectionState: ConnectionState
)
