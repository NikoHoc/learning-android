package com.dicoding.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.Result
import com.dicoding.dicodingevent.data.local.entity.FinishedEventEntity

class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {
    init {
        getFinishedEvent()
    }
    fun getFinishedEvent(): LiveData<Result<List<FinishedEventEntity>>> = eventRepository.getFinishedEvent( 0)
    fun searchFinishedEvents(query: String): LiveData<Result<List<FinishedEventEntity>>> = eventRepository.searchFinishedEvents(query)

}
