package agh.ms.smogometr_1.ui

import agh.ms.smogometr_1.R
import agh.ms.smogometr_1.data.Sensor
import agh.ms.smogometr_1.data.sensors
import agh.ms.smogometr_1.ui.theme.Smogometr_1Theme
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Battery2Bar
import androidx.compose.material.icons.filled.Battery3Bar
import androidx.compose.material.icons.filled.Battery4Bar
import androidx.compose.material.icons.filled.Battery5Bar
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.BatteryUnknown
import androidx.compose.material.icons.filled.BluetoothConnected
import androidx.compose.material.icons.filled.BluetoothDisabled
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle




@Composable
fun SensorsScreen(
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(sensors) { sensor ->
            SensorItem(
                sensor = sensor,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small))
            )
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
                Column {
                    SensorId(sensor.id)
                    Row {
                        SensorBluetooth(sensor.isConnected)
                        SensorBattery(sensor.batteryLevel)
                    }
                }
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
fun SensorComponent(
    type: String,
    isInstalled: Boolean
) {
    val installed = if (isInstalled) {
        Icons.Default.CheckCircle
    } else {
        Icons.Default.Cancel
    }
    Row (
        modifier = Modifier
            .padding(
                start = dimensionResource(id = R.dimen.padding_small),
                end = dimensionResource(id = R.dimen.padding_medium)
                )
    ){
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
    sensorId: Int,
) {
    Text(text = "Czujnik $sensorId")
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
fun SensorBattery(batteryLevel: Int) {
    val batteryIcon = when (batteryLevel) {
        in 91..100 -> Icons.Default.BatteryFull
        in 76..90 -> Icons.Default.Battery6Bar
        in 61..75 -> Icons.Default.Battery5Bar
        in 46..60 -> Icons.Default.Battery4Bar
        in 31..45 -> Icons.Default.Battery3Bar
        in 16..30 -> Icons.Default.Battery2Bar
        in 6..15 -> Icons.Default.Battery1Bar
        in 0..5 -> Icons.Default.BatteryAlert
        else -> Icons.Default.BatteryUnknown
    }
    Icon(
        imageVector = batteryIcon,
        contentDescription = "Battery Level: $batteryLevel%",
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

val testSensor = Sensor(
    0,
    55,
    true,
    listOf(
        "NOx" to true,
        "SOx" to false,
        "PM" to true,
        "Benzo(a)pyrene" to false
    )
)

@Preview(showBackground = false, showSystemUi = false)
@Composable
fun CardPreview() {
    Smogometr_1Theme {
        SensorItem(testSensor)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SensorsScreenPreview() {
    Smogometr_1Theme {
        SensorsScreen()
    }
}