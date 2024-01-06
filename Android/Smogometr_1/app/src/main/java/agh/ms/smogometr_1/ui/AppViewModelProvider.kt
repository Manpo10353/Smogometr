package agh.ms.smogometr_1.ui

import agh.ms.smogometr_1.SmogometrApplication
import agh.ms.smogometr_1.ui.map.MapViewModel
import agh.ms.smogometr_1.ui.measurement.DynamicMeasurementViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            MapViewModel(smogometrApplication().container.measurementRepository)
        }
        initializer {
            DynamicMeasurementViewModel(smogometrApplication().container.measurementRepository)
        }
    }
}
fun CreationExtras.smogometrApplication(): SmogometrApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SmogometrApplication)