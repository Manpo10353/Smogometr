package agh.ms.smogometr_1.ui

import agh.ms.smogometr_1.R
import agh.ms.smogometr_1.ui.theme.Smogometr_1Theme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StartScreen(
    modifier: Modifier = Modifier,
    onSensorsButtonClicked: () -> Unit,
    onMapButtonClicked: () -> Unit,
    onMeasurementButtonClicked: () -> Unit,
    onSettingsButtonClicked: () -> Unit,
    onAboutButtonClicked: () -> Unit,
){
    Column (
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Button(onClick = {onMeasurementButtonClicked()}) {
            Text(text = stringResource(R.string.measurmement))
        }
        Button(onClick = {onMapButtonClicked()}) {
            Text(text = stringResource(R.string.map))
        }
        Button(onClick = {onSensorsButtonClicked()}) {
            Text(text = stringResource(R.string.sensors))
        }
        Button(onClick = {onSettingsButtonClicked()}) {
            Text(text = stringResource(R.string.settings))
        }
        Button(onClick = {onAboutButtonClicked()}) {
            Text(text = stringResource(R.string.about))
        }
        Spacer(modifier=Modifier.weight(1f))
    }
}

@Preview(showSystemUi = true)
@Composable
fun StartScreenPreview(){
    Smogometr_1Theme {
        StartScreen(
            onSensorsButtonClicked = {},
            onMapButtonClicked = {},
            onMeasurementButtonClicked = {},
            onSettingsButtonClicked = {},
            onAboutButtonClicked = {},
        )
    }
}