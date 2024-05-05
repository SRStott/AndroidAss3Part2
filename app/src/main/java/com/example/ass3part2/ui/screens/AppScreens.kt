package com.example.ass3part2.ui.screens

/* Explanation
    This code defines an enum class AppScreens representing different screens,
    each associated with a unique identifier.
    The screens include the main screen, a screen for requesting permissions,
    a screen for listing waypoints, and a screen for displaying Google Maps.
*/
enum class AppScreens(val id: String) {
    MAIN_SCREEN("MainScreen"),
    NEED_PERMISSION_SCREEN("NeedPermissionsScreen"),
    LIST_WAYPOINTS_SCREEN("ListWaypointsScreen"),
    GOOGLE_MAP_SCREEN("GoogleMapScreen")
}