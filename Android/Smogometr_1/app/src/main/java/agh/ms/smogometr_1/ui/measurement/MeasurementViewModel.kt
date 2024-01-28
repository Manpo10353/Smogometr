package agh.ms.smogometr_1.ui.measurement

import agh.ms.smogometr_1.data.ConnectionState
import agh.ms.smogometr_1.data.location.LocationClient
import agh.ms.smogometr_1.data.measurement.Measurement
import agh.ms.smogometr_1.data.ble.MeasurementReceiverManager
import agh.ms.smogometr_1.data.sensors.Sensor
import agh.ms.smogometr_1.data.ble.Resource
import android.location.Location
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

@HiltViewModel
class MeasurementViewModel@Inject constructor(
    private val measurementReceiverManager: MeasurementReceiverManager,
    private val locationClient: LocationClient
) : ViewModel() {

    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    private var startLocation: Location? = null


    val smogometr:Sensor = Sensor(
        "Smogometr",
        listOf("PM 2.5" to true,"PM 10" to true, "NOx" to true),
        true
    )

    init {
        viewModelScope.launch {
            try {
                locationClient.getLocationUpdates(1000).collect { location ->
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

    var sliderPosition by mutableFloatStateOf(5f)
    fun updateSliderPosition(newSliderPosition: Float) {
        sliderPosition = newSliderPosition
    }
    var connectionState by mutableStateOf<ConnectionState>(ConnectionState.Uninitialized)

    var initializingMessage by mutableStateOf<String?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _startTime = MutableStateFlow<LocalTime?>(LocalTime.now())
    val startTime: StateFlow<LocalTime?> get() = _startTime

    var pm25 by mutableStateOf(0)
        private set
    var pm10 by mutableStateOf(0)
        private set
    var nox by mutableStateOf(0f)
        private set
    var humidity by mutableStateOf(0f)
        private set

    fun sendMessage() {
        measurementReceiverManager.sendMessage("measure".toByteArray())
    }

    private fun subscribeToChange(){
        viewModelScope.launch {
            measurementReceiverManager.data.collect{result ->
                when(result) {
                    is Resource.Success -> {
                        connectionState = result.data.connectionState
                        pm25 = result.data.pm25
                        pm10= result.data.pm10
                        nox = result.data.nox
                        humidity = result.data.humidity
                        Log.d("ble","odebrano")
                        addMeasurement(
                            latitude=startLocation!!.latitude,
                            longitude=startLocation!!.longitude,
                            pm25 = pm25,
                            pm10 = pm10,
                            nox = nox,
                            humidity = humidity
                        )
                        startLocation = location.value
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
                _buttonText.value = "Trwa pomiar, naciśnij aby zakończyć"
                startLocation = location.value
                sendMessage()
                _isMeasuring.value = true
            }
        }
    }

    private fun haversineDistance(location1: Location, location2: Location): Double {
        val earthRadius = 6371000.0

        val lat1Rad = Math.toRadians(location1.latitude)
        val lon1Rad = Math.toRadians(location1.longitude)
        val lat2Rad = Math.toRadians(location2.latitude)
        val lon2Rad = Math.toRadians(location2.longitude)

        val dLat = lat2Rad - lat1Rad
        val dLon = lon2Rad - lon1Rad

        val a = sin(dLat / 2)
            .pow(2) + cos(lat1Rad) * cos(lat2Rad) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    fun checkDistance(): Boolean {
        val distance = haversineDistance(startLocation!!, location.value!!)
        Log.d("distance","${distance}")
        Log.d("slider","${sliderPosition}")
        return distance >= sliderPosition
    }

    fun addMeasurement(
        latitude: Double,
        longitude:Double,
        pm25: Int,
        pm10: Int,
        nox: Float,
        humidity: Float
    ) {
        Log.d("db","uruchomiono dodawanie")
        val database = FirebaseDatabase.getInstance("https://smogometr-1-default-rtdb.europe-west1.firebasedatabase.app")
        val reference = database.getReference("measurements")
        val measurementId = reference.push().key

        val newMeasurement = Measurement(
            measurementId,
            latitude,
            longitude,
            pm25,
            pm10,
            nox,
            humidity
        )
        reference.child(measurementId!!).setValue(newMeasurement)
            .addOnSuccessListener {
                Log.d("db","Dodano nowy pomiar do bazy danych.")
            }
            .addOnFailureListener { e ->
                Log.d("db","Błąd podczas dodawania pomiaru: $e")
            }
    }

}