package com.dicoding.dicodingevent.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.EventRepository
import com.dicoding.dicodingevent.data.remote.response.DetailEventResponse


class DetailEventViewModel(private val eventRepository: EventRepository) : ViewModel() {
    fun getEventDetail(id: Int) = eventRepository.getDetailEvent(id)
}
