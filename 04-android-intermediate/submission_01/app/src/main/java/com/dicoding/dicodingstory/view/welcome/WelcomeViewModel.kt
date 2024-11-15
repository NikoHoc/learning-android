package com.dicoding.dicodingstory.view.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.dicodingstory.data.DataRepository

class WelcomeViewModel(private val dataRepository: DataRepository): ViewModel() {
    fun getSession() = dataRepository.getSession().asLiveData()
}