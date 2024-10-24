package com.dicoding.newsapp.data.remote.retrofit

import com.dicoding.newsapp.data.remote.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines?country=us&category=business")
    //fun getNews(@Query("apiKey") apiKey: String): Call<NewsResponse>
    // implement kotlin coroutines, hapus call dan beri suspend
    suspend fun getNews(@Query("apiKey") apiKey: String): NewsResponse //hapus Call<>
}