package agh.ms.smogometr_1

import agh.ms.smogometr_1.data.AppDataContainer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import agh.ms.smogometr_1.ui.theme.Smogometr_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Smogometr_1Theme {
                Smogometr()
            }
        }
    }
}
