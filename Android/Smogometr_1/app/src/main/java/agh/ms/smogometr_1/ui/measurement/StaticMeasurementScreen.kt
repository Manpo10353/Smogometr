package agh.ms.smogometr_1.ui.measurement

import agh.ms.smogometr_1.R
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun StaticMeasurementScreen(
    modifier: Modifier = Modifier
){
    Button(onClick = { /*TODO*/ }) {
        Text(text = stringResource(R.string.rozpocznij_pomiar))

    }

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StaticMeasurementScreenPreview(){
    StaticMeasurementScreen()
}

