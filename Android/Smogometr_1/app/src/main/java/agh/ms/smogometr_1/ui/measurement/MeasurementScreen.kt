package agh.ms.smogometr_1.ui.measurement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MeasurementScreen(
    modifier: Modifier = Modifier,
    onStaticMeasurementClicked: () -> Unit,
    onDynamicMeasurementClicked: () -> Unit,
){
    Column(
        modifier = Modifier
        .fillMaxWidth(),

    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {onDynamicMeasurementClicked()}
            ) {
                Text("Pomiar dynamiczny")
            }
        }
        Box(
            modifier = Modifier
            .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ){
            Button(
                onClick = {onStaticMeasurementClicked()}
            ) {
                Text("Pomiar statyczny")
            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MeasurementScreenPreview(){
    MeasurementScreen(
        onStaticMeasurementClicked = {},
        onDynamicMeasurementClicked = {}
    )
}