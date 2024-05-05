package com.example.ass3part2.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ass3part2.LocationViewModel

@Composable
fun ListWaypointsScreen(locationViewModel: LocationViewModel) {
    LazyColumn(
        modifier = Modifier
            .padding(10.dp)
    ) {
        items(locationViewModel.waypointList) {
            Text(text = "$it")
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 6.dp),
                thickness = 1.dp,
                color = Color.Gray
            )
        }
    }
}