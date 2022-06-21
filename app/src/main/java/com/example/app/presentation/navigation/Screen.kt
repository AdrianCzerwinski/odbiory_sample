package com.example.app.presentation.navigation

sealed class Screen(val route: String) {
    object Register: Screen(route = "register_screen")
    object Login: Screen(route = "login_screen")
    object DefectsList: Screen(route = "defects_list_screen")
    object Splash: Screen(route = "splash_screen")
    object Main: Screen(route = "main_screen")
    object Contractors: Screen(route = "contractors")
    object Location: Screen(route = "locations")
    object Defect: Screen(route = "defect")
//    object Camera: Screen(route = "camera")
}
