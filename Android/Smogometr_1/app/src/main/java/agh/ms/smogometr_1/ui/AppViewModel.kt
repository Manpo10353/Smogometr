package agh.ms.smogometr_1.ui

import agh.ms.smogometr_1.data.ConnectionState
import agh.ms.smogometr_1.data.location.LocationClient
import agh.ms.smogometr_1.data.location.SmogometrLocationClient
import agh.ms.smogometr_1.data.measurement.MeasurementReceiverManager
import agh.ms.smogometr_1.util.Resource
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class AppViewModel: ViewModel()  {






}