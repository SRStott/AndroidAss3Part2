package com.example.ass3part2.viewmodel

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Looper
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.ass3part2.exception.Assignment3Exception
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

// This LocationViewModel class manages location-related data and functionality for the application.

@Suppress("UNCHECKED_CAST")
class LocationViewModel(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder
) : ViewModel() {
    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = extras[APPLICATION_KEY] as Application
                val context = application.applicationContext

                return LocationViewModel(
                    LocationServices.getFusedLocationProviderClient(context),
                    Geocoder(context)
                ) as T
            }
        }

        private const val LOCATION_INTERVAL_MILLISECONDS = 5e3.toLong()
        private const val UNABLE_TO_GET_STREET_ADDRESS = "Unable to get street address"

        val locationPermissions: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val locationRequestHighAccuracy = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            LOCATION_INTERVAL_MILLISECONDS
        )
            .build()
        val locationRequestBalancedPowerAccuracy = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            LOCATION_INTERVAL_MILLISECONDS
        )
            .build()
    }

    private val locationListener: LocationListener = LocationListener { location ->
        currentLocation.value = location
        resolveAddress()
    }

    val isTracking = mutableStateOf(false)
    val currentLocationRequest: MutableState<LocationRequest?> = mutableStateOf(null)
    val currentLocation: MutableState<Location?> = mutableStateOf(null)
    val currentAddress: MutableState<String?> = mutableStateOf(UNABLE_TO_GET_STREET_ADDRESS)
    val waypointList = mutableStateListOf<Location>()

    fun addCurrentLocationToWaypointList() {
        if (currentLocation.value != null) {
            waypointList.add(currentLocation.value!!)
        }
    }

    fun haveLocationPermissions(context: Context): Boolean {
        return locationPermissions
            .all { permission ->
                ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
    }

    fun resolveAddress() {
        fun handleAddressResult(addresses: List<Address>) {
            currentAddress.value = addresses
                .stream()
                .findFirst()
                .map { it.getAddressLine(0) }
                .orElse(UNABLE_TO_GET_STREET_ADDRESS)
        }

        if (currentLocation.value == null) {
            handleAddressResult(listOf())
            return
        }

        val location = currentLocation.value!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            ) { addresses -> handleAddressResult(addresses) }
        } else {
            handleAddressResult(
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                ) ?: listOf()
            )
        }
    }

    @SuppressLint("MissingPermission")
    fun startListeningLocation(locationRequest: LocationRequest, context: Context) {
        if (haveLocationPermissions(context)) {
            fusedLocationProviderClient
                .requestLocationUpdates(
                    locationRequest,
                    locationListener,
                    Looper.getMainLooper()
                )
            isTracking.value = true
            currentLocationRequest.value = locationRequest
        } else {
            throw Assignment3Exception("App does not have location permissions")
        }
    }

    fun stopListeningLocation() {
        fusedLocationProviderClient.removeLocationUpdates(locationListener)
        isTracking.value = false
    }

    fun useLocationRequest(locationRequest: LocationRequest, context: Context) {
        stopListeningLocation()
        startListeningLocation(locationRequest, context)
    }
}