package com.reader.app.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.reader.app.data.local.AppDatabase
import com.reader.app.data.local.entity.UrlEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * UrlDao 集成测试示例
 * 测试数据库 CRUD 操作
 *
 * 运行方式：
 * ./gradlew connectedAndroidTest
 */
@RunWith(AndroidJUnit4::class)
class UrlDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var urlDao: UrlDao

    @Before
    fun setup() {
        // 使用内存数据库进行测试，确保每次测试都是干净的
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
        urlDao = database.urlDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertUrl_shouldInsertSuccessfully() = runTest {
        // Given
        val url = UrlEntity(
            id = "1",
            originalUrl = "https://example.com/article1",
            title = "Test Article",
            description = "Test Description",
            domain = "example.com",
            isRead = false,
            isFavorite = false,
            isArchived = false,
            createdAt = System.currentTimeMillis(),
            collectedAt = System.currentTimeMillis()
        )

        // When
        urlDao.insertUrl(url)

        // Then
        val retrieved = urlDao.getUrlById("1")
        assertNotNull(retrieved)
        assertEquals("Test Article", retrieved?.title)
        assertEquals("https://example.com/article1", retrieved?.originalUrl)
    }

    @Test
    fun getAllUrls_shouldReturnAllUrls() = runTest {
        // Given
        val urls = listOf(
            UrlEntity(
                id = "1",
                originalUrl = "https://example.com/1",
                title = "Article 1",
                description = null,
                domain = "example.com",
                isRead = false,
                isFavorite = false,
                isArchived = false,
                createdAt = 1705310400000L,
                collectedAt = 1705310400000L
            ),
            UrlEntity(
                id = "2",
                originalUrl = "https://example.com/2",
                title = "Article 2",
                description = null,
                domain = "example.com",
                isRead = false,
                isFavorite = true,
                isArchived = false,
                createdAt = 1705310401000L,
                collectedAt = 1705310401000L
            )
        )

        // When
        urlDao.insertUrls(urls)
        val result = urlDao.getAllUrls().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Article 1", result[0].title)
        assertEquals("Article 2", result[1].title)
    }

    @Test
    fun getAllUrls_shouldOnlyReturnNonArchivedUrls() = runTest {
        // Given
        val urls = listOf(
            UrlEntity(
                id = "1",
                originalUrl = "https://example.com/1",
                title = "Active Article",
                description = null,
                domain = "example.com",
                isRead = false,
                isFavorite = false,
                isArchived = false,
                createdAt = 1705310400000L,
                collectedAt = 1705310400000L
            ),
            UrlEntity(
                id = "2",
                originalUrl = "https://example.com/2",
                title = "Archived Article",
                description = null,
                domain = "example.com",
                isRead = false,
                isFavorite = false,
                isArchived = true, // 已归档
                createdAt = 1705310401000L,
                collectedAt = 1705310401000L
            )
        )

        // When
        urlDao.insertUrls(urls)
        val result = urlDao.getAllUrls().first()

        // Then - 只应该返回非归档的 URL
        assertEquals(1, result.size)
        assertEquals("Active Article", result[0].title)
    }

    @Test
    fun getAllUrls_shouldReturnUrlsOrderByCollectedAtDesc() = runTest {
        // Given - 按时间倒序插入
        val urls = listOf(
            UrlEntity(
                id = "1",
                originalUrl = "https://example.com/1",
                title = "Oldest",
                description = null,
                domain = "example.com",
                isRead = false,
                isFavorite = false,
                isArchived = false,
                createdAt = 1705310400000L,
                collectedAt = 1705310400000L // 最早
            ),
            UrlEntity(
                id = "2",
                originalUrl = "https://example.com/2",
                title = "Newest",
                description = null,
                domain = "example.com",
                isRead = false,
                isFavorite = false,
                isArchived = false,
                createdAt = 1705310402000L,
                collectedAt = 1705310402000L // 最新
            ),
            UrlEntity(
                id = "3",
                originalUrl = "https://example.com/3",
                title = "Middle",
                description = null,
                domain = "example.com",
                isRead = false,
                isFavorite = false,
                isArchived = false,
                createdAt = 1705310401000L,
                collectedAt = 1705310401000L
            )
        )

        // When
        urlDao.insertUrls(urls)
        val result = urlDao.getAllUrls().first()

        // Then - 应该按 collectedAt 降序排列
        assertEquals(3, result.size)
        assertEquals("Newest", result[0].title) // 最新
        assertEquals("Middle", result[1].title)
        assertEquals("Oldest", result[2].title) // 最旧
    }

    @Test
    fun deleteUrl_shouldRemoveUrlSuccessfully() = runTest {
        // Given
        val url = UrlEntity(
            id = "1",
            originalUrl = "https://example.com/1",
            title = "To Delete",
            description = null,
            domain = "example.com",
            isRead = false,
            isFavorite = false,
            isArchived = false,
            createdAt = 1705310400000L,
            collectedAt = 1705310400000L
        )
        urlDao.insertUrl(url)

        // When
        urlDao.deleteUrl("1")

        // Then
        val retrieved = urlDao.getUrlById("1")
        assertNull(retrieved)
    }

    @Test
    fun updateFavoriteStatus_shouldToggleFavorite() = runTest {
        // Given
        val url = UrlEntity(
            id = "1",
            originalUrl = "https://example.com/1",
            title = "Test",
            description = null,
            domain = "example.com",
            isRead = false,
            isFavorite = false,
            isArchived = false,
            createdAt = 1705310400000L,
            collectedAt = 1705310400000L
        )
        urlDao.insertUrl(url)

        // When - 更新为收藏
        urlDao.updateFavoriteStatus("1", true)

        // Then
        val updated = urlDao.getUrlById("1")
        assertNotNull(updated)
        assertTrue(updated!!.isFavorite)
    }

    @Test
    fun getUrlById_shouldReturnNullForNonExistentId() = runTest {
        // When
        val result = urlDao.getUrlById("nonexistent")

        // Then
        assertNull(result)
    }

    @Test
    fun insertUrl_withSameId_shouldReplaceExisting() = runTest {
        // Given
        val originalUrl = UrlEntity(
            id = "1",
            originalUrl = "https://example.com/old",
            title = "Old Title",
            description = null,
            domain = "example.com",
            isRead = false,
            isFavorite = false,
            isArchived = false,
            createdAt = 1705310400000L,
            collectedAt = 1705310400000L
        )
        urlDao.insertUrl(originalUrl)

        // When - 插入相同 ID 的新数据
        val updatedUrl = originalUrl.copy(
            title = "New Title",
            isFavorite = true
        )
        urlDao.insertUrl(updatedUrl)

        // Then - 应该替换旧数据
        val result = urlDao.getUrlById("1")
        assertNotNull(result)
        assertEquals("New Title", result?.title)
        assertTrue(result!!.isFavorite)
    }
}
