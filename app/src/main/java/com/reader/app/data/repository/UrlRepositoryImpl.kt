package com.reader.app.data.repository

import com.reader.app.data.local.dao.UrlDao
import com.reader.app.data.local.entity.UrlEntity
import com.reader.app.domain.repository.UrlRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class UrlRepositoryImpl @Inject constructor(
    private val urlDao: UrlDao,
    private val api: com.reader.app.data.remote.api.UrlApi
) : UrlRepository {

    override fun getUrls(): Flow<List<UrlEntity>> {
        return urlDao.getAllUrls()
    }

    override suspend fun getUrl(id: String): UrlEntity? {
        return urlDao.getUrlById(id)
    }

    override suspend fun addUrl(url: String) {
        try {
            val response = api.createUrl(com.reader.app.data.remote.dto.CreateUrlRequest(url))
            urlDao.insertUrl(response.toEntity())
        } catch (e: Exception) {
            // Log or handle error
            // Fallback to local-only for now if API fails? 
            // In real app: use WorkManager for background sync
        }
    }

    override suspend fun deleteUrl(id: String) {
        try {
            api.deleteUrl(id)
            urlDao.deleteUrl(id)
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun toggleFavorite(id: String, isFavorite: Boolean) {
        try {
            api.toggleFavorite(id, com.reader.app.data.remote.dto.ToggleFavoriteRequest(isFavorite))
            urlDao.updateFavoriteStatus(id, isFavorite)
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun sync() {
        try {
            val response = api.getUrls()
            val entities = response.data.map { it.toEntity() }
            urlDao.insertUrls(entities)
        } catch (e: Exception) {
            // Handle sync error
        }
    }

    override suspend fun getReaderView(id: String): String? {
        return try {
            api.getReaderView(id).content
        } catch (e: Exception) {
            null
        }
    }

    private fun com.reader.app.data.remote.dto.UrlDto.toEntity(): UrlEntity {
        return UrlEntity(
            id = id,
            originalUrl = originalUrl,
            title = title,
            description = description,
            domain = domain,
            isRead = isRead,
            isFavorite = isFavorite,
            isArchived = isArchived,
            createdAt = com.reader.app.utils.DateUtils.parseIsoToLong(createdAt),
            collectedAt = com.reader.app.utils.DateUtils.parseIsoToLong(collectedAt)
        )
    }
}
