
package com.example.stockpredictor

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockpredictor.adapters.AccountAccuracyAdapter
import com.example.stockpredictor.api.models.AccountAccuracy
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class AccountAccuracyActivity : AppCompatActivity() {

    interface AccountService {
        @GET("accounts/accuracy/all")
        fun getAll(): Call<List<AccountAccuracy>>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_accuracy)

        val recycler = findViewById<RecyclerView>(R.id.accountAccuracyList)
        recycler.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
            .baseUrl(AppConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(AccountService::class.java)

        api.getAll().enqueue(object : Callback<List<AccountAccuracy>> {
            override fun onResponse(call: Call<List<AccountAccuracy>>, response: Response<List<AccountAccuracy>>) {
                val accounts = response.body() ?: return
                recycler.adapter = AccountAccuracyAdapter(this@AccountAccuracyActivity, accounts)
            }

            override fun onFailure(call: Call<List<AccountAccuracy>>, t: Throwable) {
                Toast.makeText(this@AccountAccuracyActivity, "Failed to load account data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
