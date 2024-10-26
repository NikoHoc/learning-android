package com.dicoding.dicodingevent.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.dicodingevent.data.local.entity.FavoriteEventEntity
import com.dicoding.dicodingevent.data.local.entity.FinishedEventEntity
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity
import com.dicoding.dicodingevent.data.local.room.FavoriteEventDao
import com.dicoding.dicodingevent.data.local.room.FinishedEventDao
import com.dicoding.dicodingevent.data.local.room.UpcomingEventDao
import com.dicoding.dicodingevent.data.remote.response.DetailEventResponse
import com.dicoding.dicodingevent.data.remote.retrofit.ApiService



class EventRepository private constructor(
    private val apiService: ApiService,
    private val upcomingEventDao: UpcomingEventDao,
    private val finishedEventDao: FinishedEventDao,
    private val favoriteEventDao: FavoriteEventDao
) {

    fun getUpcomingEvent(active: Int) : LiveData<Result<List<UpcomingEventEntity>>> = liveData {
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

    fun getFinishedEvent(active: Int) : LiveData<Result<List<FinishedEventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getListEvents(active)
            val listEvents = response.listEvents
            val newListEvent = listEvents?.map { event ->
                FinishedEventEntity(
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
            finishedEventDao.deleteAllFinishedEvent()
            finishedEventDao.insertFinishedEvent(newListEvent!!)
        } catch (e: Exception) {
            Log.d("EventRepository", "getFinishedEvent: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<FinishedEventEntity>>> = finishedEventDao.getFinishedEvent().map { Result.Success(it) }
        emitSource(localData)
    }

    fun searchFinishedEvents(query: String): LiveData<Result<List<FinishedEventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getSearchEvent(0, query)  // active = 0 for finished events
            val listEvents = response.listEvents
            val newListEvent = listEvents?.map { event ->
                FinishedEventEntity(
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
            finishedEventDao.deleteAllFinishedEvent()
            finishedEventDao.insertFinishedEvent(newListEvent!!)
        } catch (e: Exception) {
            Log.d("EventRepository", "searchFinishedEvents: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<FinishedEventEntity>>> = finishedEventDao.getFinishedEvent().map { Result.Success(it) }
        emitSource(localData)
    }

    fun getDetailEvent(id: Int): LiveData<Result<DetailEventResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getDetailEvent(id)
            emit(Result.Success(response))
        } catch (e: Exception) {
            Log.d("EventRepository", "getDetailEvent: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavoriteEvents(): LiveData<List<FavoriteEventEntity>> {
        return favoriteEventDao.getAllFavoriteEvents()
    }

    suspend fun saveFavoriteEvent(event: FavoriteEventEntity) {
        favoriteEventDao.insertFavoriteEvent(event)
    }

    suspend fun removeFavoriteEvent(eventId: Int) {
        favoriteEventDao.deleteFavoriteEvent(eventId)
    }

    fun isEventFavorite(eventId: Int): LiveData<Boolean> {
        return favoriteEventDao.isFavorite(eventId).map { it != null }
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null
        fun getInstance(
            apiService: ApiService,
            upcomingEventDao: UpcomingEventDao,
            finishedEventDao: FinishedEventDao,
            favoriteEventDao: FavoriteEventDao

        ): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(
                    apiService,
                    upcomingEventDao,
                    finishedEventDao,
                    favoriteEventDao
                )
            }.also { instance = it }
    }
}