package com.dicoding.dicodingevent.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.local.entity.FinishedEventEntity
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity
import com.dicoding.dicodingevent.data.remote.response.EventResponse
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {
    init {
        getFinishedEvent()
    }
    fun getFinishedEvent(): LiveData<Result<List<FinishedEventEntity>>> = eventRepository.getFinishedEvent( 0)
    fun searchFinishedEvents(query: String): LiveData<Result<List<FinishedEventEntity>>> = eventRepository.searchFinishedEvents(query)

}
