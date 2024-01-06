package agh.ms.smogometr_1.data.measurement

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

@Dao
interface MeasurementDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMeasurement(measurement: Measurement)

    @Query("SELECT * FROM measurements ")//WHERE date = :date AND time <= :timeStart AND time >= :timeEnd")
    fun getMeasurementsBetweenHours(): Flow<List<Measurement>>
    //fun getMeasurementsBetweenHours(date: LocalDate,timeStart: LocalTime, timeEnd: LocalTime): Flow<List<Measurement>>
}