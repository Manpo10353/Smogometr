package agh.ms.smogometr_1.data.measurement

import android.location.Location
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.ServerValue
import java.time.LocalDate
import java.time.LocalTime
data class Measurement(
    val id: String? = "",
    val latitude: Double,
    val longitude:Double,
    val pm25: Int = 0,
    val pm10: Int = 0,
    val nox: Float = 0f,
    val humidity: Float = 0f,
    val timestamp: Any? = ServerValue.TIMESTAMP

){
    constructor() : this(
        "",
        0.0,
        0.0,
        0,
        0,
        0f,
        0f,
        ServerValue.TIMESTAMP
    )
}