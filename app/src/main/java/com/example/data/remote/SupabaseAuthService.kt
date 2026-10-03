package com.example.data.remote

import retrofit2.Response
import retrofit2.http.*

data class AuthRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val access_token: String?,
    val refresh_token: String?,
    val user: SupabaseUser?
)

data class SupabaseUser(
    val id: String,
    val email: String?
)

interface SupabaseAuthService {
    @POST("auth/v1/signup")
    suspend fun signUp(
        @Header("apikey") apiKey: String,
        @Body request: AuthRequest
    ): Response<AuthResponse>

    @POST("auth/v1/token?grant_type=password")
    suspend fun signIn(
        @Header("apikey") apiKey: String,
        @Body request: AuthRequest
    ): Response<AuthResponse>
}
