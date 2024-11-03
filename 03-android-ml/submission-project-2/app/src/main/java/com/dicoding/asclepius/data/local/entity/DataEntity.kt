package com.dicoding.asclepius.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
class HistoryEntity(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @field:ColumnInfo(name = "image_data")
    val imageData: String? = null,

    @field:ColumnInfo(name = "result")
    val result: String? = null,

    @field:ColumnInfo(name = "created_at")
    val createdAt: String? = null
)

@Entity(tableName = "cancer_articles")
class CancerArticleEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey
    val id: String,

    @ColumnInfo(name = "title")
    val title: String? = null,

    @ColumnInfo(name = "description")
    val description: String? = null,

    @ColumnInfo(name = "url")
    val url: String? = null,

    @ColumnInfo(name = "url_to_image")
    val urlToImage: String? = null,
)