package agh.ms.smogometr_1.data

import agh.ms.smogometr_1.data.measurement.MeasurementDatabase
import agh.ms.smogometr_1.data.measurement.MeasurementRepository
import agh.ms.smogometr_1.data.measurement.OfflineMeasurementRepository
import android.content.Context

interface AppContainer {
    val measurementRepository: MeasurementRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val measurementRepository: MeasurementRepository by lazy {
        OfflineMeasurementRepository(MeasurementDatabase.getDatabase(context).measurementDao())
    }

}