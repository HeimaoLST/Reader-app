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
    val readerContent by viewModel.readerContent.collectAsState()
    val fontSize by viewModel.fontSize.collectAsState()
    val readerTheme by viewModel.readerTheme.collectAsState()
    
    var showSettings by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

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
                actions = {
                    IconButton(onClick = { showSettings = true }) {
                        Icon(androidx.compose.material.icons.Icons.Default.Settings, contentDescription = "Reading Settings")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        val backgroundColor = when (readerTheme) {
            "dark" -> "#1A1A1A"
            "sepia" -> "#FBF9F6"
            else -> "#FFFFFF"
        }
        val textColor = when (readerTheme) {
            "dark" -> "#E0E0E0"
            else -> "#2D2D2D"
        }
        
        val css = """
            body {
                background-color: $backgroundColor;
                color: $textColor;
                font-family: 'Georgia', serif;
                font-size: ${fontSize}px;
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
                color: $textColor;
            }
            a {
                color: #D9795F;
            }
        """.trimIndent()

        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                }
            },
            update = { webView ->
                if (readerContent != null) {
                    val html = """
                        <!DOCTYPE html>
                        <html>
                        <head>
                            <style>$css</style>
                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        </head>
                        <body>
                            $readerContent
                        </body>
                        </html>
                    """.trimIndent()
                    webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
                } else {
                    urlEntity?.let { entity ->
                        webView.loadUrl(entity.originalUrl)
                    }
                }
            }
        )
    }

    if (showSettings) {
        ModalBottomSheet(
            onDismissRequest = { showSettings = false },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ) {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .padding(16.dp)
                    .padding(bottom = 32.dp)
            ) {
                Text("Appearance", style = MaterialTheme.typography.titleLarge)
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(24.dp))
                
                Text("Font Size", style = MaterialTheme.typography.labelLarge)
                androidx.compose.material3.Slider(
                    value = fontSize,
                    onValueChange = { viewModel.updateFontSize(it) },
                    valueRange = 16f..28f,
                    steps = 6
                )
                
                androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(24.dp))
                
                Text("Theme", style = MaterialTheme.typography.labelLarge)
                androidx.compose.foundation.layout.Row(
                    modifier = Modifier.androidx.compose.foundation.layout.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Light" to "light", "Sepia" to "sepia", "Dark" to "dark").forEach { (label, value) ->
                        FilterChip(
                            selected = readerTheme == value,
                            onClick = { viewModel.updateTheme(value) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        }
    }
}
