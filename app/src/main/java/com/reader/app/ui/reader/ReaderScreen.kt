package com.reader.app.ui.reader

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.reader.app.ui.theme.WarmCream
import com.reader.app.ui.theme.SoftCharcoal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    urlId: String?,
    onBackClick: () -> Unit,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    LaunchedEffect(urlId) {
        if (urlId != null) {
            viewModel.loadUrl(urlId)
        }
    }

    val urlEntity by viewModel.urlEntity.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = urlEntity?.domain ?: "Reading",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        val css = """
            body {
                background-color: #FBF9F6;
                color: #2D2D2D;
                font-family: 'Georgia', serif;
                font-size: 18px;
                line-height: 1.6;
                padding: 20px;
                max-width: 800px;
                margin: 0 auto;
            }
            img {
                max-width: 100%;
                height: auto;
                border-radius: 8px;
            }
            h1, h2, h3 {
                font-family: 'Merriweather', serif;
                color: #2D2D2D;
            }
            a {
                color: #D9795F;
            }
        """.trimIndent()

        val js = "var style = document.createElement('style'); style.innerHTML = `$css`; document.head.appendChild(style);"

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            view?.evaluateJavascript(js, null)
                        }
                    }
                }
            },
            update = { webView ->
                urlEntity?.let { entity ->
                    // For now, load the original URL. In future, load parsed Reader View HTML
                    webView.loadUrl(entity.originalUrl)
                }
            }
        )
    }
}
