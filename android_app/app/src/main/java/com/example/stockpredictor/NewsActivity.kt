
package com.example.stockpredictor

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.stockpredictor.api.models.PredictionRequest
import com.example.stockpredictor.api.models.PredictionResponse
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface PredictionService {
    @POST("predict")
    fun predict(@Body request: PredictionRequest): Call<PredictionResponse>
}

class NewsActivity : AppCompatActivity() {

    private lateinit var api: PredictionService
    private lateinit var output: TextView
    private var autoTrigger = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val titleInput = findViewById<EditText>(R.id.titleInput)
        val contentInput = findViewById<EditText>(R.id.contentInput)
        val tickerInput = findViewById<EditText>(R.id.tickerInput)
        val predictButton = findViewById<Button>(R.id.predictButton)
        output = findViewById(R.id.predictionOutput)

        val retrofit = RetrofitClient.instance
        api = retrofit.create(PredictionService::class.java)

        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val useGpt = SettingsManager.getUseGpt(this)

        val watcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (autoTrigger) {
                    val title = titleInput.text.toString()
                    val content = contentInput.text.toString()
                    val ticker = tickerInput.text.toString()
                    if (title.isNotEmpty() && content.isNotEmpty() && ticker.isNotEmpty()) {
                        triggerPrediction(title, content, ticker, useGpt)
                    }
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        titleInput.addTextChangedListener(watcher)
        contentInput.addTextChangedListener(watcher)
        tickerInput.addTextChangedListener(watcher)

        predictButton.setOnClickListener {
            val title = titleInput.text.toString()
            val content = contentInput.text.toString()
            val ticker = tickerInput.text.toString()
            if (title.isNotEmpty() && content.isNotEmpty() && ticker.isNotEmpty()) {
                triggerPrediction(title, content, ticker, useGpt)
            } else {
                output.text = "Fill in all fields."
            }
        }

        predictButton.setOnLongClickListener {
            autoTrigger = !autoTrigger
            Toast.makeText(this, "Auto-predict ${if (autoTrigger) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun triggerPrediction(title: String, content: String, ticker: String, useGpt: Boolean) {
        val request = PredictionRequest(title, content, ticker, useGpt)
        api.predict(request).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(call: Call<PredictionResponse>, response: Response<PredictionResponse>) {
                val body = response.body()
                if (body != null) {
                    output.text = "Sentiment: ${body.sentiment}
Prediction: ${body.prediction}
Confidence: ${body.confidence}"
                } else {
                    output.text = "Failed to get prediction."
                }
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                output.text = "Error: ${t.message}"
            }
        })
    }
}
