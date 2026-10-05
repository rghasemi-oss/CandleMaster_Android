package com.example.data.remote

import android.content.Context
import android.content.SharedPreferences

class ConfigPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("ndn_config_prefs", Context.MODE_PRIVATE)

    var renderBackendUrl: String
        get() = prefs.getString("render_backend_url", "") ?: ""
        set(value) = prefs.edit().putString("render_backend_url", value).apply()
}
