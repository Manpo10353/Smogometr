package agh.ms.smogometr_1.data


data class Sensor(
    val id: Int,
    val batteryLevel: Int,
    val isConnected: Boolean,
    val components: List<Pair<String, Boolean>>
)

val sensors = listOf(
    Sensor(0, 95, true, listOf( "NOx" to true, "SOx" to false, "PM" to true, "Benzo(a)pyrene" to false)),
    Sensor(1, 55, true, listOf( "NOx" to true, "SOx" to false, "PM" to true, "Benzo(a)pyrene" to false)),
    Sensor(2, 25, true, listOf( "NOx" to true, "SOx" to false, "PM" to true, "Benzo(a)pyrene" to false)),
    Sensor(3, 4, true, listOf( "NOx" to true, "SOx" to false, "PM" to true, "Benzo(a)pyrene" to false)),
)
val sensorConnected : Sensor = (
    Sensor(4, 95, true, listOf( "NOx" to true, "SOx" to false, "PM" to true, "Benzo(a)pyrene" to false))
)