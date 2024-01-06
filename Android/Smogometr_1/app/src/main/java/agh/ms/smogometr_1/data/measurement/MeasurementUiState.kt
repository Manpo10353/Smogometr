package agh.ms.smogometr_1.data.measurement

import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

data class MeasurementUiState(
    val id: Int = 0,
    //val latLng: LatLng,
    //val date: Date,
    val ppm25: Double,
    val ppm10: Double,
    val nox: Double,
    val sox: Double,
    val temperature: Double,
    val humidity: Double,
    //circle colors depends on norm
    val ppm25Color: Color,
    val ppm10Color: Color,
    val noxColor: Color,
    val soxColor: Color,
)
