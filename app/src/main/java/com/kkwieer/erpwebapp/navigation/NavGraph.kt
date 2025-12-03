package com.kkwieer.erpwebapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kkwieer.erpwebapp.screens.DeveloperInfoScreen
import com.kkwieer.erpwebapp.screens.HomeScreen
import com.kkwieer.erpwebapp.screens.SettingsScreen
import com.kkwieer.erpwebapp.screens.SplashScreen
import com.kkwieer.erpwebapp.screens.WebViewScreen
import java.net.URLDecoder

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash Screen
        composable(route = Screen.Splash.route) {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Home Screen
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToWebView = { title, url ->
                    navController.navigate(Screen.WebView.createRoute(title, url))
                },
                onNavigateToSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onNavigateToDeveloperInfo = {
                    navController.navigate(Screen.DeveloperInfo.route)
                }
            )
        }

        // WebView Screen with dynamic parameters
        composable(
            route = Screen.WebView.route,
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("url") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: "Portal"
            val url = backStackEntry.arguments?.getString("url")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""

            WebViewScreen(
                title = title,
                url = url,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Settings Screen
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Developer Info Screen
        composable(route = Screen.DeveloperInfo.route) {
            DeveloperInfoScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
