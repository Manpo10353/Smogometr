@file:OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)

package agh.ms.smogometr_1.ui.measurement

import agh.ms.smogometr_1.R
import agh.ms.smogometr_1.data.ConnectionState
import agh.ms.smogometr_1.data.measurement.PermissionUtils
import agh.ms.smogometr_1.data.sensors.Sensor
import agh.ms.smogometr_1.ui.theme.md_theme_light_primary
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MeasurementScreen() {
    val viewModel: MeasurementViewModel = hiltViewModel()//(factory = AppViewModelProvider.Factory)
    //val sensorDataStore: SensorDataStore = SensorDataStoreImpl(/*dataStore instance*/)
    //val locationLiveDataM by viewModel.locationLiveDataM.observeAsState()
    val buttonText by viewModel.buttonText.collectAsState()
    val permissionState =
        rememberMultiplePermissionsState(permissions = PermissionUtils.permissions)
    val lifecycleOwner = LocalLifecycleOwner.current
    val bleConnectionState = viewModel.connectionState
    val myLocation by viewModel.location.collectAsState()

    DisposableEffect(
        key1 = lifecycleOwner,
        effect = {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_START) {
                    permissionState.launchMultiplePermissionRequest()
                    if (permissionState.allPermissionsGranted && bleConnectionState == ConnectionState.Disconnected) {
                        viewModel.reconnect()
                    }
                }
                if (event == Lifecycle.Event.ON_STOP) {
                    if (bleConnectionState == ConnectionState.Connected) {
                        viewModel.disconnect()
                    }
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    )

    LaunchedEffect(key1 = permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            if (bleConnectionState == ConnectionState.Uninitialized) {
                viewModel.initializeConnection()
            }
        }
    }

    LaunchedEffect(myLocation) {
        myLocation?.let { newLocation ->
            // Wykonaj operacje w zależności od nowej lokalizacji
            // ...
        }
    }


    Column(
        modifier = Modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (bleConnectionState == ConnectionState.CurrentlyInitializing) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (viewModel.initializingMessage != null) {
                            Text(
                                text = viewModel.initializingMessage!!,
                                        textAlign = TextAlign.Center
                            )
                        }
                    }
                } else if (!permissionState.allPermissionsGranted) {
                    Text(
                        text = stringResource(R.string.ble_permission),
                        modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                        textAlign = TextAlign.Center
                    )
                } else if (viewModel.errorMessage != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = viewModel.errorMessage!!,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = {
                                if (permissionState.allPermissionsGranted) {
                                    viewModel.initializeConnection()
                                }
                            }
                        ) {
                            Text(
                                stringResource(R.string.try_again)
                            )
                        }
                    }
                } else if (bleConnectionState == ConnectionState.Connected) {
                    SensorItem(
                        sensor = Sensor(
                            "Smogometr",
                            "42:42",
                            true,
                            listOf("PM 2.5" to true,"PM 10" to true, "NOx" to true)
                        )
                    )
                } else if (bleConnectionState == ConnectionState.Disconnected) {
                    Button(
                        onClick = {
                            viewModel.initializeConnection()
                        }
                    ) {
                        Text(stringResource(R.string.connect_again))
                    }
                }
            }
        }
        Spacer(
            modifier = Modifier
                .weight(1f)
        )
        Card {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SliderDistance(
                    viewModel.sliderPosition,
                    onSliderValueChange = { newSliderPosition ->
                        viewModel.updateSliderPosition(
                            newSliderPosition
                        )
                    }
                )
                Button(
                    onClick = {
                        viewModel.toggleMeasurement()
                              },
                    modifier = Modifier
                        .size(200.dp)
                        .clip(CircleShape)
                ) {
                    Text(
                        text = buttonText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                            .padding(8.dp)
                    )
                }

            }
        }
        Text(text = myLocation.toString())
        Spacer(
            modifier = Modifier
                .weight(2f)
        )
    }
}

@Composable
fun SliderDistance(
    sliderPosition: Float,
    onSliderValueChange: (Float) -> Unit
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.distance_betwen_sensors),
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.padding_medium)),
            textAlign = TextAlign.Center
        )
        Card(
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
                .align(Alignment.CenterHorizontally),
            colors = CardDefaults.cardColors(
                containerColor = md_theme_light_primary),


            ){
            Text(
                text = sliderPosition.toInt().toString(),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
                    .padding(8.dp)
            )
        }
        Slider(
            value = sliderPosition,
            onValueChange = { onSliderValueChange(it) },
            valueRange = 5f..100f,
            steps = 18,

        )

    }
}

@Composable
fun SensorComponents(components: List<Pair<String, Boolean>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium),
                bottom = dimensionResource(R.dimen.padding_medium)
            )
    ) {
        val chunkedComponents = components.chunked(2)
        for (columnComponents in chunkedComponents.chunked(2)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                for (rowComponents in columnComponents) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        for ((type, isInstalled) in rowComponents) {
                            SensorComponent(type = type, isInstalled = isInstalled)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SensorItem(
    sensor: Sensor,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SensorImage()
                SensorId(sensor.name)
                Spacer(modifier = Modifier.weight(1f))
                SensorBluetooth(sensor.isConnected)
                Spacer(modifier = Modifier.weight(1f))
                SensorItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded }
                )
            }
            if (expanded) {
                SensorComponents(sensor.components)
            }
        }
    }
}

@Composable
fun SensorComponent(
    type: String,
    isInstalled: Boolean
) {
    val installed = if (isInstalled) {
        Icons.Default.CheckCircle
    } else {
        Icons.Default.Cancel
    }
    Row(
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_medium)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = type)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = installed,
            contentDescription = if (isInstalled) "$type sensor Connected" else "$type sensor Disconnected",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun SensorItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null
        )
    }
}

@Composable
fun SensorId(
    sensorName: String?,
) {
    Text(text = sensorName ?: "bez nazwy")
}

@Composable
fun SensorBluetooth(isConnected: Boolean) {
    val bluetoothIcon = if (isConnected) {
        Icons.Default.BluetoothConnected
    } else {
        Icons.Default.BluetoothDisabled
    }

    Icon(
        imageVector = bluetoothIcon,
        contentDescription = if (isConnected) "Bluetooth Connected" else "Bluetooth Disconnected",
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SensorImage(
    modifier: Modifier = Modifier
) {
    Image(
        modifier = modifier
            .size(dimensionResource(R.dimen.image_size))
            .padding(dimensionResource(R.dimen.padding_small))
            .clip(MaterialTheme.shapes.small),
        painter = painterResource(id = R.drawable.sensoricon),
        contentScale = ContentScale.Crop,
        contentDescription = null
    )
}


//@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MeasurementScreenPreview() {
    MeasurementScreen()
}@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SlidePreview() {
    SliderDistance(5f, {} )
}

