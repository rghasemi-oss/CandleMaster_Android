package com.example.data.remote

import android.content.Context
import android.content.SharedPreferences

class ConfigPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("ndn_config_prefs", Context.MODE_PRIVATE)

    var supabaseUrl: String
        get() = prefs.getString("supabase_url", "") ?: ""
        set(value) = prefs.edit().putString("supabase_url", value).apply()

    var supabaseKey: String
        get() = prefs.getString("supabase_key", "") ?: ""
        set(value) = prefs.edit().putString("supabase_key", value).apply()

    var renderBackendUrl: String
        get() = prefs.getString("render_backend_url", "") ?: ""
        set(value) = prefs.edit().putString("render_backend_url", value).apply()

    fun isConfigured(): Boolean {
        return supabaseUrl.isNotBlank() && supabaseKey.isNotBlank()
    }
}
