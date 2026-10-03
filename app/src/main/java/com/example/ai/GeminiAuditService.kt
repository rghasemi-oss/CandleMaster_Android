package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class GeminiAuditService {
    private val client = OkHttpClient()

    private fun getApiKey(): String {
        return try {
            val field = BuildConfig::class.java.getField("GEMINI_API_KEY")
            field.get(null) as? String ?: ""
        } catch (e: Exception) {
            ""
        }
    }

    suspend fun analyzeAuditCompliance(prompt: String): String = withContext(Dispatchers.IO) {
        val apiKey = getApiKey()
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "AI Compliance Analysis (Simulated Fallback - Please configure GEMINI_API_KEY in Secrets if connected):\n" +
                    "Based on standard regulatory frameworks (SOC 2, ISO 27001, and MiCA custody guidelines), " +
                    "ensure all cold storage multi-sig keys are rotated quarterly, attestation reports are verified by independent auditors, " +
                    "and automated workmanager sync checks are monitored continuously for any anomalies regarding: $prompt"
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"
            val jsonBody = JSONObject().apply {
                put("contents", org.json.JSONArray().put(
                    JSONObject().put("parts", org.json.JSONArray().put(
                        JSONObject().put("text", "You are an expert financial custody and compliance auditor. Analyze the following query and provide professional, rigorous, and actionable guidance:\n\n$prompt")
                    ))
                ))
            }

            val body = jsonBody.toString().toRequestBody("application/json; charset=utf-8".toMediaType())
            val request = Request.Builder().url(url).post(body).build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("Unexpected code $response")
                val responseBody = response.body?.string() ?: return@withContext "Empty response"
                val jsonResponse = JSONObject(responseBody)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "No text found")
                    }
                }
                return@withContext "Could not parse AI response."
            }
        } catch (e: Exception) {
            "Error calling Gemini API: ${e.localizedMessage}"
        }
    }
}
