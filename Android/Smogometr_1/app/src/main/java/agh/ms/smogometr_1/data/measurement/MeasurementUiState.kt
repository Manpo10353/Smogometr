package agh.ms.smogometr_1.data.measurement

import android.location.Location
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

data class MeasurementUiState(
    val id: Int = 0,
    val latitude: Double,
    val longitude:Double,
    val pm25: Int,
    val pm10: Int,
    val nox: Float,
    val humidity:Float,
)
