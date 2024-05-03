package com.example.ass3part2.utils

import androidx.navigation.NavHostController

fun navigateAndPopBack(navController: NavHostController, destination: String) {
    navController.navigate(destination) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}