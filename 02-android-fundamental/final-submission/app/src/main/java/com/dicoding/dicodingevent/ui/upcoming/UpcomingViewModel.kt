package com.dicoding.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity

class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {
    init {
        getUpcomingEvent()
    }
    fun getUpcomingEvent(): LiveData<Result<List<UpcomingEventEntity>>> = eventRepository.getUpcomingEvent( 1)

}
