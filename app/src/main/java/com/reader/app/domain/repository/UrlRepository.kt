package com.reader.app.domain.repository

import com.reader.app.data.local.entity.UrlEntity
import kotlinx.coroutines.flow.Flow

interface UrlRepository {
    fun getUrls(): Flow<List<UrlEntity>>
    suspend fun getUrl(id: String): UrlEntity?
    suspend fun addUrl(url: String)
    suspend fun deleteUrl(id: String)
    suspend fun toggleFavorite(id: String, isFavorite: Boolean)
}
