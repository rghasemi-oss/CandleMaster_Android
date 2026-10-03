package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.ndn.*
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.NdnViewModel

enum class NdnScreen {
    LOGIN,
    CUSTOMER_PANEL,
    PUDO_PANEL,
    HUB_PANEL,
    ADMIN_PANEL,
    SETTINGS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = androidx.compose.ui.platform.LocalContext.current
            val themePreferences = remember { com.example.data.ThemePreferences(context) }
            val isDarkMode by themePreferences.isDarkModeFlow.collectAsStateWithLifecycle(initialValue = true)

            MyApplicationTheme(darkTheme = isDarkMode) {
                val viewModel: NdnViewModel = viewModel()
                var currentScreen by remember { mutableStateOf(NdnScreen.LOGIN) }

                val deliveries by viewModel.deliveries.collectAsStateWithLifecycle()
                val hubs by viewModel.hubs.collectAsStateWithLifecycle()

                BackHandler(enabled = currentScreen != NdnScreen.LOGIN) {
                    currentScreen = NdnScreen.LOGIN
                }

                when (currentScreen) {
                    NdnScreen.LOGIN -> LoginScreen(
                        onLoginSuccess = { panelType ->
                            currentScreen = when (panelType) {
                                "سفیر PUDO" -> NdnScreen.PUDO_PANEL
                                "هاب محله" -> NdnScreen.HUB_PANEL
                                "مدیر سیستم" -> NdnScreen.ADMIN_PANEL
                                else -> NdnScreen.CUSTOMER_PANEL
                            }
                        }
                    )
                    NdnScreen.CUSTOMER_PANEL -> CustomerPanelScreen(
                        onScanClicked = { currentScreen = NdnScreen.HUB_PANEL },
                        onLogout = { currentScreen = NdnScreen.LOGIN }
                    )
                    NdnScreen.PUDO_PANEL -> PudoPanelScreen(
                        onPackageRegistered = { trackingId, sender, customer, hub ->
                            viewModel.addDelivery(trackingId, sender, customer, hub, "آماده ارسال", "1.2 kg")
                            currentScreen = NdnScreen.HUB_PANEL
                        },
                        onLogout = { currentScreen = NdnScreen.LOGIN }
                    )
                    NdnScreen.HUB_PANEL -> HubPanelScreen(
                        deliveries = deliveries,
                        hubs = hubs,
                        onNavigateToCustomer = { currentScreen = NdnScreen.CUSTOMER_PANEL },
                        onNavigateToPudo = { currentScreen = NdnScreen.PUDO_PANEL },
                        onLogout = { currentScreen = NdnScreen.LOGIN }
                    )
                    NdnScreen.ADMIN_PANEL -> AdminPanelScreen(
                        deliveries = deliveries,
                        onAddDelivery = { id, sender, customer, hub, status, weight ->
                            viewModel.addDelivery(id, sender, customer, hub, status, weight)
                        },
                        onDeleteDelivery = { delivery ->
                            viewModel.deleteDelivery(delivery)
                        },
                        onNavigateToSettings = { currentScreen = NdnScreen.SETTINGS },
                        onNavigateToCustomer = { currentScreen = NdnScreen.CUSTOMER_PANEL },
                        onNavigateToPudo = { currentScreen = NdnScreen.PUDO_PANEL },
                        onNavigateToHub = { currentScreen = NdnScreen.HUB_PANEL },
                        onLogout = { currentScreen = NdnScreen.LOGIN }
                    )
                    NdnScreen.SETTINGS -> SettingsScreen(
                        onBack = { currentScreen = NdnScreen.ADMIN_PANEL }
                    )
                }
            }
        }
    }
}
