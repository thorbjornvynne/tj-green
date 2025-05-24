
package com.example.stockpredictor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockpredictor.adapters.StockAccuracyAdapter
import com.example.stockpredictor.api.models.StockAccuracy
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class StockAccuracyActivity : AppCompatActivity() {

    interface StockService {
        @GET("stocks")
        fun getAll(): Call<List<StockAccuracy>>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stock_accuracy)

        val recycler = findViewById<RecyclerView>(R.id.accuracyList)
        recycler.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(StockService::class.java)

        api.getAll().enqueue(object : Callback<List<StockAccuracy>> {
            override fun onResponse(call: Call<List<StockAccuracy>>, response: Response<List<StockAccuracy>>) {
                val stocks = response.body() ?: return
                recycler.adapter = StockAccuracyAdapter(this@StockAccuracyActivity, stocks)
            }

            override fun onFailure(call: Call<List<StockAccuracy>>, t: Throwable) {
                Toast.makeText(this@StockAccuracyActivity, "Failed to load stock data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
