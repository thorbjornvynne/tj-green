
package com.example.stockpredictor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockpredictor.adapters.StockAdapter
import com.example.stockpredictor.api.models.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

interface StockService {
    @GET("stocks")
    fun getStocks(): Call<List<StockInfo>>

    @POST("stocks")
    fun addStock(@Body stock: StockCreate): Call<StockInfo>
}

class StocksActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: StockAdapter
    private lateinit var api: StockService
    private lateinit var stockList: List<StockInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stocks)

        recyclerView = findViewById(R.id.stockList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val tickerInput = findViewById<EditText>(R.id.tickerInput)
        val nameInput = findViewById<EditText>(R.id.nameInput)
        val addButton = findViewById<Button>(R.id.addStockButton)

        val retrofit = RetrofitClient.instance
        api = retrofit.create(StockService::class.java)

        addButton.setOnClickListener {
            val ticker = tickerInput.text.toString().uppercase()
            val name = nameInput.text.toString()
            if (ticker.isNotEmpty() && name.isNotEmpty()) {
                api.addStock(StockCreate(ticker, name)).enqueue(object : Callback<StockInfo> {
                    override fun onResponse(call: Call<StockInfo>, response: Response<StockInfo>) {
                        refreshList()
                    }

                    override fun onFailure(call: Call<StockInfo>, t: Throwable) {
                        Toast.makeText(this@StocksActivity, "Add failed", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        refreshList()
    }

    private fun refreshList() {
        api.getStocks().enqueue(object : Callback<List<StockInfo>> {
            override fun onResponse(call: Call<List<StockInfo>>, response: Response<List<StockInfo>>) {
                stockList = response.body() ?: listOf()
                adapter = StockAdapter(this@StocksActivity, stockList)
                recyclerView.adapter = adapter
            }

            override fun onFailure(call: Call<List<StockInfo>>, t: Throwable) {
                Toast.makeText(this@StocksActivity, "Failed to load stocks", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
