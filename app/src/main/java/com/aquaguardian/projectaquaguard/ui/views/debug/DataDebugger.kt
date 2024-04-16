package com.aquaguardian.projectaquaguard.ui.views.debug

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.core.vms.MainVMService
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Date
import java.util.Locale
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataDebugger(nav: NavHostController, mainVMService: MainVMService) {
    val ctx = LocalContext.current
    val scrollState = rememberScrollState()

    var isAllInputDataOk by remember { mutableStateOf(false) }

    val dataTypes = listOf(
        "pH",
        "Total Soluble Solids",
        "Total Dissolved Solids",
        "Total Soluble",
        "Water Temperature",
        "Water Level"
    )

    val dataTypeKeys = listOf(
        "phData",
        "tss",
        "tds",
        "ts",
        "wtemp",
        "wtlvl"
    )

    val selectedSensorDataKey = remember { mutableStateOf(dataTypeKeys[0]) }

    var sensorDataPickerExpanded by remember { mutableStateOf(false) }
    var selectedSensor by remember { mutableStateOf(dataTypes[0]) }

    val minValue = remember { mutableStateOf("") }
    val maxValue = remember { mutableStateOf("") }

    val shape = if (sensorDataPickerExpanded) RoundedCornerShape(8.dp).copy(
        bottomEnd = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
    else RoundedCornerShape(8.dp)

    val randomDataPatternTrends = listOf(
        "Rising",
        "Falling",
        "Consistent (Random Values Does Not take Effect)",
    )

    val randomDataPatternTrendKeys = listOf(
        "r",
        "f",
        "c"
    )

    var isPickedDataTrendExpanded by remember { mutableStateOf(false) }
    var pickedDataTrendType by remember { mutableStateOf(randomDataPatternTrends[0]) }
    var pickedDataTrendTypeKey by remember { mutableStateOf(randomDataPatternTrendKeys[0]) }

    val dataTrendPickerShape = if (sensorDataPickerExpanded) RoundedCornerShape(8.dp).copy(
        bottomEnd = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
    else RoundedCornerShape(8.dp)

    val dataCount = remember { mutableStateOf("") }

    val startTimeStamp = remember { mutableStateOf(getCurrentDateTimeFormatted()) }
    val intervalInMins = remember { mutableStateOf("10") }

    val dateTimeFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")
    val isStartTimestampNotProperFormat = remember { mutableStateOf(false) }

    val intervalStartMonth = remember { mutableStateOf("") }
    val intervalStartDay = remember { mutableStateOf("") }
    val intervalStartYear = remember { mutableStateOf("") }
    val intervalStartHour = remember { mutableStateOf("") }
    val intervalStartMinute = remember { mutableStateOf("") }
    val intervalStartSecond = remember { mutableStateOf("") }

    val dataMapWithTimestamp = mutableMapOf<String, Map<String, Double>>()

    val initialValue = remember { mutableStateOf("") }

    val isPushingMode = remember { mutableStateOf(false) }

    val lastComputedTimeStamp = remember { mutableStateOf("") }
    val lastDataValue = remember { mutableStateOf("") }

    val tankIDSelected = remember { mutableStateOf("t1") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp),
        animateChanges = true
    ) {
        val (topButtonGroup, titleText, dataPicker, label1, randomDataRangeInputs, label2, dataOptionsInput, label3, timestampOptionInputs, label4, initialParamInputs) = createRefs()

        Row(modifier = Modifier
            .constrainAs(topButtonGroup) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
            .padding(start = 8.dp, top = 8.dp)) {
            IconButton(onClick = {
                nav.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Arrow Forward Icon"
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        if (isPushingMode.value) {

                        } else {

                            if (!isAllInputDataOk) {
                                Toast.makeText(ctx, "Error", Toast.LENGTH_SHORT).show()
                            } else {

                                // Generate dummy values based on the maxEntries variable
                                for (i in 1..dataCount.value.toInt()) {
                                    // Check for Time and Date computation
                                    if (i == 1) {
                                        lastComputedTimeStamp.value = startTimeStamp.value
                                    } else {
                                        lastComputedTimeStamp.value = addMinutesToDateTime(
                                            lastComputedTimeStamp.value,
                                            intervalInMins.value.toInt()
                                        )
                                    }

                                    // Generate a dummy value
                                    if (i == 1) {
                                        lastDataValue.value = initialValue.value
                                    } else {
                                        val offsetToAdd = Random.nextDouble(
                                            minValue.value.toDouble(),
                                            maxValue.value.toDouble()
                                        )
                                        val calculation =
                                            lastDataValue.value.toDouble() + offsetToAdd
                                        lastDataValue.value = calculation.toString()
                                    }

                                    val sensorDataMap =
                                        mutableMapOf(selectedSensorDataKey.value to lastDataValue.value.toDouble())
                                    dataMapWithTimestamp[lastComputedTimeStamp.value] =
                                        sensorDataMap
                                }

                                Toast.makeText(ctx, "Generate Finished!", Toast.LENGTH_SHORT).show()
                                Log.e("DataDebugger", dataMapWithTimestamp.toString())

                            }
                        }

                    },
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    enabled = isAllInputDataOk
                ) {
                    Text(
                        text = when (isPushingMode.value) {
                            true -> "Push"
                            false -> "Generate"
                        },
                        fontSize = 11.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Normal
                    )

                }
            }
        }

        Text(fontSize = 50.sp,
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal,
            text = "Data Debug",
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(topButtonGroup.bottom)
                    start.linkTo(parent.start)
                }
                .padding(start = 24.dp, top = 32.dp))

        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 16.dp, end = 16.dp)
            .constrainAs(dataPicker) {
                top.linkTo(titleText.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {

            ExposedDropdownMenuBox(
                expanded = sensorDataPickerExpanded,
                onExpandedChange = { sensorDataPickerExpanded = !sensorDataPickerExpanded },
                modifier = Modifier.fillMaxWidth(),
            ) {

                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    textStyle = TextStyle.Default.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    ),
                    value = selectedSensor,
                    onValueChange = {},
                    label = {
                        Text(
                            "Sensor Type to Modify Data",
                            fontWeight = FontWeight.Bold,
                            fontFamily = productsans_family,
                            fontSize = 11.sp
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = sensorDataPickerExpanded) },
                    shape = shape,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent
                    )
                )
                DropdownMenu(
                    expanded = sensorDataPickerExpanded,
                    onDismissRequest = { sensorDataPickerExpanded = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    dataTypes.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                selectedSensor = selectionOption
                                when (selectionOption) {
                                    dataTypes[0] -> {
                                        selectedSensorDataKey.value = dataTypeKeys[0]
                                    }

                                    dataTypes[1] -> {
                                        selectedSensorDataKey.value = dataTypeKeys[1]
                                    }

                                    dataTypes[2] -> {
                                        selectedSensorDataKey.value = dataTypeKeys[2]
                                    }

                                    dataTypes[3] -> {
                                        selectedSensorDataKey.value = dataTypeKeys[3]
                                    }

                                    dataTypes[4] -> {
                                        selectedSensorDataKey.value = dataTypeKeys[4]
                                    }

                                    dataTypes[5] -> {
                                        selectedSensorDataKey.value = dataTypeKeys[4]
                                    }
                                }
                                sensorDataPickerExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(label4) {
                    top.linkTo(dataPicker.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 16.dp, bottom = 4.dp, start = 16.dp)
        ) {
            Text(
                text = "INITIAL DATA",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp)
                .constrainAs(initialParamInputs) {
                    top.linkTo(label4.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            TextField(
                value = initialValue.value,
                onValueChange = { v ->
                    initialValue.value = v
                    isAllInputDataOk = validateData(
                        minValue.value,
                        maxValue.value,
                        dataCount.value,
                        initialValue.value,
                        intervalInMins.value,
                        startTimeStamp.value,
                        isStartTimestampNotProperFormat.value
                    )
                },
                label = {
                    Text(
                        "Initial Value",
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 11.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Decimal
                )
            )

            Button(
                onClick = {
                    Toast.makeText(ctx, "No Previous Data to Get values from!", Toast.LENGTH_SHORT)
                        .show()
                },
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp, start = 8.dp)
                    .weight(0.57f),
            ) {
                Text(
                    text = "Get Values",
                    fontSize = 11.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal
                )

            }

        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(label1) {
                    top.linkTo(initialParamInputs.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 16.dp, bottom = 4.dp, start = 16.dp)
        ) {
            Text(
                text = "RANDOM DATA RANGE",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp)
                .constrainAs(randomDataRangeInputs) {
                    top.linkTo(label1.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            TextField(
                value = minValue.value,
                onValueChange = { v ->
                    minValue.value = v
                    isAllInputDataOk = validateData(
                        minValue.value,
                        maxValue.value,
                        dataCount.value,
                        initialValue.value,
                        intervalInMins.value,
                        startTimeStamp.value,
                        isStartTimestampNotProperFormat.value
                    )
                },
                label = {
                    Text(
                        "Min Value",
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 11.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Decimal
                )
            )
            TextField(
                value = maxValue.value,
                onValueChange = {
                    maxValue.value = it
                    isAllInputDataOk = validateData(
                        minValue.value,
                        maxValue.value,
                        dataCount.value,
                        initialValue.value,
                        intervalInMins.value,
                        startTimeStamp.value,
                        isStartTimestampNotProperFormat.value
                    )
                },
                label = {
                    Text(
                        "Max Value",
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 11.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Decimal
                ),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(label2) {
                    top.linkTo(randomDataRangeInputs.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 16.dp, bottom = 4.dp, start = 16.dp)
        ) {
            Text(
                text = "DATA OPTIONS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp)
                .constrainAs(dataOptionsInput) {
                    top.linkTo(label2.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            TextField(
                value = dataCount.value,
                onValueChange = { v ->
                    dataCount.value = v
                    isAllInputDataOk = validateData(
                        minValue.value,
                        maxValue.value,
                        dataCount.value,
                        initialValue.value,
                        intervalInMins.value,
                        startTimeStamp.value,
                        isStartTimestampNotProperFormat.value
                    )
                },
                label = {
                    Text(
                        "Max Data Count",
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 11.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            )
            ExposedDropdownMenuBox(
                expanded = isPickedDataTrendExpanded,
                onExpandedChange = { isPickedDataTrendExpanded = !isPickedDataTrendExpanded },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
            ) {

                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    textStyle = TextStyle.Default.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    ),
                    value = pickedDataTrendType,
                    onValueChange = {},
                    label = {
                        Text(
                            "Random Data Trend Type",
                            fontWeight = FontWeight.Bold,
                            fontFamily = productsans_family,
                            fontSize = 11.sp
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isPickedDataTrendExpanded) },
                    shape = dataTrendPickerShape,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = Transparent,
                        unfocusedIndicatorColor = Transparent
                    )
                )
                DropdownMenu(
                    expanded = isPickedDataTrendExpanded,
                    onDismissRequest = { isPickedDataTrendExpanded = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    randomDataPatternTrends.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                pickedDataTrendType = selectionOption
                                when (selectionOption) {
                                    randomDataPatternTrends[0] -> {
                                        pickedDataTrendTypeKey = randomDataPatternTrendKeys[0]
                                    }

                                    randomDataPatternTrends[1] -> {
                                        pickedDataTrendTypeKey = randomDataPatternTrendKeys[1]
                                    }

                                    randomDataPatternTrends[2] -> {
                                        pickedDataTrendTypeKey = randomDataPatternTrendKeys[2]
                                    }
                                }
                                isPickedDataTrendExpanded = false
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(label3) {
                    top.linkTo(dataOptionsInput.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 16.dp, bottom = 4.dp, start = 16.dp)
        ) {
            Text(
                text = "TIMESTAMP OPTIONS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp)
                .constrainAs(timestampOptionInputs) {
                    top.linkTo(label3.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                TextField(
                    isError = isStartTimestampNotProperFormat.value,
                    value = startTimeStamp.value,
                    onValueChange = { v ->
                        try {
                            val dateTime = LocalDateTime.parse(v, dateTimeFormat)

                            intervalStartMonth.value = dateTime.monthValue.toString()
                            intervalStartDay.value = dateTime.dayOfMonth.toString()
                            intervalStartYear.value = dateTime.year.toString()
                            intervalStartHour.value = dateTime.hour.toString()
                            intervalStartMinute.value = dateTime.minute.toString()
                            intervalStartSecond.value = dateTime.second.toString()

                            isStartTimestampNotProperFormat.value = false
                        } catch (_: DateTimeParseException) {
                            isStartTimestampNotProperFormat.value = true
                        }
                        startTimeStamp.value = v
                        isAllInputDataOk =
                            validateData(
                                minValue.value,
                                maxValue.value,
                                dataCount.value,
                                initialValue.value,
                                intervalInMins.value,
                                startTimeStamp.value,
                                isStartTimestampNotProperFormat.value
                            )
                    },
                    label = {
                        Text(
                            "Start Timestamp: MM/dd/yyyy HH:mm:ss",
                            fontWeight = FontWeight.Bold,
                            fontFamily = productsans_family,
                            fontSize = 11.sp
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text,
                        autoCorrect = false,
                        capitalization = KeyboardCapitalization.None,


                        )
                )
                if (isStartTimestampNotProperFormat.value) {
                    Text(
                        "Error: Please format timestamp properly!",
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            TextField(
                value = intervalInMins.value,
                onValueChange = { v ->
                    intervalInMins.value = v
                    isAllInputDataOk = validateData(
                        minValue.value,
                        maxValue.value,
                        dataCount.value,
                        initialValue.value,
                        intervalInMins.value,
                        startTimeStamp.value,
                        isStartTimestampNotProperFormat.value
                    )
                },
                label = {
                    Text(
                        "Interval: (In Minutes)",
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 11.sp
                    )
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                )
            )
        }
    }


}

fun validateData(
    minValue: String,
    maxValue: String,
    dataCount: String,
    initialValue: String,
    intervalInMins: String,
    startTimeStamp: String,
    isStartTimestampNotProperFormat: Boolean
): Boolean {
    if (maxValue.isNotEmpty() && minValue.isNotEmpty() && dataCount.isNotEmpty() && initialValue.isNotEmpty() && intervalInMins.isNotEmpty() && startTimeStamp.isNotEmpty()) {
        if (minValue.toDouble() > maxValue.toDouble()) {
            return false
        } else {
            if (!isStartTimestampNotProperFormat) {
                return true
            } else {
                return false
            }
        }
    } else {
        return false
    }
}


fun getCurrentDateTimeFormatted(): String {
    val currentDate = Date()
    val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())
    return dateFormat.format(currentDate)
}

fun addMinutesToDateTime(dateTimeString: String, minutesToAdd: Int): String {
    val dateTimeFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss")
    val dateTime = LocalDateTime.parse(dateTimeString, dateTimeFormatter)
    val updatedDateTime = dateTime.plusMinutes(minutesToAdd.toLong())
    return updatedDateTime.format(dateTimeFormatter)
}

fun generateListOfData() {
}