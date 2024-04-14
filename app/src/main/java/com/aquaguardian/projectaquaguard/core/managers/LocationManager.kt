package com.aquaguardian.projectaquaguard.core.managers

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import java.util.Locale

const val TAG = "LocationManager"

@SuppressLint("ComposableNaming", "MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun requestLocationAddress(
    autoRequestPermission: Boolean = false,
    onError: ((errorCode: String?) -> Unit)? = null,
    onLocated: ((addresses: Address?) -> Unit)
) {
    val context = LocalContext.current
    val locationPermissionState = rememberPermissionState(
        permission = Manifest.permission.ACCESS_FINE_LOCATION
    )
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val accuracy = Priority.PRIORITY_BALANCED_POWER_ACCURACY

    var isLocationEnabled by remember { mutableStateOf(false) }
    isLocationEnabled = checkLocationStatus()

    LaunchedEffect(Unit) {
        // Checks if Device Has Permissions to access device location
        if(locationPermissionState.hasPermission){
            // Location Permission is allowed.
            // Now we check if Location Services is enabled in the device
            if(isLocationEnabled){
                // Location Services is enabled in the device.
                // Now we try to get the current location of the device in the form of lat & long

                fusedLocationClient.getCurrentLocation(
                    accuracy, CancellationTokenSource().token,
                ).addOnSuccessListener { locationData ->
                    // Location Data successfully fetched
                    // Now we use that locationData to get lat & long
                    locationData.let { loc ->
                        val geocoder = Geocoder(context, Locale.getDefault())

                        // We check if Device SDK Version is lower than Android 13
                        // getFromLocation() function is deprecated in Android 13 and above
//                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
//
//
//                        }

                        val addresses = geocoder.getFromLocation(loc.latitude, loc.longitude, 1)
                        // We check if the address we got from android.location.Geocoder is not null
                        if (addresses != null) {
                            // We then check if the addresses is not empty
                            if (addresses.isNotEmpty()) {
                                // Checks have passed. We now call the callback function onLocated()
                                onLocated(addresses.first())

                            } else {
                                // Return an Error
                                if (onError != null) {
                                    onError("Retrieved data from android.location.Geocoder returned 0 addresses. WeatherService Aborted.")
                                }
                            }
                        } else {
                            // Return an Error
                            if (onError != null) {
                                onError("Retrieved data from android.location.Geocoder returned null. Maybe try turning on/off Location or GPS Service?")
                            }
                        }
                    }
                }.addOnFailureListener { exception ->
                    // An error had happened while getting the Location.
                    // We now notify onError() callback.
                    if (onError != null) {
                        onError(exception.message)
                    }
                }
            } else {
                if (onError != null) {
                    onError("LOCATION_NOT_AVAILABLE")
                }
            }

        } else {
            if(autoRequestPermission) {
                locationPermissionState.launchPermissionRequest()
            } else {
                if (onError != null) {
                    onError("NO_PERMISSION")
                }
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun checkLocationStatus(): Boolean {
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


}

