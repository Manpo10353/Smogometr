package agh.ms.smogometr_1.data.sensors

import agh.ms.smogometr_1.data.ConnectionState


data class Sensor(
    val name: String?,
    val components: List<Pair<String, Boolean>>,
    val isConnected: Boolean,
)
