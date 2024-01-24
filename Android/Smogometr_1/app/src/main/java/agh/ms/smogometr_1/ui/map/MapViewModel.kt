package agh.ms.smogometr_1.ui.map

import agh.ms.smogometr_1.data.location.LocationClient
import agh.ms.smogometr_1.data.measurement.Measurement
import agh.ms.smogometr_1.data.measurement.MeasurementUiState
import android.location.Location
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
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
                locationClient.getLocationUpdates(2000).collect { location ->
                    _location.value = location
                }
            } catch (e: LocationClient.LocationException) {
                // Handle location exceptions
            }
        }
    }

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)
    val selectedDate: StateFlow<LocalDate?> get() = _selectedDate

    private val _startTime = MutableStateFlow<LocalTime?>(LocalTime.now())
    val startTime: StateFlow<LocalTime?> get() = _startTime

    private val _endTime = MutableStateFlow<LocalTime?>(LocalTime.now().plusHours(1))
    val endTime: StateFlow<LocalTime?> get() = _endTime


    fun updateStartTime(startTime: LocalTime?) {
        _startTime.value = startTime
    }
    fun updateEndTime(endTime: LocalTime) {
        _endTime.value = endTime
    }
    fun updateSelectedDate(newDate: LocalDate?) {
        _selectedDate.value = newDate
    }



    private val _selectedColor = MutableStateFlow(Color.Red)
    val selectedColor: StateFlow<Color> = _selectedColor

    private val _activeButton = MutableStateFlow(ButtonType.PM25)
    val activeButton: StateFlow<ButtonType> = _activeButton


    private val _measurementsUiStates = MutableStateFlow<List<MeasurementUiState>>(emptyList())
    val measurementsUiStates: StateFlow<List<MeasurementUiState>> get() = _measurementsUiStates



    //fun getLocationLiveData() = locationLiveData
    //fun startLocationUpdates() = {
    //    locationLiveData.startLocationUpdates()
    //}

    init{
        //fetchDataBetweenHours()
    }

/*
    fun deleteAllMeasurements() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                measurementRepository.deleteAllMeasurements()
            }
        }
    }
*/
    fun Measurement.toMeasurementUiState(): MeasurementUiState {
        return MeasurementUiState(
            latLng = this.latLng,
            //date = this.date,
            ppm25 = this.ppm25,
            ppm10 = this.ppm10,
            nox = this.nox,
            ppm25Color = Color.Red,//calculateColor(ppm25),
            ppm10Color = Color.Red,//calculateColor(ppm10),
            noxColor = Color.Red,//calculateColor(nox),
        )
    }

    fun calculateColor(value: Double): Color {
        val normalizedValue = (value - minValue) / (maxValue - minValue)

        val hue = (1.0 - normalizedValue) * 120.0

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

    private val minValue = 0.0
    private val maxValue = 10.0
/*
    fun fetchDataBetweenHours(/*date: LocalDate, timeStart: LocalTime, timeEnd: LocalTime*/) {
        viewModelScope.launch {
            try {
                measurementRepository.getMeasurementsBetweenHours(/*date, timeStart, timeEnd*/)
                    .collect { measurements ->
                        _measurementsUiStates.value = measurements.map { it.toMeasurementUiState() }
                    }
            } catch (e: Exception) {
                // Handle exceptions, e.g., log or display an error message
            }
        }
    }

 */
     fun changeActiveButton(buttonType: ButtonType) {
       viewModelScope.launch {
            _selectedColor.value = when (buttonType) {
                ButtonType.PM25 -> Color.Blue
                ButtonType.PM10 -> Color.Blue
                ButtonType.NOx ->  Color.Blue
                ButtonType.SOx ->  Color.Blue
            }
            _activeButton.value = buttonType
        }
    }

/*
    fun setSelectedDate(date: Date) {
        viewModelScope.launch {
            _selectedDate.value = date
        }
    }
     
 */
}



enum class ButtonType {
    PM25,
    PM10,
    NOx,
    SOx
}