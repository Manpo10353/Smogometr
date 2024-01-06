package agh.ms.smogometr_1.data.measurement

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.android.gms.maps.model.LatLng
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

@Entity(tableName = "measurements")
data class Measurement(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    //@TypeConverters(Converters::class)
    //val latLng: LatLng,
    //@TypeConverters(Converters::class)
    //val date: Date,
    val ppm25: Double,
    val ppm10: Double,
    val nox: Double,
    val sox: Double,
    val temperature: Double,
    val humidity: Double,
)