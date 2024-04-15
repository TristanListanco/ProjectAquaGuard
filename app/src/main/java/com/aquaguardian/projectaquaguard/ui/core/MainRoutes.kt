package com.aquaguardian.projectaquaguard.ui.core

sealed class MainRoutes(val route: String) {
    data object WelcomeScreen : MainRoutes("wscreen")
    data object MainNav : MainRoutes("main")
    data object AuthenticationView : MainRoutes("auth")
    data object ViewTank: MainRoutes("view_tank")

    data object DataDebugger: MainRoutes("view_tank")
}