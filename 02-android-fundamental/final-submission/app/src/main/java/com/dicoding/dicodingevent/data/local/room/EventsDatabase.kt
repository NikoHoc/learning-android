package com.dicoding.dicodingevent.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.dicodingevent.data.local.entity.UpcomingEventEntity

//@Database(entities = [FavoriteEvent::class, UpcomingEvent::class, FinishedEvent::class], version = 1, exportSchema = false)
@Database(entities = [UpcomingEventEntity::class], version = 1, exportSchema = false)
abstract class EventDatabase : RoomDatabase() {
    //abstract fun favoriteEventDao(): FavoriteEventDao
    abstract fun upcomingEventDao(): UpcomingEventDao
    //abstract fun finishedEventDao(): FinishedEventDao

    companion object {
        @Volatile
        private var instance: EventDatabase? = null
        fun getInstance(context: Context): EventDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EventDatabase::class.java, "Events.db"
                ).build()
            }
    }
}