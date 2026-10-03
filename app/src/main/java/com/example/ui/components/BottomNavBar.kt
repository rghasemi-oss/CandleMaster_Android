package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Dashboard)
    object Audits : Screen("audits", "Audits", Icons.Default.Assignment)
    object Custody : Screen("custody", "Custody", Icons.Default.AccountBalance)
    object AiAdvisor : Screen("ai_advisor", "AI Advisor", Icons.Default.SmartToy)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

@Composable
fun BottomNavBar(
    currentRoute: String,
    onItemSelected: (Screen) -> Unit
) {
    val items = listOf(
        Screen.Dashboard,
        Screen.Audits,
        Screen.Custody,
        Screen.AiAdvisor,
        Screen.Settings
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceVariant
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = { onItemSelected(screen) }
            )
        }
    }
}
