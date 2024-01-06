package agh.ms.smogometr_1.ui.navigation

import agh.ms.smogometr_1.SmogometrScreen
import agh.ms.smogometr_1.ui.AboutScreen
import agh.ms.smogometr_1.ui.SensorsScreen
import agh.ms.smogometr_1.ui.SettingsScreen
import agh.ms.smogometr_1.ui.StartScreen
import agh.ms.smogometr_1.ui.map.MapScreen
import agh.ms.smogometr_1.ui.measurement.DynamicMeasurementScreen
import agh.ms.smogometr_1.ui.measurement.MeasurementScreen
import agh.ms.smogometr_1.ui.measurement.StaticMeasurementScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun SmogometrNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier){
    NavHost(
        navController = navController,
        startDestination = SmogometrScreen.StartScreen.name,
        modifier = modifier,

        ) {
        composable(route = SmogometrScreen.StartScreen.name) {
            StartScreen(
                onSensorsButtonClicked = {navController.navigate(SmogometrScreen.SensorsScreen.name)},
                onMapButtonClicked = {navController.navigate(SmogometrScreen.MapScreen.name)},
                onSettingsButtonClicked = {navController.navigate(SmogometrScreen.SettingsScreen.name)},
                onAboutButtonClicked = {navController.navigate(SmogometrScreen.AboutScreen.name)},
                onMeasurementButtonClicked = {navController.navigate(SmogometrScreen.MeasurementScreen.name)},

                )
        }
        composable(route = SmogometrScreen.SensorsScreen.name) {
            SensorsScreen()
        }
        composable(route = SmogometrScreen.AboutScreen.name) {
            AboutScreen()
        }
        composable(route = SmogometrScreen.SettingsScreen.name) {
            SettingsScreen()
        }
        composable(route = SmogometrScreen.MapScreen.name) {
            MapScreen()
        }
        composable(route = SmogometrScreen.MeasurementScreen.name) {
            MeasurementScreen(
                onDynamicMeasurementClicked = { navController.navigate(SmogometrScreen.DynamicMeasurementScreen.name) },
                onStaticMeasurementClicked = { navController.navigate(SmogometrScreen.StaticMeasurementScreen.name) }
            )
        }
        composable(route = SmogometrScreen.StaticMeasurementScreen.name) {
            StaticMeasurementScreen()
        }
        composable(route = SmogometrScreen.DynamicMeasurementScreen.name) {
            DynamicMeasurementScreen()
        }
    }
}