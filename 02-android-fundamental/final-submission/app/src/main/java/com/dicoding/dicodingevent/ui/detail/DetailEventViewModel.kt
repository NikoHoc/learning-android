package com.dicoding.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.local.entity.FavoriteEventEntity
import kotlinx.coroutines.launch


class DetailEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getEventDetail(id: Int) = eventRepository.getDetailEvent(id)

    fun isFavorite(eventId: Int): LiveData<Boolean> {
        return eventRepository.isEventFavorite(eventId)
    }

    fun saveFavoriteEvent(event: FavoriteEventEntity) {
        viewModelScope.launch {
            eventRepository.saveFavoriteEvent(event)
        }
    }

    fun deleteFavoriteNews(id: Int) {
        viewModelScope.launch {
            eventRepository.removeFavoriteEvent(id)
        }
    }

}
