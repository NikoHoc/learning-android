package com.dicoding.dicodingevent.data.remote.retrofit

import com.dicoding.dicodingevent.data.remote.response.DetailEventResponse
import com.dicoding.dicodingevent.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // untuk list event dan optional jika search
    // active -> 1: upcoming, 0: finished, -1: all events
    @GET("/events")
    suspend fun getListEvents(
        @Query("active") active: Int?,
    ): EventResponse

    // untuk detail event
    @GET("/events/{id}")
    suspend fun getDetailEvent(
        @Path("id") id: Int
    ): DetailEventResponse

    @GET("/events")
    suspend fun getSearchEvent(
        @Query("active") active: Int,
        @Query("q") query: String? = null
    ): EventResponse

    @GET("events")
    fun getNearbyEvent (
        @Query("active") active: Int = 1,
        @Query("limit") limit: Int = 1
    ): Call<EventResponse>
}