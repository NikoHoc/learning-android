package com.dicoding.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dicodingevent.data.local.entity.FavoriteEventEntity
import com.dicoding.dicodingevent.data.local.entity.FinishedEventEntity
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity

@Dao
interface FavoriteEventDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteEvent(favoritEvent: FavoriteEventEntity)

    @Query("SELECT * FROM favorite_events")
    fun getAllFavoriteEvents(): LiveData<List<FavoriteEventEntity>>

    @Query("SELECT * FROM favorite_events WHERE id = :id LIMIT 1")
    fun isFavorite(id: Int): LiveData<FavoriteEventEntity?>

    @Query("DELETE FROM favorite_events WHERE id = :id")
    suspend fun deleteFavoriteEvent(id: Int)

}
@Dao
interface UpcomingEventDao {
    // insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUpcomingEvent(event: List<UpcomingEventEntity>)

    // delete
    @Query("DELETE FROM upcoming_events")
    suspend fun deleteAllUpcomingEvent()

    @Query("SELECT * FROM upcoming_events")
    fun getUpcomingEvent(): LiveData<List<UpcomingEventEntity>>

    @Query("SELECT * FROM upcoming_events WHERE id = :id")
    fun getUpcomingEventById(id: Int): LiveData<UpcomingEventEntity?>

}

@Dao
interface FinishedEventDao {
    // insert
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFinishedEvent(event: List<FinishedEventEntity>)

    // delete
    @Query("DELETE FROM finished_events")
    suspend fun deleteAllFinishedEvent()

    @Query("SELECT * FROM finished_events")
    fun getFinishedEvent(): LiveData<List<FinishedEventEntity>>

    @Query("SELECT * FROM finished_events WHERE id = :id")
    fun getFinishedEventById(id: Int): LiveData<FinishedEventEntity?>

}