package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.viewmodel.ChatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiAdvisorScreen(
    chatViewModel: ChatViewModel = viewModel()
) {
    val goldColor = Color(0xFFE5B83B)
    val cardBg = Color(0xFF1D1E24)
    val darkBg = Color(0xFF121212)

    var prompt by remember { mutableStateOf("") }
    val messages by chatViewModel.messages.collectAsStateWithLifecycle()
    val isLoading by chatViewModel.isLoading.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.SmartToy, contentDescription = null, tint = goldColor)
                        Text("چت‌بات هوش مصنوعی لجستیک (Gemini 3.5 Flash)", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
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
            // Chat history thread
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(messages) { msg ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                    ) {
                        Surface(
                            color = if (msg.isUser) Color(0xFF2E271B) else cardBg,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.widthIn(max = 300.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = if (msg.isUser) Icons.Default.Person else Icons.Default.SmartToy,
                                    contentDescription = null,
                                    tint = goldColor,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = msg.text,
                                    color = Color.White,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
                if (isLoading) {
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                            Surface(
                                color = cardBg,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.size(16.dp), color = goldColor)
                                    Text("در حال بررسی وضعیت بسته‌ها با Gemini...", color = Color.Gray, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Input Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = prompt,
                    onValueChange = { prompt = it },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("ai_prompt_input"),
                    placeholder = { Text("سوال درباره وضعیت بسته یا آدرس هاب...", color = Color.Gray) },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = cardBg,
                        unfocusedContainerColor = cardBg,
                        focusedBorderColor = goldColor,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    maxLines = 3
                )
                IconButton(
                    onClick = {
                        if (prompt.isNotBlank() && !isLoading) {
                            chatViewModel.sendMessage(prompt)
                            prompt = ""
                        }
                    },
                    modifier = Modifier.testTag("ai_send_button"),
                    enabled = !isLoading && prompt.isNotBlank()
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Send Prompt", tint = goldColor)
                }
            }
        }
    }
}
