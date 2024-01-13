package agh.ms.smogometr_1.ui.measurement

import agh.ms.smogometr_1.R
import agh.ms.smogometr_1.ui.AppViewModelProvider
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun DynamicMeasurementScreen(
    modifier: Modifier = Modifier,

) {
    val viewModel: DynamicMeasurementViewModel = viewModel(factory = AppViewModelProvider.Factory)
    //val sensorDataStore: SensorDataStore = SensorDataStoreImpl(/*dataStore instance*/)
    val locationLiveDataM by viewModel.locationLiveDataM.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SensorCheckbox(
            "PPM2.5",
            viewModel.sensorState.ppm25Checked,
            { isChecked -> viewModel.updateSensorState(viewModel.sensorState.copy(ppm25Checked = isChecked))}
        )
        SensorCheckbox(
            "PPM10",
            viewModel.sensorState.ppm10Checked,
            { isChecked -> viewModel.updateSensorState(viewModel.sensorState.copy(ppm10Checked = isChecked))})
        SensorCheckbox(
            "NOx",
            viewModel.sensorState.noxChecked,
            { isChecked -> viewModel.updateSensorState(viewModel.sensorState.copy(noxChecked = isChecked))}
        )
        SensorCheckbox(
            "SOx",
            viewModel.sensorState.soxChecked,
            { isChecked -> viewModel.updateSensorState(viewModel.sensorState.copy(soxChecked = isChecked))}
        )

        SliderDistance(
            viewModel.sliderPosition,
            onSliderValueChange = { newSliderPosition -> viewModel.updateSliderPosition(newSliderPosition) }
        )
        Button(
            onClick = {viewModel.startMeasure() },
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        ) {
            Text(text = stringResource(R.string.start_meausure))


        }
        Text(text = "$locationLiveDataM")
    }
}

@Composable
fun SliderDistance(
    sliderPosition: Float,
    onSliderValueChange: (Float) -> Unit
) {

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.distance_betwen_sensors),
            modifier = Modifier
                .padding(top = 8.dp)
        )
        Slider(
            value = sliderPosition,
            onValueChange = { onSliderValueChange(it) },
            valueRange = 5f..100f,
            steps = 18
        )
        Text(
            text = sliderPosition.toInt().toString(),
            modifier = Modifier
                .padding(8.dp)
        )
    }
}
@Composable
fun SensorCheckbox(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit ,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.pomiar, text),
            modifier = Modifier
                .weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

    


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DynamicMeasurementScreenPreview(){
    DynamicMeasurementScreen(
        //startMeasure = {}
    )
}

