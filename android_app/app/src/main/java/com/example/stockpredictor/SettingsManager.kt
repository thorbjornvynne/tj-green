
package com.example.stockpredictor

import android.content.Context
import com.example.stockpredictor.AppConfig.PREFS_APP
import com.example.stockpredictor.AppConfig.PREFS_AUTH

object SettingsManager {

    fun setUseGpt(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS_APP, Context.MODE_PRIVATE)
            .edit().putBoolean("use_gpt", enabled).apply()
    }

    fun getUseGpt(context: Context): Boolean {
        return context.getSharedPreferences(PREFS_APP, Context.MODE_PRIVATE)
            .getBoolean("use_gpt", false)
    }

    fun setInterval(context: Context, minutes: Int) {
        context.getSharedPreferences(PREFS_APP, Context.MODE_PRIVATE)
            .edit().putInt("scrape_interval", minutes).apply()
    }

    fun getInterval(context: Context): Int {
        return context.getSharedPreferences(PREFS_APP, Context.MODE_PRIVATE)
            .getInt("scrape_interval", 5)
    }

    fun setToken(context: Context, token: String) {
        context.getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE)
            .edit().putString("token", token).apply()
    }

    fun getToken(context: Context): String? {
        return context.getSharedPreferences(PREFS_AUTH, Context.MODE_PRIVATE)
            .getString("token", null)
    }
}
