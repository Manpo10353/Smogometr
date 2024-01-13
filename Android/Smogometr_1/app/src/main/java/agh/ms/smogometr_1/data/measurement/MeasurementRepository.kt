package agh.ms.smogometr_1.data.measurement

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

interface MeasurementRepository {
    suspend fun insertMeasurment(measurement: Measurement)

    //fun getMeasurementsBetweenHours(date: LocalDate, timeStart: LocalTime, timeEnd: LocalTime): Flow<List<Measurement>>
    fun getMeasurementsBetweenHours(): Flow<List<Measurement>>

    fun deleteAllMeasurements()
}