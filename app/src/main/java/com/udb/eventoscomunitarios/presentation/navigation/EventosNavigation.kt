package com.udb.eventoscomunitarios.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.udb.eventoscomunitarios.presentation.screens.auth.LoginScreen
import com.udb.eventoscomunitarios.presentation.screens.auth.RegisterScreen
import com.udb.eventoscomunitarios.presentation.screens.events.DashboardScreen

@Composable
fun EventosNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onNavigateToLogin = {
                    navController.popBackStack()
                },
                onNavigateToDashboard = {
                    navController.navigate("dashboard") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen()
        }
    }
}