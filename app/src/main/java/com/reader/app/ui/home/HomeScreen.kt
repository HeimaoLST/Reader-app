package com.reader.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.reader.app.ui.components.UrlCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    initialSharedUrl: String? = null,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToReader: (String) -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val urls by viewModel.urls.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
    var clipboardUrl by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    val clipboardHelper = remember { com.reader.app.utils.ClipboardHelper(context) }
    
    androidx.compose.runtime.LaunchedEffect(Unit) {
        if (initialSharedUrl != null) {
            clipboardUrl = initialSharedUrl
            showAddDialog = true
        } else {
            val url = clipboardHelper.getClipboardUrl()
            if (url != null) {
                clipboardUrl = url
                showAddDialog = true
            }
        }
    }
    
    if (showAddDialog) {
        AddUrlDialog(
            initialUrl = clipboardUrl ?: "",
            onDismiss = { 
                showAddDialog = false
                clipboardUrl = null
            },
            onConfirm = { url ->
                viewModel.addUrl(url)
                showAddDialog = false
                clipboardUrl = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "My Reading List",
                        style = MaterialTheme.typography.displaySmall
                    ) 
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(androidx.compose.material.icons.Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true }, 
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add URL")
            }
        }
    ) { paddingValues ->
        if (urls.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Text(
                    text = "No links yet. Add one!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(urls, key = { it.id }) { url ->
                    UrlCard(
                        url = url,
                        onClick = { onNavigateToReader(url.id) },
                        onDelete = { viewModel.deleteUrl(url.id) },
                        onFavorite = { viewModel.toggleFavorite(url) }
                    )
                }
            }
        }
    }
}
