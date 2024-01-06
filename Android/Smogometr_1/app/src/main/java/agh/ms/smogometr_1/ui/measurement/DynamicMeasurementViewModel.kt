package agh.ms.smogometr_1.ui.measurement

import agh.ms.smogometr_1.data.measurement.Measurement
import agh.ms.smogometr_1.data.measurement.MeasurementRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DynamicMeasurementViewModel(
    private val measurementRepository: MeasurementRepository) : ViewModel()
{

    fun addRandomMeasurement() {
        val randomMeasurement = Measurement(
            ppm25 = getRandomDouble(),
            ppm10 = getRandomDouble(),
            nox = getRandomDouble(),
            sox = getRandomDouble(),
            temperature = getRandomDouble(),
            humidity = getRandomDouble(),
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
        // Wprowadź dowolny sposób generowania losowej liczby z zakresu, np. Random.nextDouble()
        return 10.0 * Math.random()
    }
}