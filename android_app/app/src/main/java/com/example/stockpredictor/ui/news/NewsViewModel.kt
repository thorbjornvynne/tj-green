package com.example.stockpredictor.ui.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stockpredictor.data.repository.NewsRepository

class NewsViewModel : ViewModel() {
    private val repository = NewsRepository()
    private val _news = MutableLiveData<List<String>>()
    val news: LiveData<List<String>> = _news

    fun fetchNews() {
        _news.value = repository.getPredictedNews()
    }
}