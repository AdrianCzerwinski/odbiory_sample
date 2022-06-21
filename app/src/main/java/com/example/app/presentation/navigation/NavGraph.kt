package com.example.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app.domain.model.CheckType
import com.example.app.domain.model.Defect
import com.example.app.domain.model.Location
import com.example.app.domain.model.Project
import com.example.app.presentation.components.SplashScreen
import com.example.app.presentation.screens.contractors.Contractors
import com.example.app.presentation.screens.defect.DefectScreen
import com.example.app.presentation.screens.defects_list.DefectsListScreen
import com.example.app.presentation.screens.locations.Locations
import com.example.app.presentation.screens.login_register.LoginScreen
import com.example.app.presentation.screens.login_register.RegisterScreen
import com.example.app.presentation.screens.main.MainScreen

@Composable
fun SetupNavGraph(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screen.Splash.route) {
        composable(route = Screen.Register.route) {
            RegisterScreen(navController = navHostController)
        }
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navHostController)
        }
        composable(route = Screen.Splash.route) {
            SplashScreen(navController = navHostController)
        }
        composable(route = Screen.Main.route) {
            MainScreen(navController = navHostController)
        }
        composable(route = Screen.Contractors.route) {
            Contractors(navController = navHostController)
        }
        composable(
            route = Screen.Location.route
        ) {
            val project =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<Project>("project")
            Locations(navController = navHostController, project = project)
        }
        composable(route = Screen.DefectsList.route) {
            val location =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<Location>("location")
            val project =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<Project>("project")
            DefectsListScreen(navController = navHostController, location = location, project = project)
        }
        composable(route= Screen.Defect.route) {
            val location =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<Location>("location")
            val project =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<Project>("project")
            val defect =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<Defect>("defect")
            val typeCheck =
                navHostController.previousBackStackEntry?.savedStateHandle?.get<CheckType>("typeCheck")
            DefectScreen(
                location = location,
                navController = navHostController,
                project = project,
                defect = defect,
                typeCheck = typeCheck
            )
        }
    }

}