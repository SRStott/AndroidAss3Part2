package com.example.ass3part2.utils

import androidx.navigation.NavHostController

/* Explanation
    The navigateAndPopBack function navigates to a destination screen using the
    provided NavHostController and pops the back stack up to the specified destination.

 */

fun navigateAndPopBack(navController: NavHostController, destination: String) {
    navController.navigate(destination) {
        popUpTo(navController.graph.id) { inclusive = true }
    }
}