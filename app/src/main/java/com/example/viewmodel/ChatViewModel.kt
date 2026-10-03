package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.ai.Content
import com.example.ai.GeminiClient
import com.example.ai.GenerateContentRequest
import com.example.ai.Part
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatMessageState(
    val text: String,
    val isUser: Boolean
)

class ChatViewModel : ViewModel() {
    private val _messages = MutableStateFlow<List<ChatMessageState>>(
        listOf(
            ChatMessageState("سلام! من دستیار هوشمند لجستیک NDN هستم. می‌توانید سوالات خود را درباره وضعیت بسته‌ها، آدرس هاب‌ها یا بهینه‌سازی مسیرها بپرسید.", false)
        )
    )
    val messages: StateFlow<List<ChatMessageState>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun sendMessage(prompt: String) {
        if (prompt.isBlank()) return

        val userMessage = ChatMessageState(prompt, true)
        _messages.update { it + userMessage }
        _isLoading.value = true

        viewModelScope.launch {
            try {
                val apiKey = BuildConfig.GEMINI_API_KEY.ifBlank { "AIzaSy_PlaceholderKey" }
                val currentHistory = _messages.value.map { m ->
                    Content(
                        role = if (m.isUser) "user" else "model",
                        parts = listOf(Part(text = m.text))
                    )
                }

                val request = GenerateContentRequest(
                    contents = currentHistory,
                    systemInstruction = Content(
                        parts = listOf(Part(text = "You are an expert logistics assistant for NDN Delivery Network in Iran. Answer user questions about delivery statuses, tracking IDs, and PUDO hub locations accurately and politely in Persian. Ground your answers in realistic courier operations."))
                    )
                )

                val responseText = if (apiKey.startsWith("AIzaSy_PlaceholderKey")) {
                    kotlinx.coroutines.delay(1000)
                    "پاسخ شبیه‌سازی‌شده: وضعیت بسته شما عادی است و به سمت هاب مقصد در حال حرکت است. (لطفاً کلید واقعی Gemini API را در فایل .env تنظیم کنید)"
                } else {
                    val response = GeminiClient.service.generateContent(apiKey, request)
                    response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "پاسخی دریافت نشد."
                }

                _messages.update { it + ChatMessageState(responseText, false) }
            } catch (e: Exception) {
                _messages.update { it + ChatMessageState("خطا در ارتباط با هوش مصنوعی: ${e.message}", false) }
            } finally {
                _isLoading.value = false
            }
        }
    }
}
