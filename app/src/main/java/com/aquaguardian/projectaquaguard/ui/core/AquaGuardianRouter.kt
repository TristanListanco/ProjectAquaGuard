package com.aquaguardian.projectaquaguard.ui.core

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.aquaguardian.projectaquaguard.BottomNavigationItem
import com.aquaguardian.projectaquaguard.ui.views.HomeView
import com.aquaguardian.projectaquaguard.ui.views.ViewTank



const val isOpened: Boolean = false

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AquaGuardianRouter(){
    val navController = rememberNavController()

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Articles",
            selectedIcon = Icons.AutoMirrored.Filled.List,
            unselectedIcon = Icons.AutoMirrored.Outlined.List,
        ),
        BottomNavigationItem(
            title = "About",
            selectedIcon = Icons.Filled.Info,
            unselectedIcon = Icons.Outlined.Info,
        ),



        )
    var selectedItemIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            // navController.navigate(item.title)
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon = {

                            Icon(
                                imageVector = if (index == selectedItemIndex) {
                                    item.selectedIcon
                                } else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        }
                    )
                }
            }
        },
    ){
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


}