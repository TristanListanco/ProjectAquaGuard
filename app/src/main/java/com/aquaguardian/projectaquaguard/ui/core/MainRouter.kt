package com.aquaguardian.projectaquaguard.ui.core

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aquaguardian.projectaquaguard.ui.views.AuthenticationView
import com.aquaguardian.projectaquaguard.ui.views.MainAppView
import com.aquaguardian.projectaquaguard.ui.views.WelcomeScreen
import com.google.firebase.auth.FirebaseAuth

fun observeAuthenticationState(): MutableLiveData<Boolean> {
    val isAuthenticated = MutableLiveData<Boolean>()

    FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
        isAuthenticated.value = firebaseAuth.currentUser != null
    }

    return isAuthenticated
}

/**
 * MainRouter for Pages with fullscreen UI
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AquaGuardianRouter() {
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
            MainAppView(navController)
        }

    }
}





