package com.aquaguardian.projectaquaguard.ui.core

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aquaguardian.projectaquaguard.core.vms.MainVMService
import com.aquaguardian.projectaquaguard.ui.views.AuthenticationView
import com.aquaguardian.projectaquaguard.ui.views.CsvDownloader
import com.aquaguardian.projectaquaguard.ui.views.MainAppView
import com.aquaguardian.projectaquaguard.ui.views.ViewTank
import com.aquaguardian.projectaquaguard.ui.views.WelcomeScreen
import com.aquaguardian.projectaquaguard.ui.views.debug.DataDebugger

/**
 * MainRouter for Pages with fullscreen UI
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AquaGuardianRouter(mainVMService: MainVMService) {
    val navController = rememberNavController()
    val isAuthenticated = MutableLiveData<Boolean>()
    NavHost(
        navController = navController, startDestination =

        when(isAuthenticated.value){
            true -> {
                when(isOpened) {
                    true -> MainRoutes.MainNav.route
                    else -> MainRoutes.WelcomeScreen.route
                }
            }
            /*Change this back to AuthenticationView.route*/
            false -> MainRoutes.WelcomeScreen.route
            null -> {
                Log.w("MainRouter", "ERROR! isAuthenticated FLAG IS NULL!")
                MainRoutes.WelcomeScreen.route
            }
        }

    ) {
        composable(route = MainRoutes.WelcomeScreen.route) {
            WelcomeScreen(navController)
        }

        composable(route = MainRoutes.AuthenticationView.route) {
            AuthenticationView(nav = navController)
        }

        composable(route = MainRoutes.MainNav.route) {
            MainAppView(navController, mainVMService)
        }

        composable(route = MainRoutes.DataDebugger.route) {
            DataDebugger(navController, mainVMService)
        }

        composable(route = "${MainRoutes.CsvDownloader.route}/{tankId}?sensorId={sensorId}") {navBackStackEntry ->
            val tankId = navBackStackEntry.arguments?.getString("tankId")
            val sensorId = navBackStackEntry.arguments?.getString("sensorId")
            tankId?.let {
                CsvDownloader(navController, mainVMService, it, sensorId)
            }
        }

        composable(route ="${ MainRoutes.ViewTank.route}/{tankId}") {navBackStackEntry ->
            val tankId = navBackStackEntry.arguments?.getString("tankId")
            tankId?.let {
                ViewTank(navController, it)

            }
        }

    }
}





