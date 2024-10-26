package com.dicoding.dicodingevent.di

import android.content.Context
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.local.room.EventDatabase
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = EventDatabase.getInstance(context)
        val upcomingDao = database.upcomingEventDao()
        val finishedEventDao = database.finishedEventDao()
        val favoriteEventDao = database.favoriteEventDao()

        return EventRepository.getInstance(apiService, upcomingDao, finishedEventDao, favoriteEventDao)
    }
}