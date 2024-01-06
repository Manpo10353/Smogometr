package agh.ms.smogometr_1.ui.measurement

data class SensorState(
    val ppm25Checked: Boolean = false,
    val ppm10Checked: Boolean = false,
    val noxChecked: Boolean = false,
    val soxChecked: Boolean = false,
)