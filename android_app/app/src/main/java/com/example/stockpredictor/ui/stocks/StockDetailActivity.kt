
package com.example.stockpredictor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.stockpredictor.api.models.StockDetail
import com.example.stockpredictor.api.models.PredictionItem
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface StockDetailService {
    @GET("stocks/{ticker}/history")
    fun getHistory(@Path("ticker") ticker: String): Call<StockDetail>
}

class StockDetailActivity : AppCompatActivity() {

    private lateinit var stockService: StockDetailService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_detail)

        val ticker = intent.getStringExtra("ticker") ?: return
        val header = findViewById<TextView>(R.id.stockHeader)
        val reliability = findViewById<TextView>(R.id.reliabilityScore)
        val container = findViewById<LinearLayout>(R.id.historyContainer)

        val retrofit = RetrofitClient.instance
        stockService = retrofit.create(StockDetailService::class.java)

        stockService.getHistory(ticker).enqueue(object : Callback<StockDetail> {
            override fun onResponse(call: Call<StockDetail>, response: Response<StockDetail>) {
                val data = response.body()
                if (data != null) {
                    header.text = "${data.name} (${data.ticker})"
                    
        val chartBtn = findViewById<Button>(R.id.viewChartButton)
        chartBtn.setOnClickListener {
            val intent = Intent(this, StockChartActivity::class.java)
            intent.putExtra("ticker", ticker)
            startActivity(intent)
        }
    
        reliability.text = "Reliability: ${data.reliability_score * 100}%"
                    data.history.forEach { pred ->
                        val view = TextView(this@StockDetailActivity)
                        view.text = "- ${pred.time}: ${pred.sentiment}, ${pred.prediction} (${pred.confidence})"
                        container.addView(view)
                    }
                }
            }

            override fun onFailure(call: Call<StockDetail>, t: Throwable) {
                Toast.makeText(this@StockDetailActivity, "Failed to load history", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
