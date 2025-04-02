package com.example.papka.ui.navigation

import android.net.Uri
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.navArgument
import com.example.papka.ui.screens.FolderScreen
import com.example.papka.ui.screens.HomeScreen

@Composable
fun AppNavigation(navController: NavController = rememberNavController()) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "home_screen"
    ) {
        composable("home_screen") {
            HomeScreen(navController = navController)
        }

        composable(
            "folder_screen/{folderPath}",
            arguments = listOf(navArgument("folderPath") { type = NavType.StringType })
        ) { backStackEntry ->
            val folderPath = backStackEntry.arguments?.getString("folderPath")?.let { Uri.decode(it) } ?: ""
            FolderScreen(navController = navController, folderPath = folderPath)
        }

    }
}