package agh.ms.smogometr_1.ui

import agh.ms.smogometr_1.data.measurements
import agh.ms.smogometr_1.data.sensors
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    viewModel: MapViewModel = remember { MapViewModel() }
) {
    val agh_a0 = LatLng(50.064504, 19.923284)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(agh_a0, 10f)
    }
    val selectedColor by viewModel.selectedColor.collectAsState()
    val activeButton by viewModel.activeButton.collectAsState()

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
                    viewModel.getColorForButton(ButtonType.PM25)
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
                    viewModel.getColorForButton(ButtonType.PM10)
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
                    viewModel.getColorForButton(ButtonType.NOx)
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
                    viewModel.getColorForButton(ButtonType.SOx)
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    if (activeButton == ButtonType.SOx) Color.Green else Color.Gray
                )
            ) {
                Text("SOx")
            }
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            measurements.forEach { measurement ->
                Circle(
                    center = measurement.latLng,
                    radius = 10.0,
                    fillColor = selectedColor,
                    strokeWidth = 1.0f
                )
            }
        }
    }
}
@Preview(showSystemUi = true)
@Composable
fun MapScreenPreview() {
    MapScreen()
}