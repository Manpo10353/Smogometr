package agh.ms.smogometr_1

import agh.ms.smogometr_1.ui.map.MapScreen
import agh.ms.smogometr_1.ui.measurement.MeasurementScreen
import agh.ms.smogometr_1.ui.StartScreen
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


enum class SmogometrScreen(@StringRes val title: Int){
    StartScreen(title = R.string.app_name),
    MapScreen(title = R.string.map),
    MeasurementScreen(title = R.string.measurmement),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    currentScreen: SmogometrScreen,
    modifier: Modifier = Modifier
) {
    //var connected by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                   // .padding(horizontal = 4.dp)
            ) {
                Text(
                    stringResource(id = currentScreen.title),
                    modifier = Modifier.weight(1f)
                )
                }
        },
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


@Composable
fun Smogometr(
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
                        onMapButtonClicked = { navController.navigate(SmogometrScreen.MapScreen.name) },
                        onMeasurementButtonClicked = { navController.navigate(SmogometrScreen.MeasurementScreen.name) },
                        )
                }
                composable(route = SmogometrScreen.MapScreen.name) {
                    MapScreen()
                }
                composable(route = SmogometrScreen.MeasurementScreen.name) {
                    MeasurementScreen()
                }
            }

    }
}
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview(){
    TopBar(
        canNavigateBack = true,
        navigateUp = {},
        currentScreen = SmogometrScreen.StartScreen
        )
}
