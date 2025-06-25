package com.vr13.portscanner.routes

sealed class Routes(val route: String) {
    object SplashScreen : Routes("splash_screen")
    object HomeScreen : Routes("home_screen")
    object SearchScreen : Routes("search_screen")
    object SettingScreen : Routes("setting_screen")
}
