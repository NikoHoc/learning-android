package com.dicoding.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity

class EventsDao {
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