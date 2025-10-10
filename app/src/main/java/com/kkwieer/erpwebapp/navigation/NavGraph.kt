package com.kkwieer.erpwebapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.kkwieer.erpwebapp.screens.HomeScreen
import com.kkwieer.erpwebapp.screens.SplashScreen
import com.kkwieer.erpwebapp.screens.WebViewScreen

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
                onNavigateToLMS = {
                    navController.navigate(Screen.LMSPortal.route)
                },
                onNavigateToMobileAppDev = {
                    navController.navigate(Screen.MobileAppDev.route)
                },
                onNavigateToAERP = {
                    navController.navigate(Screen.AERPLogin.route)
                }
            )
        }

        // LMS Portal WebView
        composable(route = Screen.LMSPortal.route) {
            WebViewScreen(
                title = "LMS Portal",
                url = "http://era.mkcl.org/lms/#/15477477481473922139",
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Mobile App Development Course WebView
        composable(route = Screen.MobileAppDev.route) {
            WebViewScreen(
                title = "Mobile App Development",
                url = "https://eranx.mkcl.org/learner/login",
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // AERP Login WebView
        composable(route = Screen.AERPLogin.route) {
            WebViewScreen(
                title = "AERP Login",
                url = "https://aerp.kkwagh.edu.in",
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
