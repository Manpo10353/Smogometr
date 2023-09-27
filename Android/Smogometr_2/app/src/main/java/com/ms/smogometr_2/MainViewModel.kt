package com.ms.smogometr_2

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ms.smogometr_2.db.Measurement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.random.Random


class MainViewModel(app: Application): AndroidViewModel(app) {
    private val repository = Repository(app.applicationContext)

    init {
        measurementsDatabase()
    }
    fun getMeasurements(): Flow<List<Measurement>> {
        return repository.getALL()
    }
    private fun generateRandomLocation(): Location {
        val random = Random.Default
        val location = Location("dummyProvider")

        // Generowanie losowych współrzędnych
        val latitude = random.nextDouble(-90.0, 90.0)
        val longitude = random.nextDouble(-180.0, 180.0)

        location.latitude = latitude
        location.longitude = longitude

        return location
    }

    private fun measurementsDatabase(){
        repeat(10){
            val random = Random.nextDouble(0.0, 100.0)
            val measurement = Measurement(value = "$random", date = Date(), location = generateRandomLocation())
            CoroutineScope(viewModelScope.coroutineContext).launch(){
                repository.insertMeasurement(measurement)

        }
        }
    }
}