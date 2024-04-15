package com.aquaguardian.projectaquaguard.ui.components

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.BuildConfig.Companion.WEATHER_API_AUTH_KEY
import com.aquaguardian.projectaquaguard.core.data.WeatherResponse
import com.aquaguardian.projectaquaguard.core.managers.requestLocationAddress
import com.aquaguardian.projectaquaguard.core.vms.MainVMService
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

const val TAG = "WeatherWidget"
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherWidget(
    viewModel: MainVMService,
    modifier: Modifier = Modifier,
) {
    val weatherData by viewModel.weatherData.collectAsState()
    val isWeatherServiceLoading by viewModel.isWeatherServiceLoading.collectAsState()
    val weatherServiceError by viewModel.weatherServiceError.collectAsState()

    val executeWeatherRefresh = remember { mutableStateOf(false) }
    val showErrorDetailsDialog = remember { mutableStateOf(false) }

    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    if (weatherData == null) {
        if(weatherServiceError.isEmpty()){
            viewModel.writeWeatherServiceError("")
            viewModel.updateWeatherServiceLoading(true)
            reloadWeatherData(viewModel, null)
            executeWeatherRefresh.value = false
        }
    }

    when {
        executeWeatherRefresh.value -> {
            viewModel.writeWeatherServiceError("")
            viewModel.updateWeatherServiceLoading(true)
            reloadWeatherData(viewModel, weatherData)
            executeWeatherRefresh.value = false
        }
    }

    when {
        showErrorDetailsDialog.value -> {
            ShowErrorInformation(showErrorDetailsDialog, weatherServiceError)
        }
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 160.dp, min = 150.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        if (weatherServiceError.length > 1) {

            // Error NO_LOC_ACCESS means that the Location or GPS Service is disabled.
            when (weatherServiceError) {
                "LOCATION_NOT_AVAILABLE" -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = "Location Icon",
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Location is Disabled. Please Enable it",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = productsans_family,
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Button(onClick = {
                            viewModel.clearWeatherData()
                            executeWeatherRefresh.value = true
                        }, modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = "Refresh",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = productsans_family,
                            )
                        }
                    }
                }
                "NO_PERMISSION" -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_location),
                            contentDescription = "Location Icon",
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Location Permission not Granted. Please Enable it",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = productsans_family,
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Button(onClick = {
                            if(!locationPermissionState.hasPermission){
                                locationPermissionState.launchPermissionRequest()
                            }
                        }, modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = "Grant",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal,
                                fontFamily = productsans_family,
                            )
                        }
                    }
                }
                else -> {
                    // Error is Detected. Service Didn't Worked
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sad_icon),
                            contentDescription = "Sad Icon",
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                                .padding(bottom = 8.dp)
                        )
                        Text(
                            text = "An Error Occurred Fetching Current Weather",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = productsans_family,
                            modifier = Modifier
                                .padding(bottom = 8.dp),
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(onClick = {
                                showErrorDetailsDialog.value = true
                            }) {
                                Text(
                                    text = "View  Details",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = productsans_family,
                                )
                            }
                            Button(onClick = {
                                viewModel.clearWeatherData()
                                executeWeatherRefresh.value = true
                            }, modifier = Modifier.padding(start = 8.dp)) {
                                Text(
                                    text = "Refresh",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    fontFamily = productsans_family,
                                )
                            }
                        }
                    }
                }
            }
        } else {
            if(isWeatherServiceLoading){

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        modifier = Modifier
                            .height(50.dp)
                            .width(50.dp)
                    )
                }
            } else {

                if(weatherData != null){

                    // Show Data Because Service Worked.
                    weatherData.let { response ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_weather_sunny),
                                    contentDescription = "Icon Weather Sunny",
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp)
                                )
                                if (response != null) {
                                    Text(
                                        text = response.address,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = productsans_family,
                                        fontSize = 20.sp,
                                        modifier = Modifier.fillMaxWidth(1f),
                                        textAlign = TextAlign.End
                                    )
                                } else {
                                    Text(
                                        text = "-",
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = productsans_family,
                                        fontSize = 20.sp,
                                        modifier = Modifier.fillMaxWidth(1f),
                                        textAlign = TextAlign.End
                                    )
                                }
                            }
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                if (response != null) {
                                    Text(
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = productsans_family,
                                        fontSize = 36.sp,
                                        text = "${response.days[0].temp}° C"
                                    )
                                } else {
                                    Text(
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = productsans_family,
                                        fontSize = 36.sp,
                                        text = "-° C"
                                    )
                                }
                            }
                            Spacer(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                            ) {
                                if (response != null) {
                                    Text(
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = productsans_family,
                                        fontSize = 14.sp,
                                        text = "${response.days[1].temp.toInt()}° C"
                                    )
                                } else {
                                    Text(
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = productsans_family,
                                        fontSize = 14.sp,
                                        text = "-° C"
                                    )
                                }
                                Spacer(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(16.dp)
                                )
                                if (response != null) {
                                    Text(
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = productsans_family,
                                        fontSize = 14.sp,
                                        text = "${response.days[2].temp.toInt()}° C"
                                    )
                                } else {
                                    Text(
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                        fontWeight = FontWeight.Normal,
                                        fontFamily = productsans_family,
                                        fontSize = 14.sp,
                                        text = "-° C"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun reloadWeatherData(viewModel: MainVMService, weatherData: WeatherResponse?, forcedRefresh: Boolean = false) {
//    requestLocationAddress(
//        autoRequestPermission = true,
//        onLocated = {
//
//
//        },
//        onError = {
//            viewModel.writeWeatherServiceError("$it")
//            viewModel.updateWeatherServiceLoading(false)
//        },
//    )

    if (weatherData == null || forcedRefresh) {

        // if (it != null) {

//            val baranggay = it.subLocality ?: ""
//            val city = it.locality ?: ""
           // if (baranggay == "" && city == "") {

//                viewModel.writeWeatherServiceError("Address generated from class android.location.Geocoder didn't have the required data. Try to enable Location GPS and set App Permission to access Precise Location")
//                viewModel.updateWeatherServiceLoading(false)
            //} else {

                viewModel.fetchWeatherData(
                    WEATHER_API_AUTH_KEY,
                    "Puerto Princesa Palawan"
                )
//            }
       // } else {
//
//            viewModel.writeWeatherServiceError("LOCATION_NOT_AVAILABLE")
//            viewModel.updateWeatherServiceLoading(false)
//
//        }
    }
}

@Composable
private fun ShowErrorInformation(
    openErrorDialog: MutableState<Boolean>,
    weatherServiceError: String
) {
    AlertDialog(onDismissRequest = {

    }, confirmButton = {
        TextButton(onClick = {
            openErrorDialog.value = !openErrorDialog.value
        }) {
            Text(
                text = "Close", color = MaterialTheme.colorScheme.primary
            )
        }
    },
        dismissButton = {
            TextButton(onClick = {
                // TODO: Implement Developer Reporting Function
            }) {
                Text(
                    text = "Report", color = MaterialTheme.colorScheme.primary
                )
            }
        },
        title = {
            Text(
                text = "An Error Occurred",
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal
            )
        },
        text = {
            Text(
                text = "The following is the information about the error: $weatherServiceError",
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal,
            )
        },
        icon = {
            Icon(imageVector = Icons.Outlined.Info, contentDescription = "Error Info Icon")
        })
}