package com.dicoding.dicodingstory.view.signup


import androidx.lifecycle.ViewModel
import com.dicoding.dicodingstory.data.UserRepository

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    fun registerUser(name: String, email: String, password: String) = repository.registerUser(name, email, password)

}