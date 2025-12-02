package com.kkwieer.erpwebapp.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object LMSPortal : Screen("lms_portal")
    object MobileAppDev : Screen("mobile_app_dev")
    object AERPLogin : Screen("aerp_login")
    object DeveloperInfo : Screen("developer_info")
}
