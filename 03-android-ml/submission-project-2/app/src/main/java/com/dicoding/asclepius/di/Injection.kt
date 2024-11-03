package com.dicoding.asclepius.di

import android.content.Context
import com.dicoding.asclepius.data.DataRepository
import com.dicoding.asclepius.data.local.room.AsclepiusDatabase
import com.dicoding.asclepius.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): DataRepository {
        val apiService = ApiConfig.getApiService()
        val database = AsclepiusDatabase.getInstance(context)
        val historyDao = database.historyDao()
        val articlesDao = database.articlesDao()

        return DataRepository.getInstance(apiService, historyDao, articlesDao)
    }
}