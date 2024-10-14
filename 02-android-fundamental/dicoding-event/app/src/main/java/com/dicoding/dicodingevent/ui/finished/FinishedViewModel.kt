package com.dicoding.dicodingevent.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dicodingevent.data.response.EventResponse
import com.dicoding.dicodingevent.data.response.ListEventsItem
import com.dicoding.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Response

class FinishedViewModel : ViewModel() {
    // LiveData untuk menyimpan event list
    private val _events = MutableLiveData<List<ListEventsItem>>()
    val events: LiveData<List<ListEventsItem>> = _events

    // LiveData untuk menyimpan status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // menyimpan live data toast
    private val _toastMessage = MutableLiveData<String>()
    val toastMessage: LiveData<String> = _toastMessage

    companion object{
        private const val TAG = "UpcomingViewModel"
    }

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getListEvents(0) // active 0 = event sudah selesai
        client.enqueue(object : retrofit2.Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _events.value = response.body()?.listEvents?.filterNotNull() ?: emptyList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    fun searchEvents(query: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearchEvent(0, query) // 0 for finished events
        client.enqueue(object : retrofit2.Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    _events.value = response.body()?.listEvents?.filterNotNull() ?: emptyList()
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
