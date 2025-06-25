package com.vr13.portscanner.mainscreen
// Core Compose
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vr13.portscanner.R


@Composable
@Preview(showSystemUi = true)


fun BottomNavigationBar(selectedItem: Int = 0, onItemSelected: (Int) -> Unit = {}) {
    Column {
        // This is the partition line

    HorizontalDivider(color = colorResource(R.color.light_Grey), thickness = 1.dp)
    NavigationBar(
        containerColor = colorResource(R.color.dark_grey), // Match the dark background
        tonalElevation = 0.dp,
        modifier = Modifier.height(80.dp)
    ) {
        NavigationBarItem(
            selected = selectedItem == 0,
            onClick = { onItemSelected(0) },
            icon = {
                Icon(painter = painterResource(id=R.drawable.homepage), contentDescription = "Home",modifier = Modifier.size(24.dp))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(id=R.color.bottom_bar),
                unselectedIconColor = colorResource(id=R.color.bottom_bar),
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedItem == 1,
            onClick = { onItemSelected(1) },
            icon = {
                Icon(Icons.Default.Search, contentDescription = "Search",modifier = Modifier.size(24.dp))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(id=R.color.bottom_bar),
                unselectedIconColor = colorResource(id=R.color.bottom_bar),
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = selectedItem == 2,
            onClick = { onItemSelected(2) },
            icon = {
                Icon(painter = painterResource(R.drawable.settings), contentDescription = "Settings",modifier = Modifier.size(24.dp))
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = colorResource(id=R.color.bottom_bar),
                unselectedIconColor = colorResource(id=R.color.bottom_bar),
                indicatorColor = Color.Transparent
            )
        )
    }
}}
