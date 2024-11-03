package com.dicoding.asclepius.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.asclepius.data.local.entity.CancerArticleEntity
import com.dicoding.asclepius.data.local.entity.HistoryEntity

@Dao
interface ArticlesDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoArticles(article: List<CancerArticleEntity>)

    @Query("SELECT * FROM cancer_articles")
    fun getAllArticles(): LiveData<List<CancerArticleEntity>>

    @Query("DELETE FROM cancer_articles")
    suspend fun delteAll()
}

@Dao
interface HistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIntoHistory(history: HistoryEntity)

    @Query("SELECT * FROM history")
    suspend fun getAllHistory(): List<HistoryEntity>

    @Query("SELECT EXISTS(SELECT 1 FROM history WHERE image_data = :imageUri)")
    suspend fun isResultSaved(imageUri: String): Boolean
}