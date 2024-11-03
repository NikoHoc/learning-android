    package com.dicoding.asclepius.data

    import androidx.lifecycle.LiveData
    import com.dicoding.asclepius.data.local.entity.HistoryEntity
    import com.dicoding.asclepius.data.local.room.ArticlesDao
    import com.dicoding.asclepius.data.local.room.HistoryDao
    import com.dicoding.asclepius.data.remote.retrofit.ApiService
    import java.text.SimpleDateFormat
    import java.util.Date
    import java.util.Locale
    import java.util.TimeZone

    class DataRepository private constructor(
        private val apiService: ApiService,
        private val historyDao: HistoryDao,
        private val articlesDao: ArticlesDao
    ) {

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