package com.dicoding.dicodingstory.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingstory.data.DataRepository
import com.dicoding.dicodingstory.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: DataRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getStories() = repository.getStories();
}