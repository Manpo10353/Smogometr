package agh.ms.smogometr_1.ui.map

import agh.ms.smogometr_1.data.location.LocationClient
import agh.ms.smogometr_1.data.measurement.Measurement
import agh.ms.smogometr_1.data.measurement.MeasurementUiState
import android.location.Location
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class MapViewModel@Inject constructor(
    private val locationClient: LocationClient
) : ViewModel() {


    private val _location = MutableStateFlow<Location?>(null)
    val location: StateFlow<Location?> = _location

    init {
        viewModelScope.launch {
            try {
                locationClient.getLocationUpdates(20000).collect { location ->
                    _location.value = location
                }
            } catch (e: LocationClient.LocationException) {
                // Handle location exceptions
            }
        }
    }

    private val _selectedDate = MutableStateFlow<LocalDate?>(LocalDate.now())
    val selectedDate: StateFlow<LocalDate?> get() = _selectedDate

    private val _startTime = MutableStateFlow<LocalTime?>(LocalTime.now().minusHours(1))
    val startTime: StateFlow<LocalTime?> get() = _startTime

    private val _endTime = MutableStateFlow<LocalTime?>(LocalTime.now())
    val endTime: StateFlow<LocalTime?> get() = _endTime


    fun updateStartTime(startTime: LocalTime?) {
        _startTime.value = startTime
    }
    fun updateEndTime(endTime: LocalTime) {
        _endTime.value = endTime
    }
    fun updateSelectedDate(newDate: LocalDate?) {
        _selectedDate.value = newDate
        val selectedDateValue = _selectedDate.value
        val startTimeValue = _startTime.value
        val endTimeValue = _endTime.value

        if (selectedDateValue != null && startTimeValue != null && endTimeValue != null) {
            fetchDataForSelectedDayAndTimeRange(selectedDateValue, startTimeValue, endTimeValue)
        } else {
        }
    }



    private val _selectedColor = MutableStateFlow(Color.Red)
    val selectedColor: StateFlow<Color> = _selectedColor

    private val _activeButton = MutableStateFlow(ButtonType.PM25)
    val activeButton: StateFlow<ButtonType> = _activeButton


    private val _measurementsUiStates = MutableStateFlow<MutableList<MeasurementUiState>>(mutableListOf())
    val measurementsUiStates: StateFlow<MutableList<MeasurementUiState>> get() = _measurementsUiStates


    //dodać wartości dla pm10,pm25,nox
    //obsługa przycisku i kolory dla różnych zanieczyszczeń
    private var minValue = 0.0
    private var maxValue = 0.0
    private var value = 0.0

    fun calculateFillColor(measurement: MeasurementUiState): Color {
        when(_activeButton.value){
            ButtonType.PM25 -> {
                maxValue = 110.0
                value = measurement.pm25.toDouble()
            }
            ButtonType.PM10 -> {
                maxValue = 150.0
                value = measurement.pm10.toDouble()
            }
            ButtonType.NOx -> {
                maxValue = 400.0
                value = measurement.nox.toDouble()
            }
        }
        val normalizedValue = (value - minValue) / (maxValue - minValue)

        val hue = (1.0 - normalizedValue) * 140.0

        val saturation = 1.0
        val brightness = 1.0

        return Color(
            android.graphics.Color.HSVToColor(
                floatArrayOf(
                    hue.toFloat(),
                    saturation.toFloat(),
                    brightness.toFloat()
                )
            )
        )
    }


fun fetchDataForSelectedDayAndTimeRange(
    selectedDate: LocalDate,
    startTime: LocalTime,
    endTime: LocalTime,
) {
    val database:FirebaseDatabase = FirebaseDatabase.getInstance("https://smogometr-1-default-rtdb.europe-west1.firebasedatabase.app")
    val reference: DatabaseReference = database.getReference("measurements")

    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

    // Formatowanie daty i czasu dla zapytania
    val formattedSelectedDate = selectedDate
    val formattedStartTime = startTime
    val formattedEndTime = endTime.format(timeFormatter)

    val zoneOffSet = ZoneOffset.of("+01:00")

    //val startTimestamp = selectedDate.atTime(startTime).toEpochSecond(zoneOffSet)
    //val stopTimestamp = selectedDate.atTime(endTime).toEpochSecond(zoneOffSet)

    val startDateTime = ZonedDateTime.of(selectedDate, startTime, zoneOffSet)
    val stopDateTime = ZonedDateTime.of(selectedDate, endTime, zoneOffSet)

    val startTimestamp = startDateTime.toEpochSecond()*1000
    val stopTimestamp = stopDateTime.toEpochSecond()*1000


// Query for timestamp range
    val query = reference.orderByChild("timestamp")
        .startAt(startTimestamp.toDouble())
        .endAt(stopTimestamp.toDouble())
    query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val measurements = mutableListOf<MeasurementUiState>()
                for (snapshot in dataSnapshot.children) {
                val measurement = snapshot.getValue(Measurement::class.java)

                measurement?.let { measurements.add(createMeasurementUiState(it)) }
            }

            _measurementsUiStates.value = measurements
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.d("db","Błąd podczas pobieranie pomiaru")
        }
    })
    //Log.d("db","${resultList}")
}

    fun createMeasurementUiState(measurement: Measurement): MeasurementUiState {
        return MeasurementUiState(
            id = 0, // Tutaj możesz ustawić odpowiednie id
            latitude = measurement.latitude,
            longitude = measurement.longitude,
            pm25 = measurement.pm25,
            pm10 = measurement.pm10,
            nox = measurement.nox,
            humidity = measurement.humidity
        )
    }
     fun changeActiveButton(buttonType: ButtonType) {
            _activeButton.value = buttonType
    }

    fun calculateStrokeColor(measurement: MeasurementUiState): Color =
        if (measurement.humidity >= 60.0) Color.Blue else Color.Black

}



enum class ButtonType {
    PM25,
    PM10,
    NOx,
}