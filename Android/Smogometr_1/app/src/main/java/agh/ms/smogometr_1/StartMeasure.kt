package agh.ms.smogometr_1

import agh.ms.smogometr_1.ui.measurement.SensorState
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import java.lang.Math.atan2
import java.lang.Math.cos
import java.lang.Math.sin
import java.lang.Math.sqrt
import kotlin.math.pow

class StartMeasure {


    fun beginMeasurement(sliderPosition: Float, sensorState: SensorState) {
       // val transferDataBLE = transferDataBLE()
        val presentLocation: LatLng = LatLng(0.0,0.0) //początkowa,
        val actualLocation: LatLng = LatLng(0.0,0.0)//do zmiany na stateflow
       // if (areLocationsFarEnough(presentLocation,actualLocation,sliderPosition)):
            //transferDataBLE.send(sensorState)

    }

    fun haversineDistance(location1: LatLng, location2: LatLng): Double {
        val earthRadius = 6371000.0

        val lat1Rad = Math.toRadians(location1.latitude)
        val lon1Rad = Math.toRadians(location1.longitude)
        val lat2Rad = Math.toRadians(location2.latitude)
        val lon2Rad = Math.toRadians(location2.longitude)

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        val a = sin(dLat / 2).pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    fun areLocationsFarEnough(location1: LatLng, location2: LatLng, minDistance: Float): Boolean {
        val actualDistance = haversineDistance(location1, location2)
        return actualDistance >= minDistance
    }
}