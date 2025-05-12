package com.aquaguardian.projectaquaguard.ui.components

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.BuildConfig.Companion.WEATHER_API_AUTH_KEY
import com.aquaguardian.projectaquaguard.core.data.WeatherResponse
import com.aquaguardian.projectaquaguard.core.vms.MainVMService
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val TAG = "WeatherWidget"

// Non-Composable function for performing the weather data refresh
// This is separated from UI logic to avoid Composable invocation errors
fun performWeatherDataRefresh(
    viewModel: MainVMService,
    location: String = "Puerto Princesa Palawan"
) {
    viewModel.fetchWeatherData(
        WEATHER_API_AUTH_KEY,
        location
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherWidget(
    viewModel: MainVMService,
    modifier: Modifier = Modifier,
) {
    val weatherData by viewModel.weatherData.collectAsState()
    val isWeatherServiceLoading by viewModel.isWeatherServiceLoading.collectAsState()
    val weatherServiceError by viewModel.weatherServiceError.collectAsState()

    val context = LocalContext.current
    val executeWeatherRefresh = remember { mutableStateOf(false) }
    val showErrorDetailsDialog = remember { mutableStateOf(false) }

    // Animation properties
    val infiniteTransition = rememberInfiniteTransition(label = "weather animation")
    val iconScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "weather icon animation"
    )

    // Permission handling
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )

    // Initial data load
    LaunchedEffect(Unit) {
        if (weatherData == null && weatherServiceError.isEmpty()) {
            viewModel.writeWeatherServiceError("")
            viewModel.updateWeatherServiceLoading(true)
            performWeatherDataRefresh(viewModel)
        }
    }

    // Handle refresh request
    LaunchedEffect(executeWeatherRefresh.value) {
        if (executeWeatherRefresh.value) {
            viewModel.writeWeatherServiceError("")
            viewModel.updateWeatherServiceLoading(true)
            performWeatherDataRefresh(viewModel)
            executeWeatherRefresh.value = false
        }
    }

    // Display error dialog if requested
    if (showErrorDetailsDialog.value) {
        ErrorDetailsDialog(
            errorMessage = weatherServiceError,
            onDismiss = { showErrorDetailsDialog.value = false }
        )
    }

    // Main weather card
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        // Background gradient effect
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                        )
                    )
                )
        ) {
            // Content based on state
            when {
                // Error state
                weatherServiceError.isNotEmpty() -> WeatherErrorState(
                    error = weatherServiceError,
                    onRefresh = {
                        viewModel.clearWeatherData()
                        executeWeatherRefresh.value = true
                    },
                    onShowDetails = { showErrorDetailsDialog.value = true },
                    onRequestPermission = { locationPermissionState.launchPermissionRequest() },
                    // Check permission status directly without using properties
                    hasPermission = locationPermissionState.permission == Manifest.permission.ACCESS_FINE_LOCATION,
                    shouldShowRationale = false // Default to false since we can't easily access this
                )

                // Loading state
                isWeatherServiceLoading -> WeatherLoadingState()

                // Content state
                else -> WeatherContentState(
                    weatherData = weatherData,
                    onRefresh = {
                        viewModel.clearWeatherData()
                        executeWeatherRefresh.value = true
                    },
                    iconScale = iconScale
                )
            }
        }
    }
}

