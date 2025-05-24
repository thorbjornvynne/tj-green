package com.example.stockpredictor.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stockpredictor.data.repository.DashboardRepository

class DashboardViewModel : ViewModel() {
    private val repository = DashboardRepository()
    private val _summary = MutableLiveData<String>()
    val summary: LiveData<String> = _summary

    fun loadDashboardSummary() {
        _summary.value = repository.getDashboardSummary()
    }
}