package com.dicoding.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.local.entity.FavoriteEventEntity

class FavoriteEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    init {
        getFavoriteEvent()
    }
    fun getFavoriteEvent(): LiveData<List<FavoriteEventEntity>> = eventRepository.getFavoriteEvents()
}
