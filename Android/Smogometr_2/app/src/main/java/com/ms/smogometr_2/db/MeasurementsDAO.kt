package com.ms.smogometr_2.db

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MeasurementsDAO {

    suspend fun insertMeasurements(measurement: List<Measurement>)
    suspend fun insertMeasurement(measurement: Measurement)

    @Query("SELECT * FROM measurements")
    fun getALL(): Flow<List<Measurement>>

}