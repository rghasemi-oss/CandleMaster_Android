package com.example.ui.ndn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.DeliveryEntity
import com.example.data.HubLocationEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubPanelScreen(
    deliveries: List<DeliveryEntity>,
    hubs: List<HubLocationEntity>,
    onNavigateToCustomer: () -> Unit,
    onNavigateToPudo: () -> Unit,
    onLogout: () -> Unit
) {
    val goldColor = Color(0xFFE5B83B)
    val cardBg = Color(0xFF1D1E24)
    val darkBg = Color(0xFF121212)

    val activeCount = deliveries.size
    val readyCount = deliveries.count { it.status == "آماده ارسال" || it.status == "موجود در هاب" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("پنل هاب محله (Room DB)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBg)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF18191E)
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.LocalShipping, contentDescription = null) },
                    label = { Text("سفیر PUDO") },
                    selected = false,
                    onClick = onNavigateToPudo,
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Inventory2, contentDescription = null) },
                    label = { Text("هاب محله") },
                    selected = true,
                    onClick = { },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = goldColor, selectedTextColor = goldColor)
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text("مشتری") },
                    selected = false,
                    onClick = onNavigateToCustomer,
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray)
                )
            }
        },
        containerColor = darkBg
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Stats Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Card 1
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Inventory2, contentDescription = null, tint = goldColor, modifier = Modifier.size(20.dp))
                            Text(activeCount.toString(), color = goldColor, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("بسته در هاب", color = Color.Gray, fontSize = 13.sp)
                    }
                }

                // Card 2
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = goldColor, modifier = Modifier.size(20.dp))
                            Text(readyCount.toString(), color = goldColor, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("آماده تحویل", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }

            Text(
                "لیست بسته‌های موجود در هاب (کش محلی Room)",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )

            // Deliveries List from Room
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(deliveries) { pkg ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.Inventory2, contentDescription = null, tint = goldColor)
                                    Text(pkg.trackingId, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                Surface(
                                    color = Color(0xFF2E271B),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Icon(Icons.Default.Schedule, contentDescription = null, tint = goldColor, modifier = Modifier.size(12.dp))
                                        Text(pkg.status, color = goldColor, fontSize = 11.sp)
                                    }
                                }
                            }

                            HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Default.Store, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                        Text("فرستنده: ${pkg.sender}", color = Color.Gray, fontSize = 13.sp)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(14.dp))
                                        Text("مشتری: ${pkg.customer} (${pkg.hubName})", color = Color.Gray, fontSize = 13.sp)
                                    }
                                }
                                Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}
