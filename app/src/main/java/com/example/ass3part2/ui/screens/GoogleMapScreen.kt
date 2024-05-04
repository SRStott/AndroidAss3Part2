package com.example.ass3part2.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.ass3part2.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun GoogleMapScreen(locationViewModel: LocationViewModel) {
    val context = LocalContext.current
    val itemsList = locationViewModel.locationList.toList()
    var lastLocationPlaced = LatLng(0.0,0.0)
    var cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()
    Box(
      modifier = Modifier.fillMaxSize()
    ){
        GoogleMap(
            cameraPositionState = cameraPositionState
        ){
            itemsList.forEach {
                Marker(
                    state = MarkerState(position = LatLng(it.latitude,it.longitude)),
                    title = "Lat: ${it.latitude}, Lon: ${it.longitude}",
                    onClick = {marker ->
                        onMarkerClicked(marker, context)
                    }
                )
                lastLocationPlaced = LatLng(it.latitude,it.longitude)
            }
            coroutineScope.launch {
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(lastLocationPlaced,15f))

            }

        }
    }
}

fun onMarkerClicked(marker: Marker,context: Context): Boolean{
    var clicks: Int = marker.tag.toString().toInt()
    clicks++;
    marker.tag = clicks
    Toast.makeText(context,"Marker ${marker.title} was clicked ${marker.tag} times",Toast.LENGTH_SHORT).show()
    return true
}

