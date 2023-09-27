package com.ms.smogometr_2

import android.content.Context
import android.view.KeyEvent.DispatcherState
import com.ms.smogometr_2.db.Measurement
import com.ms.smogometr_2.db.MeasurementDatabase
import com.ms.smogometr_2.db.MeasurementDb
import com.ms.smogometr_2.db.MeasurementsDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.random.Random

class Repository(context: Context) : MeasurementsDAO{ // imitacja danych
    private val dao = MeasurementDb.getInstance(context).MeasurementsDAO()
    override suspend fun insertMeasurements(measurement: List<Measurement>) = withContext(
        Dispatchers.IO){
        dao.insertMeasurements(measurement)
    }

    override suspend fun insertMeasurement(measurement: Measurement) = withContext(
        Dispatchers.IO){
        dao.insertMeasurement(measurement)
    }

    override fun getALL(): Flow<List<Measurement>>{
        return dao.getALL()
    }

}