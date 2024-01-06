package agh.ms.smogometr_1

import agh.ms.smogometr_1.ui.AboutScreen
import agh.ms.smogometr_1.ui.AppViewModel
import agh.ms.smogometr_1.ui.measurement.DynamicMeasurementScreen
import agh.ms.smogometr_1.ui.map.MapScreen
import agh.ms.smogometr_1.ui.measurement.MeasurementScreen
import agh.ms.smogometr_1.ui.SensorsScreen
import agh.ms.smogometr_1.ui.SettingsScreen
import agh.ms.smogometr_1.ui.StartScreen
import agh.ms.smogometr_1.ui.measurement.StaticMeasurementScreen
import agh.ms.smogometr_1.ui.navigation.SmogometrNavHost
import agh.ms.smogometr_1.ui.theme.Smogometr_1Theme
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


enum class SmogometrScreen(@StringRes val title: Int){
    StartScreen(title = R.string.app_name),
    MapScreen(title = R.string.map),
    SensorsScreen(title = R.string.sensors),
    SettingsScreen(title = R.string.settings),
    AboutScreen(title = R.string.about),
    MeasurementScreen(title = R.string.measurmement),
    StaticMeasurementScreen(title = R.string.static_measurement),
    DynamicMeasurementScreen(title = R.string.dynamic_measurement)


}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    currentScreen: SmogometrScreen,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id = currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Smogometr(
    viewModel: AppViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = SmogometrScreen.valueOf(
        backStackEntry?.destination?.route ?: SmogometrScreen.StartScreen.name
    )

    Scaffold(
        topBar = {
            TopBar(
                currentScreen = currentScreen,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = SmogometrScreen.StartScreen.name,

                ) {
                composable(route = SmogometrScreen.StartScreen.name) {
                    StartScreen(
                        onSensorsButtonClicked = { navController.navigate(SmogometrScreen.SensorsScreen.name) },
                        onMapButtonClicked = { navController.navigate(SmogometrScreen.MapScreen.name) },
                        onSettingsButtonClicked = { navController.navigate(SmogometrScreen.SettingsScreen.name) },
                        onAboutButtonClicked = { navController.navigate(SmogometrScreen.AboutScreen.name) },
                        onMeasurementButtonClicked = { navController.navigate(SmogometrScreen.MeasurementScreen.name) },

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

    }
}

