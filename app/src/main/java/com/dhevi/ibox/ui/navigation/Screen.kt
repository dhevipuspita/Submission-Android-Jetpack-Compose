package com.dhevi.ibox.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Cart : Screen("cart")
    object Profile : Screen("profile")
    object DetailIbox : Screen("home/{iboxId}") {
        fun createRoute(iboxId: Long) = "home/$iboxId"
    }
}
