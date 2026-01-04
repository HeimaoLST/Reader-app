package com.reader.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.reader.app.data.local.entity.UrlEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UrlDao {
    @Query("SELECT * FROM urls WHERE isArchived = 0 ORDER BY collectedAt DESC")
    fun getAllUrls(): Flow<List<UrlEntity>>

    @Query("SELECT * FROM urls WHERE id = :id")
    suspend fun getUrlById(id: String): UrlEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrl(url: UrlEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUrls(urls: List<UrlEntity>)

    @Query("DELETE FROM urls WHERE id = :id")
    suspend fun deleteUrl(id: String)

    @Query("UPDATE urls SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)
}
