package agh.ms.smogometr_1.ui.measurement

import agh.ms.smogometr_1.StartMeasure
import agh.ms.smogometr_1.data.ConnectionState
import agh.ms.smogometr_1.data.location.LocationClient
import agh.ms.smogometr_1.data.measurement.MeasurementReceiverManager
import agh.ms.smogometr_1.data.sensors.Sensor
import agh.ms.smogometr_1.data.sensors.SensorState
import agh.ms.smogometr_1.util.Resource
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow
@HiltViewModel
class MeasurementViewModel@Inject constructor(
    private val measurementReceiverManager: MeasurementReceiverManager,
    private val locationClient: LocationClient
) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    private val startLocation: Location? = null

    init {
        viewModelScope.launch {
            try {
                locationClient.getLocationUpdates(2000).collect { location ->
                    _location.value = location
                }
            } catch (e: LocationClient.LocationException) {
                // Handle location exceptions
            }
        }
    }

    private val _isMeasuring = MutableStateFlow(false)
    val isMeasuring: StateFlow<Boolean> get() = _isMeasuring

    private val _buttonText = MutableStateFlow("Rozpocznij pomiar")
    val buttonText: StateFlow<String> get() = _buttonText

    var sensorState by mutableStateOf(SensorState())

    var sensor by mutableStateOf<Sensor?>(null)
        private set
    fun updateSensorState(newSensorState: SensorState) {
        sensorState = newSensorState
    }
    var sliderPosition by mutableStateOf(5f)
    fun updateSliderPosition(newSliderPosition: Float) {
        sliderPosition = newSliderPosition
    }
    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var pm25 by mutableStateOf(0f)
        private set
    var pm10 by mutableStateOf(0f)
        private set
    var nox by mutableStateOf(0f)
        private set


    private fun subscribeToChange(){
        viewModelScope.launch {
            measurementReceiverManager.data.collect{result ->
                when(result) {
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        pm25 = result.data.pm25
                        pm10= result.data.pm10
                        nox = result.data.nox
                    }
                    is Resource.Loading -> {
                        initializingMessage = result.message
                        connectionState = ConnectionState.CurrentlyInitializing
                    }
                    is Resource.Error -> {
                        errorMessage = result.errorMessage
                        connectionState = ConnectionState.Uninitialized
                    }
                }
            }
        }
    }

    fun initializeConnection(){
        errorMessage = null
        subscribeToChange()
        measurementReceiverManager.startReceiving()
    }

    override fun onCleared() {
        super.onCleared()
        measurementReceiverManager.closeConnection()
    }

    fun reconnect(){
        measurementReceiverManager.reconnect()
    }
    fun disconnect(){
        measurementReceiverManager.disconnect()
    }

    fun toggleMeasurement() {
        viewModelScope.launch {
            if (_isMeasuring.value) {
                _isMeasuring.value = false
                _buttonText.value = "Rozpocznij pomiar"
            } else {
                _isMeasuring.value = true
                _buttonText.value = "Trwa pomiar, naciśnij aby zakończyć"
                startMeasure()
            }
        }
    }


    fun startMeasure() {
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


/*
    suspend fun startMeasure() {

        var prevLocation: LatLng = locationLiveData.value?.let {
            LatLng(it.latitude.toDouble(), it.longitude.toDouble())
        } ?: LatLng(0.0, 0.0)

        while (prevLocation == null || prevLocation == LatLng(0.0, 0.0)) {
            // Pobierz nową wartość locationLiveData i zaktualizuj prevLocation
            locationLiveData.value?.let {
                prevLocation = LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            }
        }
        addRandomMeasurement(prevLocation)
        Log.d("measure","dodano 1")

        while (_isMeasuring.value) {
            Log.d("measure","w petli")
            var currentLocation = locationLiveData.value?.let {
                LatLng(it.latitude.toDouble(), it.longitude.toDouble())
            }
            if (currentLocation != null) {
                if (areLocationsFarEnough(currentLocation, prevLocation, sliderPosition)) {
                    addRandomMeasurement(currentLocation)
                    Log.d("measure","dodano kolejny")
                }
                currentLocation.also { prevLocation = it }
            } else {
                Log.d("measure","za blisko")
                // Możesz tutaj obsłużyć sytuację, gdy wartość lokalizacji jest null
                // Na przykład, możesz zalogować błąd lub wyświetlić komunikat do użytkownika.
            }
            delay(2000)
        }
    }
*/
    /*
    fun addRandomMeasurement(currentLocation: LatLng) {
        val randomMeasurement = Measurement(
            ppm25 = getRandomDouble(),
            ppm10 = getRandomDouble(),
            nox = getRandomDouble(),
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
*/

}