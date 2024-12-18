package com.dicoding.dicodingstory.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.dicodingstory.data.pref.UserModel
import com.dicoding.dicodingstory.data.pref.UserPreference
import com.dicoding.dicodingstory.data.remote.response.ListStoryItem
import com.dicoding.dicodingstory.data.remote.response.LoginResponse
import com.dicoding.dicodingstory.data.remote.response.RegisterResponse
import com.dicoding.dicodingstory.data.remote.response.StoryResponse
import com.dicoding.dicodingstory.data.remote.response.UploadStoryResponse
import com.dicoding.dicodingstory.data.remote.retrofit.ApiServices
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import com.dicoding.dicodingstory.data.Result
import com.dicoding.dicodingstory.data.local.StoryDatabase
import com.dicoding.dicodingstory.data.local.StoryEntity
import com.dicoding.dicodingstory.data.paging.StoryRemoteMediator

class DataRepository private constructor(
    private val storyDatabase: StoryDatabase,
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
            val errorResponse = Gson().fromJson(errorBody, LoginResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    fun getStories(): LiveData<PagingData<StoryEntity>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiServices),
            pagingSourceFactory = {
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

//    fun getStoriesWithLocation() = liveData {
//        emit(Result.Loading)
//        try {
//            val response = apiServices.getStoriesWithLocation()
//            emit(Result.Success(response))
//        } catch (e: HttpException) {
//            val errorBody = e.response()?.errorBody()?.string()
//            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
//            emit(Result.Error(errorResponse.message ?: "Unknown error"))
//        } catch (e: Exception) {
//            emit(Result.Error(e.message ?: "Something went wrong"))
//        }
//    }

    fun getStoriesWithLocation(): LiveData<Result<List<ListStoryItem>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiServices.getStoriesWithLocation()
            val listStory = response.listStory?.filterNotNull() ?: emptyList()
            emit(Result.Success(listStory))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
            emit(Result.Error(errorResponse.message ?: "Unknown error"))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Something went wrong"))
        }
    }

    fun uploadStory(imageFile: File, description: String, lat: Float? = null, lon: Float? = null) = liveData {
        emit(Result.Loading)

        try {
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val successResponse = when {
                lat == null && lon == null -> {
                    apiServices.uploadStory(multipartBody, descriptionRequestBody)
                }
                else -> {
                    val latDescriptionBody = lat.toString().toRequestBody("text/plain".toMediaType())
                    val lonDescriptionBody = lon.toString().toRequestBody("text/plain".toMediaType())
                    apiServices.uploadStory(multipartBody, descriptionRequestBody, latDescriptionBody, lonDescriptionBody)
                }
            }
            emit(Result.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadStoryResponse::class.java)
            emit(Result.Error(errorResponse.message.toString()))
        }
    }

    suspend fun saveSession(userModel: UserModel) = userPreference.saveSession(userModel)

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        fun getInstance(database: StoryDatabase, apiService: ApiServices, dataStoreToken: UserPreference) = DataRepository(database, apiService, dataStoreToken)
    }
}