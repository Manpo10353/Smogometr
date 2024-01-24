package agh.ms.smogometr_1.ui

import agh.ms.smogometr_1.R
import agh.ms.smogometr_1.ui.theme.AppTheme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StartScreen(
    onMapButtonClicked: () -> Unit,
    onMeasurementButtonClicked: () -> Unit,
){
    Column (
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Button(
            onClick = {onMeasurementButtonClicked()},
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        ) {
            Text(text = stringResource(R.string.measurmement))
        }
        Spacer(
            modifier = Modifier
            .weight(1f)
        )
        Button(
            onClick = {onMapButtonClicked()},
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        ) {
            Text(text = stringResource(R.string.map))
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun StartScreenPreview(){
    AppTheme {
        StartScreen(
            onMapButtonClicked = {},
            onMeasurementButtonClicked = {},
        )
    }
}