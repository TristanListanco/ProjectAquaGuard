package com.aquaguardian.projectaquaguard.ui.core

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.aquaguardian.projectaquaguard.ui.views.HomeView
import com.aquaguardian.projectaquaguard.ui.views.ViewTank
import com.google.accompanist.navigation.animation.rememberAnimatedNavController


val isOpened: Boolean = false;

@Composable
@OptIn(ExperimentalAnimationApi::class)
fun AquaGuardianRouter(){
    val navController = rememberNavController()
    NavHost(
        navController = navController, startDestination =
        when(isOpened) {
            true -> AquaGuardianRoutes.WelcomeScreen.route
            false -> AquaGuardianRoutes.Home.route
        }

    ) {
        composable(route = AquaGuardianRoutes.ViewTank.route) {
            ViewTank(nav = navController)
        }

        composable(route = AquaGuardianRoutes.WelcomeScreen.route) {
            HomeView(nav = navController)
        }

        composable(route = AquaGuardianRoutes.Home.route) {
            HomeView(nav = navController)
        }

    }
}