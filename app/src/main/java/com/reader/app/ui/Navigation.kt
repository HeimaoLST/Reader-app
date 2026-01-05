package com.reader.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.reader.app.ui.auth.LoginScreen
import com.reader.app.ui.auth.RegisterScreen
import com.reader.app.ui.home.HomeScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Collections : Screen("collections")
    object Search : Screen("search")
    object Settings : Screen("settings")
    object Reader : Screen("reader/{urlId}") {
        fun createRoute(urlId: String) = "reader/$urlId"
    }
}

@Composable
fun ReaderNavigation(
    initialSharedUrl: String? = null,
    authRepository: com.reader.app.domain.repository.AuthRepository = hiltViewModel<com.reader.app.ui.auth.AuthViewModel>().repository // Helper way to get it or via Hilt DI in Activity
) {
    val navController = rememberNavController()
    val startDestination = if (authRepository.isLoggedIn()) "main" else Screen.Login.route

    NavHost(navController = navController, startDestination = startDestination) {
        composable("main") {
            MainShell(
                initialSharedUrl = initialSharedUrl,
                rootNavController = navController,
                onLogoutSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Login.route) {
            val viewModel: com.reader.app.ui.auth.AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val state by viewModel.authState.androidx.compose.runtime.collectAsState()

            androidx.compose.runtime.LaunchedEffect(state) {
                if (state is com.reader.app.ui.auth.AuthState.Success) {
                    navController.navigate("main") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            // ... rest of Login composable

            LoginScreen(
                onLoginClick = { email, password ->
                    viewModel.login(email, password)
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                isLoading = state is com.reader.app.ui.auth.AuthState.Loading,
                errorMessage = (state as? com.reader.app.ui.auth.AuthState.Error)?.message
            )
        }

        composable(Screen.Register.route) {
            val viewModel: com.reader.app.ui.auth.AuthViewModel = androidx.hilt.navigation.compose.hiltViewModel()
            val state by viewModel.authState.androidx.compose.runtime.collectAsState()

            androidx.compose.runtime.LaunchedEffect(state) {
                if (state is com.reader.app.ui.auth.AuthState.Success) {
                    navController.navigate("main") {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            RegisterScreen(
                onRegisterClick = { email, password, name ->
                    viewModel.register(email, password, name)
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                isLoading = state is com.reader.app.ui.auth.AuthState.Loading,
                errorMessage = (state as? com.reader.app.ui.auth.AuthState.Error)?.message
            )
        }

        }

        composable(Screen.Reader.route) { backStackEntry ->
            val urlId = backStackEntry.arguments?.getString("urlId")
            com.reader.app.ui.reader.ReaderScreen(
                urlId = urlId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
