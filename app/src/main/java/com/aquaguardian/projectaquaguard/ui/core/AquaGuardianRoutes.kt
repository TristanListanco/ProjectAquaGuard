package com.aquaguardian.projectaquaguard.ui.core

sealed class AquaGuardianRoutes (val route: String){
    object WelcomeScreen: AquaGuardianRoutes("wscreen")
    object Home: AquaGuardianRoutes("home")
    object ViewTank: AquaGuardianRoutes("viewtank")
}