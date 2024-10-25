package com.dicoding.dicodingevent.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.remote.response.EventResponse
import com.dicoding.dicodingevent.data.remote.response.ListEventsItem
import com.dicoding.dicodingevent.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class HomeViewModel : ViewModel() {
    // LiveData untuk menyimpan upcoming event list
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    // LiveData untuk menyimpan finished event list
    private val _finishedEvents = MutableLiveData<List<ListEventsItem>>()
    val finishedEvents: LiveData<List<ListEventsItem>> = _finishedEvents

    // LiveData untuk menyimpan status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // menyimpan live data toast
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    companion object{
        private const val TAG = "HomeViewModel"
    }

    init {
        fetchEvents(1)
        fetchEvents(0)
    }

    private fun fetchEvents(active: Int) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListEvents(active)
        client.enqueue(object : retrofit2.Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val events = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                    if (active == 1) {
                        // Set upcoming events
                        _upcomingEvents.value = events.take(5)
                    } else {
                        // Set finished events
                        _finishedEvents.value = events.take(5)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _toastMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
                _toastMessage.value = "Error: ${t.message.toString()}"
            }
        })
    }
}
