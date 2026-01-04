package com.reader.app.data.repository

import com.reader.app.data.local.dao.UrlDao
import com.reader.app.data.local.entity.UrlEntity
import com.reader.app.domain.repository.UrlRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class UrlRepositoryImpl @Inject constructor(
    private val urlDao: UrlDao
    // private val api: UrlApi // TODO: Integrate API later
) : UrlRepository {

    override fun getUrls(): Flow<List<UrlEntity>> {
        return urlDao.getAllUrls()
    }

    override suspend fun getUrl(id: String): UrlEntity? {
        return urlDao.getUrlById(id)
    }

    override suspend fun addUrl(url: String) {
        // Mock implementation for now until API is ready
        // In real app: call API -> get parsed result -> save to DB
        
        val newUrl = UrlEntity(
            id = UUID.randomUUID().toString(),
            originalUrl = url,
            title = "New Link: $url",
            description = "Description pending...",
            domain = "example.com",
            createdAt = System.currentTimeMillis(),
            collectedAt = System.currentTimeMillis()
        )
        urlDao.insertUrl(newUrl)
    }

    override suspend fun deleteUrl(id: String) {
        urlDao.deleteUrl(id)
    }

    override suspend fun toggleFavorite(id: String, isFavorite: Boolean) {
        urlDao.updateFavoriteStatus(id, isFavorite)
    }
}
