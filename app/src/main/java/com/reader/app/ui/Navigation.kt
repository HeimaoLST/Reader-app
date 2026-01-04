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
    object Reader : Screen("reader/{urlId}") {
        fun createRoute(urlId: String) = "reader/$urlId"
    }
}

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Login.route) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginClick = { email, password ->
                    // TODO: Connect to ViewModel Auth Logic
                    // For now, simulate success
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }

        composable(Screen.Register.route) {
            RegisterScreen(
                onRegisterClick = { email, password, name ->
                     // TODO: Connect to ViewModel Auth Logic
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToReader = { urlId ->
                    navController.navigate(Screen.Reader.createRoute(urlId))
                }
            )
        }

        composable(Screen.Reader.route) { backStackEntry ->
            val urlId = backStackEntry.arguments?.getString("urlId")
           // ReaderScreen(urlId = urlId) // To be implemented
        }
    }
}
