package agh.ms.smogometr_1.ui

import agh.ms.smogometr_1.data.measurements
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MapViewModel : ViewModel() {

    private val _selectedColor = MutableStateFlow<Color>(measurements.first().PM25Color)
    val selectedColor: StateFlow<Color> = _selectedColor

    private val _activeButton = MutableStateFlow<ButtonType>(ButtonType.PM25)
    val activeButton: StateFlow<ButtonType> = _activeButton

    fun getColorForButton(buttonType: ButtonType) {
        viewModelScope.launch {
            _selectedColor.value = when (buttonType) {
                ButtonType.PM25 -> measurements.first().PM25Color
                ButtonType.PM10 -> measurements.first().PM10Color
                ButtonType.NOx -> measurements.first().NOxColor
                ButtonType.SOx -> measurements.first().SOxColor
            }
            _activeButton.value = buttonType
        }
    }
}

enum class ButtonType {
    PM25,
    PM10,
    NOx,
    SOx
}