package com.dicoding.asclepius.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.DataRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class HistoryViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun getHistory(): LiveData<List<HistoryEntity>> = liveData {
        val historyList = dataRepository.getHistory()
        emit(historyList)
    }
}