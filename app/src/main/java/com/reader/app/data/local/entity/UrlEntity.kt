package com.reader.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "urls")
data class UrlEntity(
    @PrimaryKey val id: String,
    val originalUrl: String,
    val title: String,
    val description: String?,
    val domain: String?,
    val isRead: Boolean = false,
    val isFavorite: Boolean = false,
    val isArchived: Boolean = false,
    val createdAt: Long,
    val collectedAt: Long
)
