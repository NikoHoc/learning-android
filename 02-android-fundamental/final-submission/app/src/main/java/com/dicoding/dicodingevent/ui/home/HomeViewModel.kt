package com.dicoding.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.local.entity.FinishedEventEntity
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {

    init {
        getUpcomingEvent()
        getFinishedEvent()
    }
    fun getUpcomingEvent(): LiveData<Result<List<UpcomingEventEntity>>> = eventRepository.getUpcomingEvent( 1)
    fun getFinishedEvent(): LiveData<Result<List<FinishedEventEntity>>> = eventRepository.getFinishedEvent( 0)
}
