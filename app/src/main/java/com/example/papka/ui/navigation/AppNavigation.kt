package com.example.papka.ui.navigation

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import com.example.papka.ui.screens.FolderScreen
import com.example.papka.ui.screens.HomeScreen

object NavRoutes {
    const val HOME = "home"
    const val FOLDER = "folder/{folderName}"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME // Исправлено: вместо mainScreenRoute используем определённый маршрут
    ) {
        composable(NavRoutes.HOME) {
            HomeScreen(navController)
        }

        composable(NavRoutes.FOLDER) { backStackEntry ->
            val folderName =
                backStackEntry.arguments?.getString("folderName") ?: "Неизвестная папка"
            FolderScreen(navController, folderName)
        }
    }
}