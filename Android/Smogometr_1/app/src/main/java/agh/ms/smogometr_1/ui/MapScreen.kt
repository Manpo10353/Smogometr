package agh.ms.smogometr_1.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val agh_a0 = LatLng(50.064504, 19.923284)
    val agh_a0State = MarkerState(position = agh_a0)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(agh_a0,10f)
    }
    GoogleMap (
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState

    ){
        Marker(
            state = agh_a0State,
            title = "AGH A0"
        )
    }


}
@Preview(showSystemUi = true)
@Composable
fun MapScreenPreview() {
    MapScreen()
}