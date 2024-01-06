package agh.ms.smogometr_1.data.measurement

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime

class OfflineMeasurementRepository(
    private val measurementDao: MeasurementDao
): MeasurementRepository {
    override fun getMeasurementsBetweenHours(
        //date: LocalDate,
        //timeStart: LocalTime,
        //timeEnd: LocalTime
    ): Flow<List<Measurement>> =
        measurementDao.getMeasurementsBetweenHours(
        //date,
        //timeStart,
        //timeEnd
        )

    override suspend fun insertMeasurment(measurement: Measurement) = withContext(Dispatchers.IO) {
        measurementDao.insertMeasurement(measurement)
    }
}