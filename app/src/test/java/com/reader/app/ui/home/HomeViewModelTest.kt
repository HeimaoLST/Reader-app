package com.reader.app.ui.home

import app.cash.turbine.test
import com.reader.app.data.local.entity.UrlEntity
import com.reader.app.domain.repository.UrlRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * HomeViewModel 单元测试示例
 * 测试首页 ViewModel 的状态管理和用户交互
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    @Mock
    private lateinit var repository: UrlRepository

    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `urls should emit initial empty list`() = runTest {
        // Given
        whenever(repository.getUrls()).thenReturn(flow { emit(emptyList()) })

        // When
        viewModel = HomeViewModel(repository)

        // Then
        viewModel.urls.test {
            val items = awaitItem()
            assertTrue(items.isEmpty())
        }
    }

    @Test
    fun `urls should emit list from repository`() = runTest {
        // Given
        val testUrls = listOf(
            UrlEntity(
                id = "1",
                originalUrl = "https://example.com/1",
                title = "Article 1",
                domain = "example.com",
                isFavorite = false,
                isRead = false,
                collectedAt = 1705310400000L
            ),
            UrlEntity(
                id = "2",
                originalUrl = "https://example.com/2",
                title = "Article 2",
                domain = "example.com",
                isFavorite = true,
                isRead = false,
                collectedAt = 1705310401000L
            )
        )
        whenever(repository.getUrls()).thenReturn(flow { emit(testUrls) })

        // When
        viewModel = HomeViewModel(repository)

        // Then
        viewModel.urls.test {
            val items = awaitItem()
            assertEquals(2, items.size)
            assertEquals("Article 1", items[0].title)
            assertEquals("Article 2", items[1].title)
        }
    }

    @Test
    fun `addUrl should call repository addUrl`() = runTest {
        // Given
        val testUrl = "https://example.com/new"
        whenever(repository.getUrls()).thenReturn(flow { emit(emptyList()) })

        // When
        viewModel = HomeViewModel(repository)
        viewModel.addUrl(testUrl)

        // Then
        verify(repository).addUrl(testUrl)
    }

    @Test
    fun `deleteUrl should call repository deleteUrl`() = runTest {
        // Given
        val urlId = "123"
        whenever(repository.getUrls()).thenReturn(flow { emit(emptyList()) })

        // When
        viewModel = HomeViewModel(repository)
        viewModel.deleteUrl(urlId)

        // Then
        verify(repository).deleteUrl(urlId)
    }

    @Test
    fun `toggleFavorite should call repository with toggled value`() = runTest {
        // Given
        val testUrl = UrlEntity(
            id = "1",
            originalUrl = "https://example.com/1",
            title = "Article 1",
            domain = "example.com",
            isFavorite = false,
            isRead = false,
            collectedAt = 1705310400000L
        )
        whenever(repository.getUrls()).thenReturn(flow { emit(emptyList()) })

        // When
        viewModel = HomeViewModel(repository)
        viewModel.toggleFavorite(testUrl)

        // Then - 应该用 true 调用（因为原来是 false）
        verify(repository).toggleFavorite("1", true)
    }

    @Test
    fun `sync should call repository sync on init`() = runTest {
        // Given
        whenever(repository.getUrls()).thenReturn(flow { emit(emptyList()) })

        // When
        viewModel = HomeViewModel(repository)

        // Then - init 会自动调用 sync()
        verify(repository).sync()
    }

    @Test
    fun `sync should call repository sync when manually triggered`() = runTest {
        // Given
        whenever(repository.getUrls()).thenReturn(flow { emit(emptyList()) })

        // When
        viewModel = HomeViewModel(repository)
        viewModel.sync()

        // Then - 应该调用两次，一次在 init，一次手动调用
        verify(repository).sync()
    }
}
