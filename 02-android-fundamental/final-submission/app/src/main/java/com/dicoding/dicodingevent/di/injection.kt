package com.dicoding.dicodingevent.di

import android.content.Context
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.local.room.EventDatabase
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig
import com.dicoding.dicodingevent.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val upcomingDao = database.upcomingEventDao()

        return EventRepository.getInstance(apiService, upcomingDao)
    }
}