package agh.ms.smogometr_1.ui.measurement

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.Flow

interface SensorDataStore {
    val sensorStateFlow: Flow<SensorState>
    suspend fun updateSensorState(updatedState: SensorState)
}