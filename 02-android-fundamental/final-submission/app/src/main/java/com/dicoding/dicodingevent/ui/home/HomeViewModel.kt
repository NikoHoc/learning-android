package com.dicoding.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity
import com.dicoding.dicodingevent.data.remote.response.EventResponse
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {
    init {
        getUpcomingEvent()
        getFinishedEvent()
    }

    fun getUpcomingEvent(): LiveData<Result<List<UpcomingEventEntity>>> = eventRepository.getUpcomingEvent( 1)
    fun getFinishedEvent(): LiveData<Result<List<UpcomingEventEntity>>> = eventRepository.getUpcomingEvent( 0)
}
