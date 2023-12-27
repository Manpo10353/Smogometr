package agh.ms.smogometr_1.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AboutScreen(
    modifier: Modifier = Modifier
){
    Column (){
        Text(text = "O smogometrze ze zdjęciami")
    }
}

@Preview
@Composable
fun AboutScreenPreview(){
    AboutScreen()
}
