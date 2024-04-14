package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.vms.MainVMService
import com.aquaguardian.projectaquaguard.ui.components.DashboardTile
import com.aquaguardian.projectaquaguard.ui.components.WeatherWidget
import com.aquaguardian.projectaquaguard.ui.components.reloadWeatherData
import com.aquaguardian.projectaquaguard.ui.core.MainRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.util.Calendar

@Composable
fun HomeView(nav: NavHostController, parentNav: NavHostController, mainVMService: MainVMService) {


    val weatherData by mainVMService.weatherData.collectAsState()
    var refreshing by remember { mutableStateOf(false) }
    val executeWeatherRefresh = remember { mutableStateOf(false) }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            executeWeatherRefresh.value = true
        }
    }

    when {
        executeWeatherRefresh.value -> {
            reloadWeatherData(mainVMService, weatherData, forcedRefresh = true)
            executeWeatherRefresh.value = false
            refreshing = false
        }
    }
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = refreshing),
        onRefresh = { refreshing = true },
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                // TODO: Use System Insets for Top (Status-bar) & Bottom (Navigation Bar)
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = (32.dp + 16.dp))
                .verticalScroll(rememberScrollState())
        ) {
            val (title, profileIcon, overviewCard, tankLabel, cardContainer) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.ic_dummy_acc),
                contentDescription = "About Me Icon",
                modifier = Modifier
                    .wrapContentSize()
                    .constrainAs(profileIcon) {
                        top.linkTo(parent.top)
                        end.linkTo(parent.end)
                    }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .constrainAs(title) {
                        top.linkTo(profileIcon.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
            ) {

                Text(
                    fontSize = 50.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    text = "Good"
                )
                Text(
                    fontSize = 50.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    text = getTimeOfDay()
                )
            }

            WeatherWidget(
                viewModel = mainVMService,
                modifier = Modifier
                    .constrainAs(overviewCard) {
                        top.linkTo(title.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = "TANK STATUS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                modifier = Modifier
                    .constrainAs(tankLabel) {
                        top.linkTo(overviewCard.bottom)
                        start.linkTo(parent.start)
                    }
                    .padding(top = 16.dp, bottom = 16.dp)
            )
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(cardContainer) {
                    top.linkTo(tankLabel.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    DashboardTile(
                        titleText = "Tank #1",
                        subText = "Unfiltered Water",
                        icon = painterResource(id = R.drawable.waterdrop),
                        cardStatus = "Updated 1 min ago.",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 16.dp),
                        onClick = {
                            parentNav.navigate("${MainRoutes.ViewTank.route}/t1")
                        })
                    DashboardTile(
                        titleText = "Tank #2",
                        subText = "Rapid Sand Filter",
                        icon = painterResource(id = R.drawable.waterdrop),
                        cardStatus = "Updated 1 min ago.",
                        modifier = Modifier
                            .weight(1f),
                        onClick = {
                            parentNav.navigate("${MainRoutes.ViewTank.route}/t2")
                        }
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {


                }
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}

@Composable
fun getTimeOfDay(): String {
    val calendar = Calendar.getInstance()
    val timeOfDay: String

    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    timeOfDay = when (hour) {
        in 0..11 -> "Morning"
        in 12..15 -> "Afternoon"
        else -> "Evening"
    }

    return timeOfDay
}