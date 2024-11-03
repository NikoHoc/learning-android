package com.dicoding.asclepius.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.dicoding.asclepius.data.DataRepository
import com.dicoding.asclepius.data.local.entity.HistoryEntity

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun getCancerArticles() = dataRepository.getCancerArticle()
}