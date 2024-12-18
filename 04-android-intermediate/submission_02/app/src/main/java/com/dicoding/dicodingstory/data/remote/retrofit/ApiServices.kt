package com.dicoding.dicodingstory.data.remote.retrofit

import com.dicoding.dicodingstory.data.local.StoryEntity
import com.dicoding.dicodingstory.data.remote.response.ListStoryItem
import com.dicoding.dicodingstory.data.remote.response.LoginResponse
import com.dicoding.dicodingstory.data.remote.response.RegisterResponse
import com.dicoding.dicodingstory.data.remote.response.StoryResponse
import com.dicoding.dicodingstory.data.remote.response.UploadStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiServices {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

//    @GET("stories")
//    suspend fun getStories(): StoryResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ) : UploadStoryResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse
}