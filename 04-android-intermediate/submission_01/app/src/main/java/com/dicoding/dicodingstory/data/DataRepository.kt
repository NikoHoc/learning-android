package com.dicoding.dicodingstory.data

import androidx.lifecycle.liveData
import com.dicoding.dicodingstory.data.pref.UserModel
import com.dicoding.dicodingstory.data.pref.UserPreference
import com.dicoding.dicodingstory.data.remote.response.RegisterResponse
import com.dicoding.dicodingstory.data.remote.response.StoryResponse
import com.dicoding.dicodingstory.data.remote.retrofit.ApiServices
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import retrofit2.HttpException

class DataRepository private constructor(
    private val apiServices: ApiServices,
    private val userPreference: UserPreference
) {

    fun registerUser(name: String, email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun login(email: String, password: String) = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.login(email, password)
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getStories() = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.getStories()
            emit(Result.Success(response))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }
    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(
            apiServices: ApiServices,
            userPreference: UserPreference
        ): DataRepository =
            instance ?: synchronized(this) {
                instance ?: DataRepository(
                    apiServices,
                    userPreference)
            }.also { instance = it }
    }
}