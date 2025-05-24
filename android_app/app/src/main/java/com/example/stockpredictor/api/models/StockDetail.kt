
package com.example.stockpredictor.api.models

data class StockDetail(
    val ticker: String,
    val name: String,
    val reliability_score: Float,
    val total_predictions: Int,
    val high_confidence: Int,
    val history: List<PredictionItem>
)

data class PredictionItem(
    val sentiment: String,
    val prediction: String,
    val confidence: Float,
    val time: String
)
