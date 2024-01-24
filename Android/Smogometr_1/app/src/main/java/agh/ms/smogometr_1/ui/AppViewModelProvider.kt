package agh.ms.smogometr_1.ui

import agh.ms.smogometr_1.SmogometrApplication
import agh.ms.smogometr_1.ui.map.MapViewModel
import agh.ms.smogometr_1.ui.measurement.MeasurementViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
/*
object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            MapViewModel(smogometrApplication().container.measurementRepository, smogometrApplication().container.locationLiveData)
        }
        initializer {
            MeasurementViewModel(smogometrApplication().container.measurementRepository, smogometrApplication().container.locationLiveData)
        }
    }
}*/
fun CreationExtras.smogometrApplication(): SmogometrApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SmogometrApplication)