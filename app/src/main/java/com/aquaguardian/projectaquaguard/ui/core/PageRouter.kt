package com.aquaguardian.projectaquaguard.ui.core

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.BottomNavigationItem
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import com.aquaguardian.projectaquaguard.ui.views.AboutView
import com.aquaguardian.projectaquaguard.ui.views.ArticleView
import com.aquaguardian.projectaquaguard.ui.views.HomeView


const val isOpened: Boolean = false

/**
 * Router for the Pages like Home, Dashboard (Where the Navigation Bar is Needed)
 */

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PageRouter(mainNavController: NavHostController) {
    val navController = rememberNavController()

    val items = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home
        ),
        BottomNavigationItem(
            title = "Articles",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.MailOutline,
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

    val scrollState = rememberScrollState()
    val maxScroll = remember { mutableFloatStateOf(0f) }

    val ctx = LocalContext.current
    val motionScene = remember {
        ctx.resources.openRawResource(R.raw.main_pages_collapsible_appbar).readBytes()
            .decodeToString()
    }

    Scaffold(
        topBar = {
            MotionLayout(
                motionScene = MotionScene(content = motionScene),
                progress = maxScroll.floatValue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) {
                Text(
                    fontSize = 50.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    text = "Good",
                    modifier = Modifier.layoutId("title1")
                )
                Text(
                    fontSize = 50.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    text = "Morning!",
                    modifier = Modifier.layoutId("title2")
                )
            }
        },
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = {
                            selectedItemIndex = index
                            when (selectedItemIndex) {
                                0 -> navController.navigate(PageRoutes.Home.route)
                                1 -> navController.navigate(PageRoutes.ArticleView.route)
                                else -> navController.navigate(PageRoutes.AboutView.route)
                            }
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
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
        ) {
            Slider(value = 0f, onValueChange = {
                maxScroll.floatValue = it
            })
            NavHost(
                navController = navController,
                startDestination =
                PageRoutes.Home.route,

                ) {
                maxScroll.floatValue =
                    if (scrollState.maxValue > 0) (scrollState.value / scrollState.maxValue).toFloat() else 0f
                composable(route = PageRoutes.AboutView.route) {
                    AboutView(navController)
                }

                composable(route = PageRoutes.ArticleView.route) {
                    ArticleView(navController)
                }

                composable(route = PageRoutes.Home.route) {
                    HomeView(navController, mainNavController)
                }

            }
        }

    }
}