package com.example.data.remote

import com.example.BuildConfig

object SupabaseClientProvider {
    val supabaseUrl: String
        get() = if (BuildConfig.SUPABASE_URL.isNotBlank()) BuildConfig.SUPABASE_URL else "https://placeholder.supabase.co"

    val supabaseAnonKey: String
        get() = if (BuildConfig.SUPABASE_ANON_KEY.isNotBlank()) BuildConfig.SUPABASE_ANON_KEY else "placeholder-key"

    val authManager: SupabaseAuthManager by lazy {
        val authService = SupabaseClient.apiService.let {
            // Reusing Retrofit auth service
            object : SupabaseAuthService {
                override suspend fun signUp(apiKey: String, request: AuthRequest) = SupabaseClient.apiService.let {
                    // delegate or direct client implementation
                    it.getDeliveries(apiKey, "") // placeholder delegate
                    retrofit2.Response.success(AuthResponse(null, null, null))
                }
                override suspend fun signIn(apiKey: String, request: AuthRequest) = retrofit2.Response.success(AuthResponse(null, null, null))
            }
        }
        SupabaseAuthManager(authService, supabaseAnonKey)
    }

    init {
        // Initialization logging or verification of environment variables from .env
        android.util.Log.d("SupabaseClientProvider", "Initialized with URL: $supabaseUrl")
    }
}
