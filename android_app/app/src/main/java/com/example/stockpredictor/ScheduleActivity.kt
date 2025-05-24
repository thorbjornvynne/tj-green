
package com.example.stockpredictor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class ScheduleActivity : AppCompatActivity() {

    private val intervals = listOf("Every 5 minutes", "Every 15 minutes", "Every 30 minutes", "Hourly")
    private val values = listOf(5, 15, 30, 60)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule)

        val spinner = findViewById<Spinner>(R.id.intervalSpinner)
        val saveBtn = findViewById<Button>(R.id.saveButton)
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, intervals)
        spinner.adapter = adapter

        val current = prefs.getInt("scrape_interval", 5)
        spinner.setSelection(values.indexOf(current))

        saveBtn.setOnClickListener {
            val selected = spinner.selectedItemPosition
            SettingsManager.setInterval(this, values[selected])
            Toast.makeText(this, "Saved: ${intervals[selected]}", Toast.LENGTH_SHORT).show()
        }
    }
}
