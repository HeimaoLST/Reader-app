package com.reader.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.reader.app.ui.home.HomeScreen
import com.reader.app.ui.settings.SettingsScreen

sealed class BottomNavItem(val screen: Screen, val icon: androidx.compose.ui.graphics.vector.ImageVector, val label: String) {
    object Home : BottomNavItem(Screen.Home, Icons.Default.Home, "Timeline")
    object Collections : BottomNavItem(Screen.Collections, Icons.Default.List, "Collections")
    object Search : BottomNavItem(Screen.Search, Icons.Default.Search, "Search")
    object Settings : BottomNavItem(Screen.Settings, Icons.Default.Settings, "Settings")
}

@Composable
fun MainShell(
    initialSharedUrl: String? = null,
    rootNavController: NavHostController,
    onLogoutSuccess: () -> Unit
) {
    val shellNavController = rememberNavController()
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Collections,
        BottomNavItem.Search,
        BottomNavItem.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                val navBackStackEntry by shellNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                items.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                        onClick = {
                            shellNavController.navigate(item.screen.route) {
                                popUpTo(shellNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(shellNavController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) {
                HomeScreen(
                    initialSharedUrl = initialSharedUrl,
                    onNavigateToReader = { urlId ->
                        rootNavController.navigate(Screen.Reader.createRoute(urlId))
                    },
                    onNavigateToSettings = {
                        shellNavController.navigate(Screen.Settings.route)
                    }
                )
            }
            composable(Screen.Collections.route) {
                com.reader.app.ui.collections.CollectionsScreen()
            }
            composable(Screen.Search.route) {
                com.reader.app.ui.search.SearchScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen(
                    onBackClick = { shellNavController.popBackStack() },
                    onLogoutSuccess = onLogoutSuccess
                )
            }
        }
    }
}
