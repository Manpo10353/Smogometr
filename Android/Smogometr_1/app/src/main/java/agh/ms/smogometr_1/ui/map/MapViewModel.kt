package agh.ms.smogometr_1.ui.map

import agh.ms.smogometr_1.data.measurement.Measurement
import agh.ms.smogometr_1.data.measurement.MeasurementRepository
import agh.ms.smogometr_1.data.measurement.MeasurementUiState
import agh.ms.smogometr_1.ui.AppViewModel
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.util.Date

class MapViewModel(
    private val measurementRepository: MeasurementRepository) : ViewModel()
{
    private val _selectedColor = MutableStateFlow<Color>(Color.Red)
    val selectedColor: StateFlow<Color> = _selectedColor

    private val _activeButton = MutableStateFlow<ButtonType>(ButtonType.PM25)
    val activeButton: StateFlow<ButtonType> = _activeButton

    private val _selectedDate = MutableStateFlow<Date?>(null)
    val selectedDate: StateFlow<Date?> = _selectedDate

    private val _measurementUiStates = MutableStateFlow<List<MeasurementUiState>>(emptyList())
    val measurementUiStates: StateFlow<List<MeasurementUiState>> get() = _measurementUiStates

    init{
        fetchDataBetweenHours()
    }

    fun Measurement.toMeasurementUiState(): MeasurementUiState {
        return MeasurementUiState(
            //latLng = this.latLng,
            //date = this.date,
            ppm25 = this.ppm25,
            ppm10 = this.ppm10,
            nox = this.nox,
            sox = this.sox,
            temperature = this.temperature,
            humidity = this.humidity,
            ppm25Color = calculateColor(ppm25),
            ppm10Color = calculateColor(ppm10),
            noxColor = calculateColor(nox),
            soxColor = calculateColor(sox)
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

    // Zakres wartości
    private val minValue = 0.0
    private val maxValue = 10.0

    fun fetchDataBetweenHours(/*date: LocalDate, timeStart: LocalTime, timeEnd: LocalTime*/) {
        viewModelScope.launch {
            try {
                measurementRepository.getMeasurementsBetweenHours(/*date, timeStart, timeEnd*/)
                    .collect { measurements ->
                        _measurementUiStates.value = measurements.map { it.toMeasurementUiState() }
                    }
            } catch (e: Exception) {
                // Handle exceptions, e.g., log or display an error message
            }
        }
    }
    // fun getColorForButton(buttonType: ButtonType) {
    /*   viewModelScope.launch {
            _selectedColor.value = when (buttonType) {
                ButtonType.PM25 -> Color.Blue
                ButtonType.PM10 -> Color.Blue
                ButtonType.NOx ->  Color.Blue
                ButtonType.SOx ->  Color.Blue
            }
            _activeButton.value = buttonType
        }
    }

    fun setSelectedDate(date: Date) {
        viewModelScope.launch {
            _selectedDate.value = date
        }
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