package com.example.ass3part2.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun NeedPermissionScreen() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize(1f)) {
        Text(
            text = "Application needs location permissions",
            fontWeight = FontWeight.Medium,
            fontSize = 23.sp
        )
    }
}