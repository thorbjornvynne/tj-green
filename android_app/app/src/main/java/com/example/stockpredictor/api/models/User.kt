
package com.example.stockpredictor.api.models

data class UserCreate(val username: String, val password: String)

data class Token(val access_token: String, val token_type: String)
