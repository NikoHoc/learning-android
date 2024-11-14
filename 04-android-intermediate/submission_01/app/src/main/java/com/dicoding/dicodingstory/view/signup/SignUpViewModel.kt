package com.dicoding.dicodingstory.view.signup


import androidx.lifecycle.ViewModel
import com.dicoding.dicodingstory.data.DataRepository

class SignUpViewModel(private val repository: DataRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = repository.registerUser(name, email, password)

}