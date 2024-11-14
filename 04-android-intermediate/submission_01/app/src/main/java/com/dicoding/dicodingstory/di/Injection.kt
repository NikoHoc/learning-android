package com.dicoding.dicodingstory.di

import android.content.Context
import com.dicoding.dicodingstory.data.DataRepository
import com.dicoding.dicodingstory.data.pref.UserPreference
import com.dicoding.dicodingstory.data.pref.dataStore
import com.dicoding.dicodingstory.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return DataRepository.getInstance(apiService, pref)
    }
}