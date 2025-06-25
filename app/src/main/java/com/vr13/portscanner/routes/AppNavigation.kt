package com.vr13.portscanner.routes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.vr13.portscanner.home.HomeScreen
import com.vr13.portscanner.splashscreen.SplashScreen

import com.vr13.portscanner.searchscreen.SearchScreen_AllPorts
import com.vr13.portscanner.settings.SettingsScreen
@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SplashScreen.route) {

        composable(Routes.SplashScreen.route) {
            SplashScreen {
                navController.navigate(Routes.HomeScreen.route) {
                    popUpTo(Routes.SplashScreen.route) { inclusive = true }
                }
            }
        }

        composable(Routes.HomeScreen.route) {
            HomeScreen(navController = navController)
        }


        composable(Routes.SearchScreen.route) {
            SearchScreen_AllPorts(navController = navController)
        }

        composable(Routes.SettingScreen.route) {
            SettingsScreen(navController = navController)
        }
    }
}
