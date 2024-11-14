package com.dicoding.dicodingstory.view.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingstory.data.DataRepository
import com.dicoding.dicodingstory.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: DataRepository) : ViewModel() {
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
    fun login(email: String, password: String) = repository.login(email, password)
}