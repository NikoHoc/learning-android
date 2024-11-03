package com.dicoding.asclepius.ui.home

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.DataRepository

class HomeViewModel(private val dataRepository: DataRepository) : ViewModel() {
    fun getCancerArticles() = dataRepository.getCancerArticle()
}