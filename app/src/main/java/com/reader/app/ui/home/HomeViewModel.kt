package com.reader.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reader.app.data.local.entity.UrlEntity
import com.reader.app.domain.repository.UrlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: UrlRepository
) : ViewModel() {

    init {
        sync()
    }

    val urls: StateFlow<List<UrlEntity>> = repository.getUrls()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addUrl(url: String) {
        viewModelScope.launch {
            repository.addUrl(url)
        }
    }
    
    fun deleteUrl(id: String) {
        viewModelScope.launch {
            repository.deleteUrl(id)
        }
    }

    fun toggleFavorite(url: UrlEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(url.id, !url.isFavorite)
        }
    }

    fun sync() {
        viewModelScope.launch {
            repository.sync()
        }
    }
}
