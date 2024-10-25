package com.dicoding.dicodingevent.data.remote.retrofit

import com.dicoding.dicodingevent.data.remote.response.DetailEventResponse
import com.dicoding.dicodingevent.data.remote.response.EventResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    // untuk list event dan optional jika search
    // active -> 1: upcoming, 0: finished, -1: all events
    @GET("/events")
    fun getListEvents(
        @Query("active") active: Int,
        @Query("q") query: String? = null
    ): Call<EventResponse>

    // untuk detail event
    @GET("/events/{id}")
    fun getDetailEvent(
        @Path("id") id: Int
    ): Call<DetailEventResponse>

    @GET("/events")
    fun getSearchEvent(
        @Query("active") active: Int,
        @Query("q") query: String? = null
    ): Call<EventResponse>

}