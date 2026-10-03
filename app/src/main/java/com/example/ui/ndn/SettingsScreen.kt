package com.example.ui.ndn

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.ThemePreferences
import com.example.data.remote.ConfigPreferences
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val configPrefs = remember { ConfigPreferences(context) }
    val themePreferences = remember { ThemePreferences(context) }
    val coroutineScope = rememberCoroutineScope()

    var supabaseUrl by remember { mutableStateOf(configPrefs.supabaseUrl) }
    var supabaseKey by remember { mutableStateOf(configPrefs.supabaseKey) }
    var renderUrl by remember { mutableStateOf(configPrefs.renderBackendUrl) }
    var showSavedMessage by remember { mutableStateOf(false) }

    val isDarkMode by themePreferences.isDarkModeFlow.collectAsStateWithLifecycle(initialValue = true)

    val goldColor = Color(0xFFE5B83B)
    val cardBg = Color(0xFF1D1E24)
    val darkBg = Color(0xFF121212)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("تنظیمات و شخصی‌سازی برنامه", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBg)
            )
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
            // Theme Switcher Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.DarkMode, contentDescription = null, tint = goldColor)
                        Column {
                            Text("حالت تاریک / روشن (Dark Mode)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("تغییر تم برنامه و ذخیره خودکار در DataStore", color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { checked ->
                            coroutineScope.launch {
                                themePreferences.setDarkMode(checked)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = goldColor,
                            checkedTrackColor = Color(0xFF2E271B)
                        )
                    )
                }
            }

            // Connection Configuration Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Cloud, contentDescription = null, tint = goldColor)
                        Text("پیکربندی اتصال واقعی (Supabase & Render)", color = goldColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Text(
                        "لطفاً آدرس پروژه Supabase، کلید Anon و آدرس سرور مستقر شده روی Render.com را وارد کنید.",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    OutlinedTextField(
                        value = supabaseUrl,
                        onValueChange = { supabaseUrl = it },
                        label = { Text("Supabase Project URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = supabaseKey,
                        onValueChange = { supabaseKey = it },
                        label = { Text("Supabase Anon / API Key") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = renderUrl,
                        onValueChange = { renderUrl = it },
                        label = { Text("Render.com Backend URL") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Button(
                        onClick = {
                            configPrefs.supabaseUrl = supabaseUrl
                            configPrefs.supabaseKey = supabaseKey
                            configPrefs.renderBackendUrl = renderUrl
                            showSavedMessage = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = goldColor)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ذخیره تنظیمات اتصال", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    if (showSavedMessage) {
                        Text(
                            "تنظیمات با موفقیت ذخیره شد.",
                            color = Color(0xFF4CAF50),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}
