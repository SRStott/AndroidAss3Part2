package com.example.ass3part2.ui.screens

import android.location.Location
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ass3part2.ui.components.ButtonComponent
import com.example.ass3part2.viewmodel.LocationViewModel

@Composable
fun MainScreen(locationViewModel: LocationViewModel, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        LocationDetails(
            location = locationViewModel.currentLocation.value,
            locationViewModel.currentAddress.value,
            locationViewModel.isTracking.value
        )
        HorizontalDivider(modifier = Modifier.padding(top = 15.dp))

        LocationControls(locationViewModel)

        WaypointButtons(locationViewModel, navController)
    }
}

@Composable
fun LocationControls(locationViewModel: LocationViewModel) {
    val context = LocalContext.current

    Column {
        // Location On and Off
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            Text(
                text = "Location Updates",
                modifier = Modifier.padding(end = 15.dp)
            )
            Switch(checked = locationViewModel.isTracking.value, onCheckedChange = { checked ->
                if (checked) {
                    locationViewModel.startListeningLocation(
                        locationViewModel.currentLocationRequest.value
                            ?: LocationViewModel.locationRequestHighAccuracy,
                        context
                    )
                } else {
                    locationViewModel.stopListeningLocation()
                }
            })
        }
        Row {
            Text(text = "Updates", Modifier.width(75.dp))
            Text(text = if (locationViewModel.isTracking.value) "on" else "off")
        }

        // Location accuracy
        val isHighAccuracy =
            LocationViewModel.locationRequestHighAccuracy === locationViewModel.currentLocationRequest.value
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(1f)
        ) {
            Text(
                text = "GPS/SavePower",
                modifier = Modifier.padding(end = 15.dp)
            )
            Switch(checked = isHighAccuracy, onCheckedChange = { checked ->
                if (checked) {
                    locationViewModel.useLocationRequest(
                        LocationViewModel.locationRequestHighAccuracy,
                        context
                    )
                } else {
                    locationViewModel.useLocationRequest(
                        LocationViewModel.locationRequestBalancedPowerAccuracy,
                        context
                    )
                }
            })
        }
        Row {
            Text(text = "Sensor", Modifier.width(75.dp))
            Text(
                text = if (isHighAccuracy) "GPS" else "Cell Tower + WiFi"
            )
        }
    }
}

@Composable
fun LocationDetails(location: Location?, address: String?, isTracking: Boolean) {
    @Composable
    fun LocationValueText(title: String, value: Any?) {
        Row {
            Text(
                text = title,
                modifier = Modifier
                    .width(150.dp)
            )

            Text(text = if (isTracking) value?.toString() ?: "" else "Not tracking location")
        }
    }

    fun resolveOptionalValues(hasValue: Boolean?, extractor: () -> Any?): Any? {
        if (hasValue == true) {
            return extractor()
        }
        return if (hasValue == false) "Not available" else null
    }

    Column {
        LocationValueText(title = "Lat:", value = location?.latitude)
        LocationValueText(title = "Lon:", value = location?.longitude)
        LocationValueText(
            title = "Altitude:",
            resolveOptionalValues(location?.hasAltitude()) { location?.altitude }
        )
        LocationValueText(title = "Accuracy:", value = location?.accuracy)
        LocationValueText(
            title = "Speed:",
            value = resolveOptionalValues(location?.hasSpeed()) { location?.speed }
        )
        LocationValueText(title = "Address:", value = address)
    }
}

@Composable
fun WaypointButtons(locationViewModel: LocationViewModel, navController: NavController){
    val context = LocalContext.current
    val wayPointCount = locationViewModel.waypointList.count()

    Row {
        Text(text = "Waypoints: ")
        Text(text = wayPointCount.toString())
    }
    ButtonComponent(
        onClick = locationViewModel::addCurrentLocationToWaypointList,
        buttonText = "New Waypoint"
    )

    ButtonComponent(
        onClick = {
            if (wayPointCount > 0) {
                navController.navigate(AppScreens.LIST_WAYPOINTS_SCREEN.id)
            } else {
                Toast.makeText(context, "There are no waypoints", Toast.LENGTH_SHORT).show()
            }
        },
        buttonText = "Show Waypoint List"
    )

    ButtonComponent(
        onClick = {
            navController.navigate(AppScreens.GOOGLE_MAP_SCREEN.id)
        },
        buttonText = "Show Map"
    )
}

