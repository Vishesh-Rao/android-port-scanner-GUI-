package com.vr13.portscanner.searchscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vr13.portscanner.R
import com.vr13.portscanner.mainscreen.BottomNavigationBar
import com.vr13.portscanner.routes.Routes
import com.vr13.portscanner.shared.OpenPortsRepository

@Composable
fun SearchScreen_AllPorts(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }

    // Get the updated list of open ports after scan from shared repository
    val openPorts = remember { OpenPortsRepository.openPorts }
    val filteredPorts = remember(searchQuery, openPorts) {
        if (searchQuery.isNotEmpty()) {
            openPorts.filter { it.toString().contains(searchQuery.trim()) }
        } else openPorts
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedItem = 1,
                onItemSelected = { index ->
                    when (index) {
                        0 -> navController.navigate(Routes.HomeScreen.route) {
                            popUpTo(Routes.HomeScreen.route) { inclusive = true }
                        }
                        1 -> {} // Current screen
                        2 -> navController.navigate(Routes.SettingScreen.route) {
                            popUpTo(Routes.SearchScreen.route)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.black1))
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = colorResource(R.color.white1)
                    )
                }
                Text(
                    "Search",
                    color = colorResource(R.color.white1),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(15.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Search by Port Number", color = colorResource(R.color.bottom_bar))
                },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(id = R.color.light_Grey),
                    unfocusedContainerColor = colorResource(id = R.color.light_Grey),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredPorts.isEmpty()) {
                Text("No matching open ports.", color = Color.LightGray)
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(top = 8.dp)
                ) {
                    items(filteredPorts) { port ->
                        val service = when (port) {
                            21 -> "FTP"
                            22 -> "SSH"
                            23 -> "Telnet"
                            25 -> "SMTP"
                            53 -> "DNS"
                            80 -> "HTTP"
                            110 -> "POP3"
                            143 -> "IMAP"
                            443 -> "HTTPS"
                            3306 -> "MySQL"
                            8080 -> "HTTP-Alt"
                            else -> "Unknown"
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp, horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Port $port",
                                color = Color.White,
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(service, color = Color.Gray, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
