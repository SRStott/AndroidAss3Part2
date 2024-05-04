package com.example.ass3part2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ass3part2.ui.screens.AppScreens
import com.example.ass3part2.ui.screens.ListWaypointsScreen
import com.example.ass3part2.ui.screens.MainScreen
import com.example.ass3part2.ui.screens.NeedPermissionScreen
import com.example.ass3part2.ui.theme.Ass3Part2Theme
import com.example.ass3part2.utils.navigateAndPopBack

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val locationViewModel = viewModels<LocationViewModel> {
                LocationViewModel.Factory
            }.value
            val navController = rememberNavController()
            val permissionRequestLaunger = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions()
            ) {
                if (it.all { entry -> entry.value }) {
                    locationViewModel.startListeningLocation(LocationViewModel.locationRequestHighAccuracy, this)
                    navigateAndPopBack(navController, AppScreens.MAIN_SCREEN.id)
                }
            }

            Ass3Part2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(title = { Text(text = "GPSTrackingDemo") })
                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = AppScreens.MAIN_SCREEN.id,
                            modifier = Modifier.padding(paddingValues = innerPadding)
                        ) {
                            composable(AppScreens.MAIN_SCREEN.id) {
                                MainScreen(locationViewModel, navController)
                            }

                            composable(AppScreens.NEED_PERMISSION_SCREEN.id) {
                                NeedPermissionScreen()
                            }

                            composable(AppScreens.LIST_WAYPOINTS_SCREEN.id) {
                                ListWaypointsScreen(locationViewModel)
                            }
                        }

                        if (locationViewModel.haveLocationPermissions(this)) {
                            locationViewModel.startListeningLocation(
                                LocationViewModel.locationRequestHighAccuracy,
                                this
                            )
                        } else {
                            navigateAndPopBack(navController, AppScreens.NEED_PERMISSION_SCREEN.id)
                            SideEffect {
                                permissionRequestLaunger.launch(LocationViewModel.locationPermissions)
                            }
                        }
                    }
                }
            }
        }
    }
}