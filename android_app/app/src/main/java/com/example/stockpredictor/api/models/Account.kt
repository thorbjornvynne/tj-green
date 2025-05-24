
package com.example.stockpredictor.api.models

data class AccountCreate(val username: String)
data class AccountInfo(val id: Int, val username: String, val enabled: Boolean)
