package agh.ms.smogometr_1.ui.measurement

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SensorViewModel : ViewModel() {

    // _sensorState używany jako MutableStateFlow
    private val _sensorState = MutableStateFlow(SensorState())

    // sensorState używany jako StateFlow
    val sensorState: StateFlow<SensorState> = _sensorState.asStateFlow()

    // Wersja używająca remember + mutableStateOf
    // var sensorState by remember { mutableStateOf(SensorState()) }
    //    private set

    fun updateSensorState(updatedState: SensorState) {
        // Uaktualnienie _sensorState
        viewModelScope.launch {
            _sensorState.emit(updatedState)
        }

        // Uaktualnienie sensorState w przypadku użycia remember + mutableStateOf
        // sensorState = updatedState
    }
}