// SettingsScreen.kt
package com.vr13.portscanner.settings

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vr13.portscanner.R
import com.vr13.portscanner.mainscreen.BottomNavigationBar
import com.vr13.portscanner.routes.Routes

var globalTimeout by mutableStateOf(500)
var globalPortRange by mutableStateOf("")
var globalThreads by mutableStateOf(10)
var globalCveSource by mutableStateOf("")

@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current

    // Override system back press to navigate to HomeScreen
    BackHandler {
        navController.navigate(Routes.HomeScreen.route) {
            popUpTo(Routes.HomeScreen.route) { inclusive = true }
            launchSingleTop = true
        }
    }

    var timeout by remember { mutableStateOf(globalTimeout.toString()) }
    var portRange by remember { mutableStateOf(globalPortRange) }
    var threads by remember { mutableStateOf(globalThreads.toString()) }
    var cveSource by remember { mutableStateOf(globalCveSource) }

    Scaffold(bottomBar = {
        BottomNavigationBar(
            selectedItem = 2,
            onItemSelected = { index ->
                when (index) {
                    0 -> navController.navigate(Routes.HomeScreen.route)
                    1 -> navController.navigate(Routes.SearchScreen.route)
                    2 -> {} // current screen
                }
            }
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.black1))
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    navController.navigate(Routes.HomeScreen.route) {
                        popUpTo(Routes.HomeScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                }
                Text(
                    "Settings",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Scan Settings", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))

            CustomTextField(value = timeout, onValueChange = { timeout = it }, placeholder = "Timeout (ms)")
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(value = portRange, onValueChange = { portRange = it }, placeholder = "Port Range")
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(value = threads, onValueChange = { threads = it }, placeholder = "Threads")

            Spacer(modifier = Modifier.height(24.dp))

            Text("CVE Lookup", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(12.dp))
            CustomTextField(value = cveSource, onValueChange = { cveSource = it }, placeholder = "CVE Source")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    globalTimeout = timeout.toIntOrNull() ?: 500
                    globalPortRange = portRange
                    globalThreads = threads.toIntOrNull() ?: 10
                    globalCveSource = cveSource

                    Toast.makeText(
                        context,
                        "Settings saved. Future scans will use: Timeout = ${'$'}globalTimeout ms, Ports = ${'$'}globalPortRange, Threads = ${'$'}globalThreads",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.button)),
                shape = RoundedCornerShape(8.dp),
            ) {
                Text("Save Settings", color = Color.White)
            }
        }
    }
}

@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, placeholder: String) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            cursorColor = Color.White,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.Gray,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = colorResource(id = R.color.dark_grey),
            unfocusedContainerColor = colorResource(id = R.color.dark_grey)
        )
    )
}
