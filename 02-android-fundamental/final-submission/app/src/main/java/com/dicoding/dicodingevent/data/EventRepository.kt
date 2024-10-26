package com.dicoding.dicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity
import com.dicoding.dicodingevent.data.local.room.UpcomingEventDao
import com.dicoding.dicodingevent.data.remote.retrofit.ApiService

class EventRepository private constructor(
    private val apiService: ApiService,
    private val upcomingEventDao: UpcomingEventDao
) {

    fun getUpcomingEvent(active: Int? = -1) : LiveData<Result<List<UpcomingEventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getListEvents(active)
            val listEvents = response.listEvents
            val newListEvent =listEvents?.map { event ->
                UpcomingEventEntity(
                    event?.id,
                    event?.name,
                    event?.mediaCover,
                    event?.imageLogo,
                    event?.category,
                    event?.registrants,
                    event?.quota,
                    event?.cityName,
                    event?.summary,
                    event?.description,
                    event?.beginTime,
                    event?.endTime,
                    event?.link
                )
            }
            upcomingEventDao.deleteAllUpcomingEvent()
            upcomingEventDao.insertUpcomingEvent(newListEvent!!)
        } catch (e: Exception) {
            Log.d("EventRepository", "getUpcomingEvent: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<UpcomingEventEntity>>> = upcomingEventDao.getUpcomingEvent().map { Result.Success(it) }
        emitSource(localData)
    }
    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            upcomingEventDao: UpcomingEventDao,

        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(
                    apiService,
                    upcomingEventDao,
                )
            }.also { instance = it }
    }
}