package com.dicoding.dicodingstory.di

import android.content.Context
import com.dicoding.dicodingstory.data.UserRepository
import com.dicoding.dicodingstory.data.pref.UserPreference
import com.dicoding.dicodingstory.data.pref.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}