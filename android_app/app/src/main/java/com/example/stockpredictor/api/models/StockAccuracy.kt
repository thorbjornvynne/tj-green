
package com.example.stockpredictor.api.models

data class StockAccuracy(
    val ticker: String,
    val name: String,
    val total_predictions: Int,
    val high_confidence: Int,
    val reliability_score: Float
)
