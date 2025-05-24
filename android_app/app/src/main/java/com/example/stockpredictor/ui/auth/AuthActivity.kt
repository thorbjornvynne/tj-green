
package com.example.stockpredictor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import com.example.stockpredictor.api.models.UserCreate
import com.example.stockpredictor.api.models.Token
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.Executor

interface AuthService {
    @POST("register")
    fun register(@Body user: UserCreate): Call<Token>

    @POST("login")
    fun login(@Body user: UserCreate): Call<Token>
}

class AuthActivity : AppCompatActivity() {

    private lateinit var api: AuthService
    private lateinit var executor: Executor
    private lateinit var prompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val username = findViewById<EditText>(R.id.usernameInput)
        val password = findViewById<EditText>(R.id.passwordInput)
        val login = findViewById<Button>(R.id.loginButton)
        val register = findViewById<Button>(R.id.registerButton)
        val bio = findViewById<Button>(R.id.biometricButton)

        val retrofit = RetrofitClient.instance
        api = retrofit.create(AuthService::class.java)

        login.setOnClickListener {
            val user = UserCreate(username.text.toString(), password.text.toString())
            api.login(user).enqueue(tokenCallback())
        }

        register.setOnClickListener {
            val user = UserCreate(username.text.toString(), password.text.toString())
            api.register(user).enqueue(tokenCallback())
        }

        executor = ContextCompat.getMainExecutor(this)
        prompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                val token = getSharedPreferences("auth", Context.MODE_PRIVATE)
                    .getString("token", null)
                if (token != null) {
                    startActivity(Intent(this@AuthActivity, DashboardActivity::class.java))
                }
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Login")
            .setSubtitle("Log in using fingerprint or face")
            .setNegativeButtonText("Cancel")
            .build()

        val manager = BiometricManager.from(this)
        if (manager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            bio.setOnClickListener {
                prompt.authenticate(promptInfo)
            }
        } else {
            bio.isEnabled = false
        }
    }

    private fun tokenCallback(): Callback<Token> {
        return object : Callback<Token> {
            override fun onResponse(call: Call<Token>, response: Response<Token>) {
                val token = response.body()?.access_token
                if (token != null) {
                    val prefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
                    SettingsManager.setToken(this, token)
                    startActivity(Intent(this@AuthActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this@AuthActivity, "Login failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Token>, t: Throwable) {
                Toast.makeText(this@AuthActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
