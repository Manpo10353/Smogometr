package agh.ms.smogometr_1.data

import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng

data class Measurement(
    val id: Int,
    val latLng: LatLng,
    val PM25: Double,
    val PM10: Double,
    val NOx: Double,
    val SOx: Double,
    val PM25Color: Color,
    val PM10Color: Color,
    val NOxColor: Color,
    val SOxColor: Color,
    val temperature: Double,
    val humidity: Double,

)

val measurements = listOf(
Measurement(1,LatLng(50.0645, 19.9232),2.5,1.0,5.0,4.0, Color.Red,Color.Green,Color.Yellow,Color.Blue,20.0,10.0),
Measurement(2,LatLng(50.0646, 19.9233),2.5,1.0,5.0,4.0,Color.Red,Color.Green,Color.Yellow,Color.Blue,20.0,10.0),
Measurement(3,LatLng(50.0647, 19.9234),2.5,1.0,5.0,4.0,Color.Red,Color.Green,Color.Yellow,Color.Blue,20.0,10.0),
Measurement(4,LatLng(50.0648, 19.9235),2.5,1.0,5.0,4.0,Color.Red,Color.Green,Color.Yellow,Color.Blue,20.0,10.0)
)