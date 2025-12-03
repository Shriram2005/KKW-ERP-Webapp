package com.kkwieer.erpwebapp.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object WebView : Screen("webview/{title}/{url}") {
        fun createRoute(title: String, url: String): String {
            return "webview/${
                java.net.URLEncoder.encode(
                    title,
                    "UTF-8"
                )
            }/${java.net.URLEncoder.encode(url, "UTF-8")}"
        }
    }

    data object Settings : Screen("settings")
    data object DeveloperInfo : Screen("developer_info")
}
