package com.aquaguardian.projectaquaguard.ui.core

sealed class PageRoutes(val route: String) {
    data object Home : MainRoutes("home")
    data object AboutView : MainRoutes("about")
    data object ArticleView : MainRoutes("article")

}