@Composable
fun WeatherContentState(
    weatherData: WeatherResponse?,
    onRefresh: () -> Unit,
    iconScale: Float
) {
    if (weatherData == null) {
        // Empty state, should rarely happen due to the loading state handling
        EmptyWeatherState(onRefresh = onRefresh)
        return
    }

    // Format the current date
    val dateFormat = SimpleDateFormat("E, MMM d", Locale.getDefault())
    val currentDate = dateFormat.format(Date())

    // Format the weather update time
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val updateTime = timeFormat.format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top row with location and refresh
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Location info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = weatherData.address,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                    fontFamily = productsans_family,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Refresh button
            IconButton(
                onClick = onRefresh,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Weather",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Date row
        Text(
            text = currentDate,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
            fontWeight = FontWeight.Normal,
            fontFamily = productsans_family,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Main content with current temperature and conditions
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Current temperature
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${weatherData.days[0].temp}°C",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold,
                    fontFamily = productsans_family,
                    fontSize = 42.sp
                )

                // Weather condition
                Text(
                    text = getWeatherCondition(weatherData.days[0].temp.toInt()),
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium,
                    fontFamily = productsans_family,
                    fontSize = 16.sp
                )
            }

            // Weather icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .graphicsLayer {
                        scaleX = iconScale
                        scaleY = iconScale
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(
                        id = getWeatherIconResource(weatherData.days[0].temp.toInt())
                    ),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(70.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Divider
        Divider(
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
            thickness = 1.dp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Forecast row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Tomorrow
            ForecastDayItem(
                day = "Tomorrow",
                temperature = weatherData.days[1].temp.toInt(),
                iconRes = getWeatherIconResource(weatherData.days[1].temp.toInt())
            )

            // Day after tomorrow
            ForecastDayItem(
                day = "Day After",
                temperature = weatherData.days[2].temp.toInt(),
                iconRes = getWeatherIconResource(weatherData.days[2].temp.toInt())
            )

            // Update info
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Updated",
                    fontSize = 10.sp,
                    fontFamily = productsans_family,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                )

                Text(
                    text = updateTime,
                    fontSize = 12.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun ForecastDayItem(
    day: String,
    temperature: Int,
    iconRes: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = day,
            fontSize = 12.sp,
            fontFamily = productsans_family,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Image(
            painter = painterResource(id = iconRes),
            contentDescription = "Weather Icon for $day",
            modifier = Modifier.size(28.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "$temperature°C",
            fontSize = 14.sp,
            fontFamily = productsans_family,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun WeatherLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(48.dp),
                strokeWidth = 4.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Fetching weather data...",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontFamily = productsans_family,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun WeatherErrorState(
    error: String,
    onRefresh: () -> Unit,
    onShowDetails: () -> Unit,
    onRequestPermission: () -> Unit,
    hasPermission: Boolean,
    shouldShowRationale: Boolean
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            // Different error states
            when {
                error == "LOCATION_NOT_AVAILABLE" -> {
                    // Location is disabled
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Disabled",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Location services are disabled",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = productsans_family,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Please enable location in your device settings to get accurate weather data.",
                        fontSize = 14.sp,
                        fontFamily = productsans_family,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onRefresh,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = "Try Again")
                    }
                }

                error == "NO_PERMISSION" -> {
                    // Permission not granted
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location Permission",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (shouldShowRationale)
                            "Location Permission Needed"
                        else
                            "Location Permission Denied",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = productsans_family,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (shouldShowRationale)
                            "We need location access to provide accurate local weather information."
                        else
                            "You've denied location permission. Please enable it in your device settings.",
                        fontSize = 14.sp,
                        fontFamily = productsans_family,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (shouldShowRationale) {
                        Button(
                            onClick = onRequestPermission,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(text = "Grant Permission")
                        }
                    } else {
                        Button(
                            onClick = onRefresh,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(text = "Try Again")
                        }
                    }
                }

                else -> {
                    // Generic error
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(40.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Failed to Load Weather Data",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = productsans_family,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "We're having trouble connecting to the weather service.",
                        fontSize = 14.sp,
                        fontFamily = productsans_family,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        TextButton(
                            onClick = onShowDetails,
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text(text = "Details")
                        }

                        Button(
                            onClick = onRefresh,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(text = "Try Again")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyWeatherState(onRefresh: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_weather_sunny),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Weather Data Available",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tap below to check the weather for your location.",
                fontSize = 14.sp,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRefresh,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = "Fetch Weather")
            }
        }
    }
}

@Composable
fun ErrorDetailsDialog(
    errorMessage: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = "Error Info",
                tint = MaterialTheme.colorScheme.error
            )
        },
        title = {
            Text(
                text = "Error Details",
                fontFamily = productsans_family,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column {
                Text(
                    text = "The following error occurred while fetching weather data:",
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        fontFamily = productsans_family,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "If this issue persists, please contact support or report this issue.",
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    // TODO: Implement reporting functionality
                    onDismiss()
                }
            ) {
                Text(
                    text = "Report Issue",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp)
    )
}

// Helper functions for weather UI
private fun getWeatherCondition(temperature: Int): String {
    return when {
        temperature > 30 -> "Hot & Sunny"
        temperature > 25 -> "Warm & Clear"
        temperature > 20 -> "Pleasant"
        temperature > 15 -> "Mild"
        temperature > 10 -> "Cool"
        temperature > 5 -> "Cold"
        else -> "Very Cold"
    }
}

private fun getWeatherIconResource(temperature: Int): Int {
    return when {
        temperature > 30 -> R.drawable.ic_weather_sunny // Hot
        temperature > 25 -> R.drawable.ic_weather_sunny // Warm
        temperature > 15 -> R.drawable.ic_weather_sunny // Mild
        temperature > 5 -> R.drawable.ic_weather_sunny // Cool
        else -> R.drawable.ic_weather_sunny // Cold
    }
    // Note: You should replace with actual different weather icons
    // based on the actual weather conditions from your API
}