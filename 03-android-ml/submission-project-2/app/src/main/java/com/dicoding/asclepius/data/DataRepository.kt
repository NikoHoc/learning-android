    package com.dicoding.asclepius.data

    import android.util.Log
    import androidx.lifecycle.LiveData
    import androidx.lifecycle.liveData
    import androidx.lifecycle.map
    import com.dicoding.asclepius.BuildConfig
    import com.dicoding.asclepius.data.local.entity.CancerArticleEntity
    import com.dicoding.asclepius.data.local.entity.HistoryEntity
    import com.dicoding.asclepius.data.local.room.ArticlesDao
    import com.dicoding.asclepius.data.local.room.HistoryDao
    import com.dicoding.asclepius.data.remote.retrofit.ApiService
    import java.text.SimpleDateFormat
    import java.util.Date
    import java.util.Locale

    class DataRepository private constructor(
        private val apiService: ApiService,
        private val historyDao: HistoryDao,
        private val articlesDao: ArticlesDao
    ) {
        fun getCancerArticle(): LiveData<Result<List<CancerArticleEntity>>> = liveData {
            emit(Result.Loading)
            try {
                val response = apiService.getCancerArticles(apiKey = BuildConfig.API_KEY)
                Log.d("DataRepository", "API response articles: ${response.articles}")
                val articles = response.articles?.map { article ->
                    CancerArticleEntity(
                        id = article?.source?.id.toString(),
                        title = article?.title,
                        description = article?.description,
                        url = article?.url,
                        urlToImage = article?.urlToImage
                    )
                }
                articlesDao.delteAll()
                Log.d("DataRepository", "Old articles deleted")
                articlesDao.insertIntoArticles(articles!!)
                Log.d("DataRepository", "New articles inserted into database: ${articles.size}")
            } catch (e: Exception) {
                Log.d("DataRepository", "getCancerArticle: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
            //val localData = articlesDao.getAllArticles().map { Result.Success(it) }
            val localData: LiveData<Result<List<CancerArticleEntity>>> = articlesDao.getAllArticles().map {
                Log.d("DataRepository", "Fetched articles from DB: ${it.size}")
                Result.Success(it)
            }
            emitSource(localData)
        }

        suspend fun insertToHistory(imageData: String?, result: String?) {
            val targetFormat = SimpleDateFormat("dd MMMM yyyy | hh:mm a", Locale.getDefault())
            val currentDate = targetFormat.format(Date())

            val history = HistoryEntity(imageData = imageData, result = result, createdAt = currentDate)
            historyDao.insertIntoHistory(history)
        }

        suspend fun getHistory(): List<HistoryEntity> = historyDao.getAllHistory()

        suspend fun isResultSaved(imageUri: String): Boolean {
            return historyDao.isResultSaved(imageUri)
        }

        companion object {
            @Volatile
            private var instance: DataRepository? = null
            fun getInstance(
                apiService: ApiService,
                historyDao: HistoryDao,
                articlesDao: ArticlesDao

            ): DataRepository =
                instance ?: synchronized(this) {
                    instance ?: DataRepository(
                        apiService,
                        historyDao,
                        articlesDao
                    )
                }.also { instance = it }
        }
    }