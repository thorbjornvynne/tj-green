
package com.example.stockpredictor

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        prefs = getSharedPreferences("app_settings", MODE_PRIVATE)

        findViewById<Button>(R.id.newsButton).setOnClickListener {
            startActivity(Intent(this, NewsActivity::class.java))
        }

        findViewById<Button>(R.id.accountsButton).setOnClickListener {
            startActivity(Intent(this, AccountsActivity::class.java))
        }

        findViewById<Button>(R.id.scheduleButton).setOnClickListener {
            startActivity(Intent(this, ScheduleActivity::class.java))
        }

        findViewById<Button>(R.id.stocksButton).setOnClickListener {
            startActivity(Intent(this, StocksActivity::class.java))
        }

        findViewById<Button>(R.id.themesButton).setOnClickListener {
            startActivity(Intent(this, ThemePreviewActivity::class.java))
        }

        val gptToggle = findViewById<Switch>(R.id.gptToggle)
        gptToggle.isChecked = SettingsManager.getUseGpt(this)
        gptToggle.setOnCheckedChangeListener { _, isChecked ->
            SettingsManager.setUseGpt(this, isChecked)
        }
    }
}
