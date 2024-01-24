package agh.ms.smogometr_1.data.sensors

import kotlinx.coroutines.flow.Flow

interface SensorDataStore {
    val sensorStateFlow: Flow<SensorState>
    suspend fun updateSensorState(updatedState: SensorState)
}