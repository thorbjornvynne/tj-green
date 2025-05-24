
package com.example.stockpredictor

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.annotations.SerializedName
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class StockChartActivity : AppCompatActivity() {

    private lateinit var chart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_chart)
        chart = findViewById(R.id.stockChart)

        val ticker = intent.getStringExtra("ticker") ?: return

        val retrofit = Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(StockChartService::class.java)
        service.getChartData(ticker).enqueue(object : Callback<StockChartData> {
            override fun onResponse(call: Call<StockChartData>, response: Response<StockChartData>) {
                response.body()?.let { plotChart(it) }
            }

            override fun onFailure(call: Call<StockChartData>, t: Throwable) {}
        })
    }

    private fun plotChart(data: StockChartData) {
        val priceEntries = ArrayList<Entry>()
        val predictionEntries = ArrayList<Entry>()
        val labels = ArrayList<String>()
        val dots = mutableListOf<Entry>()

        data.price_series.forEachIndexed { i, pt ->
            priceEntries.add(Entry(i.toFloat(), pt.price.toFloat()))
            labels.add(pt.date)
        }

        data.predictions.forEach { pred ->
            val idx = labels.indexOf(pred.time.substring(0, 10))
            if (idx != -1) {
                dots.add(Entry(idx.toFloat(), priceEntries[idx].y))
            }
        }

        val priceSet = LineDataSet(priceEntries, "Stock Price")
        priceSet.color = Color.BLUE
        priceSet.setDrawCircles(false)
        priceSet.setDrawValues(false)

        val dotSet = LineDataSet(dots, "Predictions")
        dotSet.setDrawValues(false)
        dotSet.setDrawCircles(true)
        dotSet.circleRadius = 5f
        dotSet.setDrawCircleHole(false)
        dotSet.color = Color.GRAY
        dotSet.setDrawHighlightIndicators(false)
        dotSet.setColors(dots.mapIndexed { i, _ ->
            if (data.predictions[i].is_correct == true) Color.GREEN else Color.RED
        })

        val sets = ArrayList<ILineDataSet>()
        sets.add(priceSet)
        sets.add(dotSet)

        chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        chart.data = LineData(sets)
        chart.invalidate()
    }
}

data class StockChartData(
    @SerializedName("price_series") val price_series: List<PricePoint>,
    @SerializedName("predictions") val predictions: List<PredictionDot>
)

data class PricePoint(val date: String, val price: Double)
data class PredictionDot(val time: String, val prediction: String, val confidence: Float, val is_correct: Boolean?)

interface StockChartService {
    @GET("stocks/{ticker}/chart")
    fun getChartData(@Path("ticker") ticker: String): Call<StockChartData>
}
