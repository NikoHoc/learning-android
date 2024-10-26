package com.dicoding.dicodingevent.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
class FavoriteEventEntity(
    @field:ColumnInfo(name = "title")
    @field:PrimaryKey
    val title: String,

    @field:ColumnInfo(name = "publishedAt")
    val publishedAt: String,

    @field:ColumnInfo(name = "urlToImage")
    val urlToImage: String? = null,

    @field:ColumnInfo(name = "url")
    val url: String? = null,

    @field:ColumnInfo(name = "bookmarked")
    var isBookmarked: Boolean
)

@Entity(tableName = "upcoming_events")
class UpcomingEventEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = false)
    val id: Int? = null,

    @field:ColumnInfo(name = "name")
    val name: String? = null,

    @field:ColumnInfo(name = "media_cover")
    val mediaCover: String? = null,

    @field:ColumnInfo(name = "image_logo")
    val imageLogo: String? = null,

    @field:ColumnInfo(name = "category")
    val category: String? = null,

    @field:ColumnInfo(name = "registrants")
    val registrants: Int? = null,

    @field:ColumnInfo(name = "quota")
    val quota: Int? = null,

    @field:ColumnInfo(name = "city_name")
    val cityName: String? = null,

    @field:ColumnInfo(name = "summary")
    val summary: String? = null,

    @field:ColumnInfo(name = "description")
    val description: String? = null,

    @field:ColumnInfo(name = "begin_time")
    val beginTime: String? = null,

    @field:ColumnInfo(name = "end_time")
    val endTime: String? = null,

    @field:ColumnInfo(name = "link")
    val link: String? = null,
)
