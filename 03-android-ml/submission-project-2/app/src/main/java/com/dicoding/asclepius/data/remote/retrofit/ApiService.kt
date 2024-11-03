package com.dicoding.asclepius.data.remote.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.data.remote.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    //https://newsapi.org/v2/top-headlines?q=cancer&apiKey=efb7cb645a374923915ccb557c7cbb53
    @GET("top-headlines")
    suspend fun getCancerArticles(
        @Query("q") q: String = "cancer",
        @Query("apiKey") apiKey: String
    ): ArticleResponse
}