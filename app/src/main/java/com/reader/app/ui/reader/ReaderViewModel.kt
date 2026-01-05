package com.reader.app.ui.reader

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reader.app.data.local.entity.UrlEntity
import com.reader.app.domain.repository.UrlRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val repository: UrlRepository
) : ViewModel() {

    private val _fontSize = MutableStateFlow(18f)
    val fontSize: StateFlow<Float> = _fontSize.asStateFlow()

    private val _readerTheme = MutableStateFlow("sepia") // "light", "sepia", "dark"
    val readerTheme: StateFlow<String> = _readerTheme.asStateFlow()

    fun loadUrl(id: String) {
        viewModelScope.launch {
            val url = repository.getUrl(id)
            _urlEntity.value = url
            _readerContent.value = repository.getReaderView(id)
        }
    }

    fun updateFontSize(newSize: Float) {
        _fontSize.value = newSize
    }

    fun updateTheme(newTheme: String) {
        _readerTheme.value = newTheme
    }
}
