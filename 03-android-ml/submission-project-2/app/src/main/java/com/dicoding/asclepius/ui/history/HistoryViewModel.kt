package com.dicoding.asclepius.ui.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.DataRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class HistoryViewModel(private val dataRepository: DataRepository) : ViewModel() {
    init {
        getHistory()
    }
    fun getHistory(): LiveData<List<HistoryEntity>> = dataRepository.getHistory()
}