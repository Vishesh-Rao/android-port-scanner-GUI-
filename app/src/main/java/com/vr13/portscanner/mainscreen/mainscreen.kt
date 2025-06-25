package com.vr13.portscanner.mainscreen


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.vr13.portscanner.home.HomeScreen
import com.vr13.portscanner.searchscreen.SearchScreen_AllPorts
import com.vr13.portscanner.settings.SettingsScreen

@Composable
fun MainScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {
        when (selectedTab) {
            0 -> HomeScreen(navController)
            1 -> SearchScreen_AllPorts(navController)
            2 -> SettingsScreen(navController)
        }

        BottomNavigationBar(
            selectedItem = selectedTab,
            onItemSelected = { selectedTab = it }
        )
    }
}
