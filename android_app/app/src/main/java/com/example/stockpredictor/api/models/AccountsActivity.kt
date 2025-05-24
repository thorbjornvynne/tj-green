
package com.example.stockpredictor

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockpredictor.adapters.AccountAdapter
import com.example.stockpredictor.api.models.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface AccountService {
    @GET("accounts")
    fun getAccounts(): Call<List<AccountInfo>>

    @POST("accounts")
    fun addAccount(@Body account: AccountCreate): Call<AccountInfo>

    @PATCH("accounts/{username}")
    fun toggleAccount(@Path("username") username: String, @Body enabled: Boolean): Call<Void>
}

class AccountsActivity : AppCompatActivity() {

    private lateinit var accountList: RecyclerView
    private lateinit var adapter: AccountAdapter
    private lateinit var accountService: AccountService
    private var accounts: MutableList<AccountInfo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_accounts)

        val input = findViewById<EditText>(R.id.usernameInput)
        val addBtn = findViewById<Button>(R.id.addButton)
        accountList = findViewById(R.id.accountList)
        accountList.layoutManager = LinearLayoutManager(this)

        val retrofit = RetrofitClient.instance
        accountService = retrofit.create(AccountService::class.java)

        addBtn.setOnClickListener {
            val username = input.text.toString().trim()
            if (username.isNotEmpty()) {
                accountService.addAccount(AccountCreate(username)).enqueue(object : Callback<AccountInfo> {
                    override fun onResponse(call: Call<AccountInfo>, response: Response<AccountInfo>) {
                        loadAccounts()
                    }

                    override fun onFailure(call: Call<AccountInfo>, t: Throwable) {
                        Toast.makeText(this@AccountsActivity, "Failed to add account", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        loadAccounts()
    }

    private fun loadAccounts() {
        accountService.getAccounts().enqueue(object : Callback<List<AccountInfo>> {
            override fun onResponse(call: Call<List<AccountInfo>>, response: Response<List<AccountInfo>>) {
                accounts = response.body()?.toMutableList() ?: mutableListOf()
                adapter = AccountAdapter(this@AccountsActivity, accounts) { acc, enabled ->
                    toggleAccount(acc.username, enabled)
                }
                accountList.adapter = adapter
            }

            override fun onFailure(call: Call<List<AccountInfo>>, t: Throwable) {
                Toast.makeText(this@AccountsActivity, "Failed to load accounts", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun toggleAccount(username: String, enabled: Boolean) {
        accountService.toggleAccount(username, enabled).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                // optional toast
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@AccountsActivity, "Toggle failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
