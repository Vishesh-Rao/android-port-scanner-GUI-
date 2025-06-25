package com.vr13.portscanner.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vr13.portscanner.R
import com.vr13.portscanner.mainscreen.BottomNavigationBar
import com.vr13.portscanner.routes.Routes
import com.vr13.portscanner.settings.globalPortRange
import com.vr13.portscanner.settings.globalThreads
import com.vr13.portscanner.shared.OpenPortsRepository
import com.vr13.portscanner.portscannerlogic.scanPorts
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    var ipAddress by remember { mutableStateOf("") }
    var isScanning by remember { mutableStateOf(false) }
    var rawProgress by remember { mutableStateOf(0f) }
    val progress by animateFloatAsState(targetValue = rawProgress, label = "")
    var openPorts by remember { mutableStateOf(listOf<Int>()) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var scanJob by remember { mutableStateOf<Job?>(null) }

    val portNameMap = mapOf(
        21 to "FTP",
        22 to "SSH",
        23 to "Telnet",
        25 to "SMTP",
        53 to "DNS",
        80 to "HTTP",
        110 to "POP3",
        143 to "IMAP",
        443 to "HTTPS",
        3306 to "MySQL",
        8080 to "HTTP-Alt"
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(selectedItem = 0) { idx ->
                when (idx) {
                    1 -> navController.navigate(Routes.SearchScreen.route)
                    2 -> navController.navigate(Routes.SettingScreen.route)
                }
            }
        }
    ) { paddingValues ->
        Column(
            Modifier
                .fillMaxSize()
                .background(colorResource(R.color.black1))
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    navController.navigate(Routes.HomeScreen.route) {
                        popUpTo(Routes.HomeScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = colorResource(R.color.white1))
                }
                Text(
                    "Port Scanner",
                    modifier = Modifier.weight(1f),
                    color = colorResource(R.color.white1),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = { navController.navigate(Routes.SettingScreen.route) }) {
                    Icon(Icons.Default.Settings, contentDescription = null, tint = colorResource(R.color.white1))
                }
            }

            Spacer(Modifier.height(20.dp))

            TextField(
                value = ipAddress,
                onValueChange = { ipAddress = it },
                placeholder = { Text("Enter IP or Hostname", color = colorResource(R.color.bottom_bar)) },
                modifier = Modifier.fillMaxWidth(0.9f),
                shape = RoundedCornerShape(8.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = colorResource(R.color.light_Grey),
                    unfocusedContainerColor = colorResource(R.color.light_Grey),
                    cursorColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            Row(Modifier.fillMaxWidth(0.9f), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(
                    onClick = {
                        if (ipAddress.trim().isEmpty()) {
                            Toast.makeText(context, "Please enter a valid IP address.", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        scanJob = coroutineScope.launch {
                            isScanning = true
                            rawProgress = 0f
                            openPorts = emptyList()

                            val parts = globalPortRange.split("-")
                            val start = parts.getOrNull(0)?.toIntOrNull() ?: 1
                            val end = parts.getOrNull(1)?.toIntOrNull() ?: 1000
                            val total = end - start + 1
                            val batch = total / globalThreads

                            Toast.makeText(context, "Scanning $ipAddress...", Toast.LENGTH_SHORT).show()

                            val scanned = mutableListOf<Int>()
                            for ((i, cStart) in (start..end step batch).withIndex()) {
                                val chunk = cStart until (cStart + batch).coerceAtMost(end + 1)
                                val res = scanPorts(
                                    ip = ipAddress.trim(),
                                    portRange = chunk,
                                    threads = globalThreads
                                ) { chunkProgress ->
                                    rawProgress = (i + chunkProgress) / (total / batch.toFloat())
                                }
                                scanned.addAll(res)
                            }

                            openPorts = scanned.toList()
                            OpenPortsRepository.openPorts = scanned.toList()

                            isScanning = false
                            Toast.makeText(context, "Scan complete.", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = !isScanning,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.button))
                ) {
                    Text("Start Scan", color = Color.White)
                }

                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = {
                        scanJob?.cancel()
                        isScanning = false
                        Toast.makeText(context, "Scan stopped.", Toast.LENGTH_SHORT).show()
                    },
                    enabled = isScanning,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.light_Grey))
                ) {
                    Text("Stop Scan", color = Color.White)
                }
            }

            if (isScanning) {
                Spacer(Modifier.height(24.dp))
                Text("Scanning", color = Color.White)
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    color = colorResource(R.color.button),
                    trackColor = colorResource(R.color.light_Grey)
                )
            }

            if (!isScanning && openPorts.isEmpty()) {
                Spacer(Modifier.height(24.dp))
                Text("No open ports found.", color = Color.LightGray, fontSize = 16.sp, textAlign = TextAlign.Center)
            }

            if (!isScanning && openPorts.isNotEmpty()) {
                Spacer(Modifier.height(16.dp))
                Text("Open Ports", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)

                LazyColumn {
                    items(openPorts) { port ->
                        val portName = portNameMap[port] ?: "Unknown"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        color = colorResource(R.color.light_Grey),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.global),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column {
                                Text("Port $port", color = Color.White, fontSize = 16.sp)
                                Text(portName, color = Color.LightGray, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
