
package com.example.stockpredictor.api.models

data class AccountAccuracy(
    val username: String,
    val total_predictions: Int,
    val high_confidence: Int,
    val accuracy: Float
)
