package agh.ms.smogometr_1.ui.map

import agh.ms.smogometr_1.data.measurement.MeasurementRepository
import agh.ms.smogometr_1.ui.AppViewModelProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel

import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier,
) {

    val aghA0 = LatLng(50.064504, 19.923284)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(aghA0, 10f)
    }
    val selectedColor by viewModel.selectedColor.collectAsState()
    val activeButton by viewModel.activeButton.collectAsState()
    val measurementUiState by viewModel.measurementUiStates.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                  //  viewModel.getColorForButton(ButtonType.PM25)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    if (activeButton == ButtonType.PM25) Color.Green else Color.Gray
                )
            ) {
                Text("PM2.5")
            }

            Button(
                onClick = {
                   // viewModel.getColorForButton(ButtonType.PM10)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    if (activeButton == ButtonType.PM10) Color.Green else Color.Gray
                )
            ) {
                Text("PM10")
            }

            Button(
                onClick = {
                 //   viewModel.getColorForButton(ButtonType.NOx)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    if (activeButton == ButtonType.NOx) Color.Green else Color.Gray
                )
            ) {
                Text("NOx")
            }

            Button(
                onClick = {
                   // viewModel.getColorForButton(ButtonType.SOx)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    if (activeButton == ButtonType.SOx) Color.Green else Color.Gray
                )
            ) {
                Text("SOx")
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
            ){
                Text("9:00")
                
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .weight(1f)
            ){
                Text("10:00")

            }
            Button(
                onClick = {
                    // Pokaż DatePicker, np. za pomocą BottomSheet lub innego interfejsu użytkownika
                },
                modifier = Modifier
                    .weight(2f)
            ) {
                Text("${viewModel.selectedDate.value?.toString() ?: "Wybierz dzień"}")
            }
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            measurementUiState.forEach { measurementUiState ->
                Circle(
                    center = LatLng(50.0645, 19.9232),//measurementUiState.latLng,
                    radius = 10.0,
                    fillColor = measurementUiState.ppm25Color,
                    strokeWidth = 1.0f
                )
            }
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun MapScreenPreview() {
    //MapScreen(measurementRepository)
}