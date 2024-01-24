package agh.ms.smogometr_1.data.sensors


data class Sensor(
    val name: String?,
    val address: String,
    val isConnected: Boolean,
    val components: List<Pair<String, Boolean>>
)
