// SplashScreen.kt
package com.vr13.portscanner.splashscreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.vr13.portscanner.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNavigateToHomeScreen: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000) // 2-second splash
        onNavigateToHomeScreen()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.teal1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.size(200.dp)
        )
        Text(
            text = "Port Scanner",
            color = colorResource(id = R.color.white1),
            fontSize = 24.sp
        )
    }
}