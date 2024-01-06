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
    var sensorState by remember { mutableStateOf(SensorState()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SensorCheckbox("PPM2.5", sensorState.ppm25Checked)//, {sensorState.ppm25Checked = it })/*
        SensorCheckbox("PPM10", sensorState.ppm10Checked)//, {
        //      viewModel.updateSensorState(sensorState.copy(ppm10Checked = it))
        // })
        SensorCheckbox("NOx", sensorState.noxChecked)//, {
        //   viewModel.updateSensorState(sensorState.copy(noxChecked = it))
        // })
        SensorCheckbox("SOx", sensorState.soxChecked)//, {
        //     viewModel.updateSensorState(sensorState.copy(soxChecked = it))
        //}
//*/
        SliderDistance()
        Button(
            onClick = {viewModel.addRandomMeasurement() },
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        ) {
            Text(text = stringResource(R.string.start_meausure))
        }
    }
}

@Composable
fun SliderDistance() {
    var sliderPosition by remember { mutableFloatStateOf(0f) }
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
            onValueChange = { sliderPosition = it },
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
    isChecked: Boolean,
    //onCheckedChange: (Boolean) -> Unit ,
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
            checked = isChecked,
            onCheckedChange = {}
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

