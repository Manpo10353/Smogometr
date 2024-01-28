package agh.ms.smogometr_1.ui.map

import agh.ms.smogometr_1.R
import agh.ms.smogometr_1.ui.theme.md_theme_light_background
import agh.ms.smogometr_1.ui.theme.md_theme_light_primary
import agh.ms.smogometr_1.ui.theme.md_theme_light_tertiary
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun MapScreen(

) {
    val viewModel: MapViewModel = hiltViewModel()

    val myLocation by viewModel.location.collectAsState()


    //poprawić, przenieść do viewModel, tak aby tylko raz się ładowało
    val cameraPositionState = rememberCameraPositionState()
    myLocation?.let {
        cameraPositionState.position = CameraPosition.fromLatLngZoom(
            LatLng(it.latitude, it.longitude), 15f
        )
    }
    val activeButton by viewModel.activeButton.collectAsState()
    val measurementsUiState by viewModel.measurementsUiStates.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val startTime by viewModel.startTime.collectAsState()
    val endTime by viewModel.endTime.collectAsState()
    val formattedStartTime = startTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""
    val formattedEndTime = endTime?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""

    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var showStartTimePicker by remember {
        mutableStateOf(false)
    }
    var showEndTimePicker by remember {
        mutableStateOf(false)
    }



    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .background(color = Color(md_theme_light_background.toArgb()))
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            viewModel.changeActiveButton(ButtonType.PM25)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            if (activeButton == ButtonType.PM25) Color(md_theme_light_primary.toArgb()) else Color(
                                md_theme_light_tertiary.toArgb()
                            )
                        )
                    ) {
                        Text(stringResource(R.string.pm2_5))
                    }

                    Button(
                        onClick = {
                            viewModel.changeActiveButton(ButtonType.PM10)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            if (activeButton == ButtonType.PM10) Color(md_theme_light_primary.toArgb()) else Color(
                                md_theme_light_tertiary.toArgb()
                            )
                        )
                    ) {
                        Text(stringResource(R.string.pm10))
                    }

                    Button(
                        onClick = {
                            viewModel.changeActiveButton(ButtonType.NOx)
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            if (activeButton == ButtonType.NOx) Color(md_theme_light_primary.toArgb()) else Color(
                                md_theme_light_tertiary.toArgb()
                            )
                        )
                    ) {
                        Text(stringResource(R.string.nox))
                    }

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = { showStartTimePicker = true },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = formattedStartTime)
                    }
                    if (showStartTimePicker) {
                        SmogometrTimePickerDialog(
                            onTimeSelected = { viewModel.updateStartTime(it) },
                            onDismiss = { showStartTimePicker = false },
                            time = startTime!!
                        )
                    }
                    Button(
                        onClick = { showEndTimePicker = true },
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        Text(text = formattedEndTime)
                    }
                    if (showEndTimePicker) {
                        SmogometrTimePickerDialog(
                            onTimeSelected = { viewModel.updateEndTime(it) },
                            onDismiss = { showEndTimePicker = false },
                            time = endTime!!
                        )
                    }
                    Button(
                        onClick = { showDatePicker = true },
                        modifier = Modifier
                            .weight(2f)
                    ) {
                        Text(
                            text = selectedDate?.toString() ?: stringResource(R.string.select_date)
                        )
                    }
                    if (showDatePicker) {
                        SmogometrDatePickerDialog(
                            onDateSelected = { viewModel.updateSelectedDate(it) },
                            onDismiss = { showDatePicker = false }
                        )
                    }
                }
            }
        }

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        )
        {
            Log.d("db","${measurementsUiState.size}")
            measurementsUiState.forEach { measurementUiState ->
                Circle(
                    center = LatLng(measurementUiState.latitude,measurementUiState.longitude),
                    radius = 10.0,
                    fillColor = viewModel.calculateFillColor(measurementUiState),
                    strokeWidth = 5f,
                    strokeColor = viewModel.calculateStrokeColor(measurementUiState)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmogometrDatePickerDialog(
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    @OptIn(ExperimentalMaterial3Api::class)
    val datePickerState = remember {
        DatePickerState(
            initialDisplayMode = DisplayMode.Picker,
            yearRange = 2023..2024,
            initialDisplayedMonthMillis = null,
            initialSelectedDateMillis = null
        )
    }

    val selectedDate = datePickerState.selectedDateMillis?.let {
        Instant.ofEpochMilli(it)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    } ?: LocalDate.now()

    DatePickerDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                onDateSelected(selectedDate)
                onDismiss()
            }

            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmogometrTimePickerDialog(
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    time: LocalTime,
) {
    @OptIn(ExperimentalMaterial3Api::class)
    val timePickerState = remember {
        TimePickerState(
            is24Hour = true,
            initialHour = time.hour,
            initialMinute = time.minute,
        )
    }
    val selectedTime = LocalTime.of(
        timePickerState.hour,
        timePickerState.minute
    )

    Dialog(
        onDismissRequest = { onDismiss() }
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(id = R.dimen.padding_small)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.choose_time),
                    style = TextStyle(
                        fontSize = 28.sp
                    )
                )
                TimePicker(
                    state = timePickerState
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Spacer(
                        modifier = Modifier
                            .weight(6f)
                    )
                    Button(
                        onClick = { onDismiss() }
                    ) {
                        Text(text = stringResource(R.string.cancel))
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                    )
                    Button(
                        onClick = {
                            onTimeSelected(selectedTime)
                            onDismiss()
                        }
                    ) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        }
    }


}


@Preview(showSystemUi = true)
@Composable
fun MapScreenPreview() {
    //MapScreen(measurementRepository)
}

