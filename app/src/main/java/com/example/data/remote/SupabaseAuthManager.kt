package com.example.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SupabaseAuthManager(
    private val authService: SupabaseAuthService,
    private val apiKey: String
) {
    suspend fun signUp(email: String, password: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authService.signUp(apiKey, AuthRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("خطا در ثبت‌نام: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signIn(email: String, password: String): Result<AuthResponse> = withContext(Dispatchers.IO) {
        try {
            val response = authService.signIn(apiKey, AuthRequest(email, password))
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("خطا در ورود: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Helper for generating social login URL (Google / GitHub)
    fun getSocialLoginUrl(provider: String, supabaseUrl: String): String {
        return "$supabaseUrl/auth/v1/authorize?provider=$provider"
    }
}
