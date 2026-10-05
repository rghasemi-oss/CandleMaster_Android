package com.example.ui.ndn

import androidx.compose.foundation.BorderStroke
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    deliveries: List<DeliveryEntity>,
    onAddDelivery: (String, String, String, String, String, String) -> Unit,
    onDeleteDelivery: (DeliveryEntity) -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToCustomer: () -> Unit,
    onNavigateToPudo: () -> Unit,
    onNavigateToHub: () -> Unit,
    onLogout: () -> Unit
) {
    val goldColor = Color(0xFFE5B83B)
    val cardBg = Color(0xFF1D1E24)
    val darkBg = Color(0xFF121212)

    var searchQuery by remember { mutableStateOf("") }
    var showAddDialog by remember { mutableStateOf(false) }

    val filteredDeliveries = deliveries.filter {
        it.trackingId.contains(searchQuery, ignoreCase = true) ||
                it.customer.contains(searchQuery, ignoreCase = true) ||
                it.sender.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("پنل مدیریت سیستم (ادمین)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Notifications */ }) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = goldColor)
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = null, tint = goldColor)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBg)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = goldColor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Delivery", tint = Color.Black)
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
            // Quick Panel Access Row
            Text("دسترسی سریع به پنل‌ها (اتصال واقعی Backend API / Render)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateToCustomer,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = cardBg),
                    border = BorderStroke(1.dp, Color.DarkGray),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("مشتری", color = Color.White, fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = onNavigateToPudo,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = cardBg),
                    border = BorderStroke(1.dp, Color.DarkGray),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("سفیر PUDO", color = Color.White, fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = onNavigateToHub,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = cardBg),
                    border = BorderStroke(1.dp, Color.DarkGray),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("هاب محله", color = Color.White, fontSize = 12.sp)
                }
            }

            // System Analytics Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(deliveries.size.toString(), color = goldColor, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("کل بسته‌ها", color = Color.Gray, fontSize = 12.sp)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Backend API", color = goldColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ارتباط ابری", color = Color.Gray, fontSize = 11.sp)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(containerColor = cardBg),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Render", color = goldColor, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("هاست سرور", color = Color.Gray, fontSize = 11.sp)
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("جستجوی بسته، مشتری یا فرستنده...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = cardBg,
                    unfocusedContainerColor = cardBg,
                    focusedBorderColor = goldColor,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true
            )

            Text("مدیریت جامع پایگاه داده (Backend API & Render)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)

            // Deliveries List
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredDeliveries) { delivery ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(delivery.trackingId, color = goldColor, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Surface(
                                    color = Color(0xFF2E271B),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        delivery.status,
                                        color = goldColor,
                                        fontSize = 11.sp,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            HorizontalDivider(color = Color.DarkGray, thickness = 0.5.dp)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("فرستنده: ${delivery.sender}", color = Color.Gray, fontSize = 12.sp)
                                Text("مشتری: ${delivery.customer}", color = Color.Gray, fontSize = 12.sp)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("هاب: ${delivery.hubName}", color = Color.Gray, fontSize = 12.sp)
                                Text("وزن: ${delivery.weight}", color = Color.Gray, fontSize = 12.sp)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TextButton(
                                    onClick = { onDeleteDelivery(delivery) }
                                ) {
                                    Text("حذف بسته", color = Color(0xFFCF6679), fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var newId by remember { mutableStateOf("IR-2026-" + (1000..9999).random()) }
        var newSender by remember { mutableStateOf("") }
        var newCustomer by remember { mutableStateOf("") }
        var newHub by remember { mutableStateOf("هاب سعادت‌آباد") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("ثبت بسته جدید (ارسال به سرور واقعی)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = newId,
                        onValueChange = { newId = it },
                        label = { Text("شناسه رهگیری") }
                    )
                    OutlinedTextField(
                        value = newSender,
                        onValueChange = { newSender = it },
                        label = { Text("فرستنده") }
                    )
                    OutlinedTextField(
                        value = newCustomer,
                        onValueChange = { newCustomer = it },
                        label = { Text("مشتری") }
                    )
                    OutlinedTextField(
                        value = newHub,
                        onValueChange = { newHub = it },
                        label = { Text("هاب محله") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newSender.isNotBlank() && newCustomer.isNotBlank()) {
                            onAddDelivery(newId, newSender, newCustomer, newHub, "موجود در هاب", "1.0 kg")
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = goldColor)
                ) {
                    Text("ثبت نهایی", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("انصراف")
                }
            }
        )
    }
}
