package agh.ms.smogometr_1.data.measurement

import agh.ms.smogometr_1.data.ConnectionState

data class MeasurementResult(
    val pm25: Float = 0f,
    val pm10: Float = 0f,
    val nox: Float = 0f,
    val connectionState: ConnectionState
)
