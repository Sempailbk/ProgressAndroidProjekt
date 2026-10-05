package com.example.progr3ss.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.progr3ss.ui.auth.LoginScreen
import com.example.progr3ss.ui.auth.RegisterScreen
import com.example.progr3ss.ui.home.HomeScreen

@Composable
fun AppNavigation(startDestination: String) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoggedIn = {
                    navController.navigate(Routes.HOME){
                        popUpTo(Routes.LOGIN){inclusive = true}
                    }
                },
                onGoToRegister = {navController.navigate(Routes.REGISTER)},
        ) }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegistered = {
                    navController.navigate(Routes.HOME){
                        popUpTo(Routes.LOGIN){inclusive = true}
                    }
                },
                onBack = {navController.popBackStack()},
            )
        }
        composable(Routes.HOME) { HomeScreen() }
    }
}