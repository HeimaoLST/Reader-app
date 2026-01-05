package com.reader.app.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.reader.app.ui.auth.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    onLogoutSuccess: () -> Unit,
    authViewModel: AuthViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Account", style = MaterialTheme.typography.titleMedium)
            
            ListItem(
                headlineContent = { Text("Logout") },
                supportingContent = { Text("Sign out of your account") },
                leadingContent = { Icon(Icons.Default.ExitToApp, contentDescription = null) },
                modifier = androidx.compose.foundation.clickable {
                    authViewModel.logout()
                    onLogoutSuccess()
                }
            )

            Divider()

            Text("Data", style = MaterialTheme.typography.titleMedium)

            ListItem(
                headlineContent = { Text("Sync Now") },
                supportingContent = { Text("Force immediate synchronization") },
                leadingContent = { Icon(Icons.Default.Refresh, contentDescription = null) },
                modifier = androidx.compose.foundation.clickable {
                    // Logic for manual sync could go here if exposed via ViewModel
                }
            )
        }
    }
}
