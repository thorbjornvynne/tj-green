package com.example.stockpredictor.ui.stocks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stockpredictor.data.repository.StocksRepository

class StocksViewModel : ViewModel() {
    private val repository = StocksRepository()
    private val _stocks = MutableLiveData<List<String>>()  // Replace String with proper Stock model
    val stocks: LiveData<List<String>> = _stocks

    fun fetchStocks() {
        _stocks.value = repository.getMonitoredStocks()
    }
}