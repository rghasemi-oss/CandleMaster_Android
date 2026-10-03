package com.example.ui.ndn

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit
) {
    var selectedPanel by remember { mutableStateOf("سفیر PUDO") }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val goldColor = Color(0xFFE5B83B)
    val cardBg = Color(0xFF1D1E24)
    val darkBg = Color(0xFF121212)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkBg
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                // Logo Icon Box
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .border(2.dp, goldColor, CircleShape)
                        .background(cardBg, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📦", fontSize = 28.sp)
                        Text("NDN", color = goldColor, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }

                Text(
                    "شبکه تحویل مطمئن محله (NDN)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    "درگاه ورود سفیران و مدیران شبکه",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    "نوع پنل را انتخاب کنید",
                    color = Color.White,
                    fontSize = 14.sp
                )

                // Panel Selector Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val panels = listOf("سفیر PUDO", "هاب محله", "مشترک", "مدیر سیستم")
                    panels.forEach { panel ->
                        val isSelected = selectedPanel == panel
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(if (isSelected) cardBg else Color(0xFF18191E))
                                .border(1.dp, if (isSelected) goldColor else Color.Transparent, RoundedCornerShape(22.dp))
                                .clickable { selectedPanel = panel },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = panel,
                                color = if (isSelected) goldColor else Color.Gray,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 11.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Username field
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("نام کاربری", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray) },
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

                // Password field
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("رمز عبور", color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray) },
                    trailingIcon = { Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = Color.Gray) },
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

                Spacer(modifier = Modifier.height(8.dp))

                // Login Button
                Button(
                    onClick = { onLoginSuccess(selectedPanel) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = goldColor)
                ) {
                    Text(
                        "ورود",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                TextButton(onClick = { /* Register */ }) {
                    Text("ثبت‌نام کاربر جدید", color = goldColor, fontWeight = FontWeight.Bold)
                }
            }

            // Fingerprint login section at bottom
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HorizontalDivider(color = Color.DarkGray, thickness = 1.dp, modifier = Modifier.width(120.dp))
                Text("یا", color = Color.Gray, fontSize = 12.sp)

                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .border(2.dp, goldColor, CircleShape)
                        .background(cardBg, CircleShape)
                        .clickable { onLoginSuccess("مدیر سیستم") },
                    contentAlignment = Alignment.Center
                ) {
                    Text("👆", fontSize = 28.sp)
                }

                Text(
                    "ورود با اثر انگشت (مدیر سیستم)",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    "دسترسی کامل به تمام پنل‌ها",
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
