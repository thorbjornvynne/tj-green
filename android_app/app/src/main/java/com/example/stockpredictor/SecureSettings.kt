
package com.example.stockpredictor

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.stockpredictor.AppConfig.PREFS_APP
import com.example.stockpredictor.AppConfig.PREFS_AUTH

object SecureSettings {

    private fun securePrefs(context: Context, name: String) =
        EncryptedSharedPreferences.create(
            context,
            name,
            MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun setToken(context: Context, token: String) {
        securePrefs(context, PREFS_AUTH).edit().putString("token", token).apply()
    }

    fun getToken(context: Context): String? {
        return securePrefs(context, PREFS_AUTH).getString("token", null)
    }
}
