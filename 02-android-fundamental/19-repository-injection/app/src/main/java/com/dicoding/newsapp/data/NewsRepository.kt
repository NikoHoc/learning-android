package com.dicoding.newsapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.dicoding.newsapp.BuildConfig
import com.dicoding.newsapp.data.local.entity.NewsEntity
import com.dicoding.newsapp.data.local.room.NewsDao
import com.dicoding.newsapp.data.remote.response.NewsResponse
import com.dicoding.newsapp.data.remote.retrofit.ApiService
import com.dicoding.newsapp.utils.AppExecutors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsRepository private constructor(
    private val apiService: ApiService,
    private val newsDao: NewsDao,
    private val appExecutors: AppExecutors
) {
    /* sebelum pakai kotlin coroutines */
//    private val result = MediatorLiveData<Result<List<NewsEntity>>>()
//    fun getHeadlineNews(): LiveData<Result<List<NewsEntity>>> {
//        result.value = Result.Loading
//        val client = apiService.getNews(BuildConfig.API_KEY)
//        client.enqueue(object : Callback<NewsResponse> {
//            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
//                if (response.isSuccessful) {
//                    val articles = response.body()?.articles
//                    val newsList = ArrayList<NewsEntity>()
//                    appExecutors.diskIO.execute {
//                        articles?.forEach { article ->
//                            val isBookmarked = newsDao.isNewsBookmarked(article.title)
//                            val news = NewsEntity(
//                                article.title,
//                                article.publishedAt,
//                                article.urlToImage,
//                                article.url,
//                                isBookmarked
//                            )
//                            newsList.add(news)
//                        }
//                        newsDao.deleteAll()
//                        newsDao.insertNews(newsList)
//                    }
//                }
//            }
//            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
//                result.value = Result.Error(t.message.toString())
//            }
//        })
//        val localData = newsDao.getNews()
//        result.addSource(localData) { newData: List<NewsEntity> ->
//            result.value = Result.Success(newData)
//        }
//        return result
//    }
//    fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
//        return newsDao.getBookmarkedNews()
//    }
//    fun setBookmarkedNews(news: NewsEntity, bookmarkState: Boolean) {
//        appExecutors.diskIO.execute {
//            news.isBookmarked = bookmarkState
//            newsDao.updateNews(news)
//        }
//    }
    /* sebelum pakai kotlin coroutines */

    /* Sesudah pakai kotlin coroutines */
    fun getHeadlineNews(): LiveData<Result<List<NewsEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getNews(BuildConfig.API_KEY)
            val articles = response.articles
            val newsList = articles.map { article ->
                val isBookmarked = newsDao.isNewsBookmarked(article.title)
                NewsEntity(
                    article.title,
                    article.publishedAt,
                    article.urlToImage,
                    article.url,
                    isBookmarked
                )
            }
            newsDao.deleteAll()
            newsDao.insertNews(newsList)
        } catch (e: Exception) {
            Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
        val localData: LiveData<Result<List<NewsEntity>>> = newsDao.getNews().map { Result.Success(it) }
        emitSource(localData)
    }
    fun getBookmarkedNews(): LiveData<List<NewsEntity>> {
        return newsDao.getBookmarkedNews()
    }
    suspend fun setNewsBookmark(news: NewsEntity, bookmarkState: Boolean) {
        //hapus penggunaan appExecutor
        news.isBookmarked = bookmarkState
        newsDao.updateNews(news)
    }
    /* Sesudah pakai kotlin coroutines */

    companion object {
        @Volatile
        private var instance: NewsRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: NewsDao,
            appExecutors: AppExecutors
        ): NewsRepository =
            instance ?: synchronized(this) {
                instance ?: NewsRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}