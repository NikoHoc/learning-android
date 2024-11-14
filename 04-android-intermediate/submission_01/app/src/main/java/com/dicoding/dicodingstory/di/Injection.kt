package com.dicoding.dicodingstory.di

import android.content.Context
import com.dicoding.dicodingstory.data.UserRepository
import com.dicoding.dicodingstory.data.pref.UserPreference
import com.dicoding.dicodingstory.data.pref.dataStore
import com.dicoding.dicodingstory.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(apiService, pref)
    }
}