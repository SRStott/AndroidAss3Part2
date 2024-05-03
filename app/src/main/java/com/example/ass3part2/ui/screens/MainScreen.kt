package com.example.ass3part2.ui.screens

import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ass3part2.LocationViewModel

@Composable
fun MainScreen(locationViewModel: LocationViewModel) {
    Column(
        modifier = Modifier
            .padding(10.dp)
    ) {
        LocationDetails(
            location = locationViewModel.currentLocation.value,
            locationViewModel.currentAddress.value
        )
    }
}

@Composable
fun LocationDetails(location: Location?, address: String?) {
    @Composable
    fun LocationValueText(title: String, value: Any?) {
        Row {
            Text(
                text = title,
                modifier = Modifier
                    .width(150.dp)
            )

            Text(text = value?.toString() ?: "Not tracking location")
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