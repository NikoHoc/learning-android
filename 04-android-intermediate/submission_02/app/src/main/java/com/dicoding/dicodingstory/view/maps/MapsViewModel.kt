package com.dicoding.dicodingstory.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.dicodingstory.data.Result
import com.dicoding.dicodingstory.data.DataRepository
import com.dicoding.dicodingstory.data.pref.UserModel
import com.dicoding.dicodingstory.data.remote.response.ListStoryItem


class MapsViewModel (private val repository: DataRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

//    fun getStoriesWithLocation() = repository.getStoriesWithLocation()
    fun getStoriesWithLocation(): LiveData<Result<List<ListStoryItem>>> {
        return repository.getStoriesWithLocation()
    }
}