package agh.ms.smogometr_1.ui.measurement

import agh.ms.smogometr_1.StartMeasure
import agh.ms.smogometr_1.data.LocationDetails
import agh.ms.smogometr_1.data.LocationLiveData
import agh.ms.smogometr_1.data.measurement.Measurement
import agh.ms.smogometr_1.data.measurement.MeasurementRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch
import kotlin.math.pow

class DynamicMeasurementViewModel(
    private val measurementRepository: MeasurementRepository,
    private val locationLiveData: LocationLiveData
) : ViewModel() {

    var sensorState by mutableStateOf(SensorState())


    var sliderPosition by mutableStateOf(5f)
    fun updateSensorState(newSensorState: SensorState) {
        sensorState = newSensorState
    }
    fun updateSliderPosition(newSliderPosition: Float) {
        sliderPosition = newSliderPosition
    }

    fun getLocationLiveData() = locationLiveData
    fun startLocationUpdates() = {

        locationLiveData.startLocationUpdates()
    }

    private val _locationLiveData = locationLiveData
    val locationLiveDataM: LiveData<LocationDetails> get() = _locationLiveData

    fun startMeasure_t() {
        val measurement = StartMeasure()
        measurement.beginMeasurement(sliderPosition,sensorState)
    }

////////////////////
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

        val a = Math.sin(dLat / 2)
            .pow(2) + Math.cos(lat1Rad) * Math.cos(lat2Rad) * Math.sin(dLon / 2).pow(2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }

    fun areLocationsFarEnough(location1: LatLng, location2: LatLng, minDistance: Float): Boolean {
        val actualDistance = haversineDistance(location1, location2)
        return actualDistance >= minDistance
    }
    /////////////////



    fun startMeasure() {
        val prevLocation = locationLiveData.value!!.let {
            LatLng(it.latitude.toDouble(), it.longitude.toDouble())
        }

        val currentLocation = locationLiveData.value?.let {
            LatLng(it.latitude.toDouble(), it.longitude.toDouble())
        }
        if (currentLocation != null) {
            areLocationsFarEnough(currentLocation,prevLocation,sliderPosition)
            addRandomMeasurement(currentLocation)
        } else {
            // Możesz tutaj obsłużyć sytuację, gdy wartość lokalizacji jest null
            // Na przykład, możesz zalogować błąd lub wyświetlić komunikat do użytkownika.
        }
    }

    fun addRandomMeasurement(currentLocation: LatLng) {
        val randomMeasurement = Measurement(
            ppm25 = getRandomDouble(),
            ppm10 = getRandomDouble(),
            nox = getRandomDouble(),
            sox = getRandomDouble(),
            temperature = getRandomDouble(),
            humidity = getRandomDouble(),
            latLng = LatLng(currentLocation.latitude,currentLocation.longitude)
        )

        viewModelScope.launch {
            try {
                measurementRepository.insertMeasurment(randomMeasurement)
            } catch (e: Exception) {
                // Handle exceptions, e.g., log or display an error message
            }
        }
    }

    private fun getRandomDouble(): Double {
        return 10.0 * Math.random()
    }
}