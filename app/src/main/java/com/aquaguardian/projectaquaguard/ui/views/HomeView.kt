package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.vms.MainVMService
import com.aquaguardian.projectaquaguard.ui.components.DashboardTile
import com.aquaguardian.projectaquaguard.ui.components.WeatherWidget
import com.aquaguardian.projectaquaguard.ui.components.performWeatherDataRefresh
import com.aquaguardian.projectaquaguard.ui.core.MainRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@Composable
fun HomeView(nav: NavHostController, parentNav: NavHostController, mainVMService: MainVMService) {
    val weatherData by mainVMService.weatherData.collectAsState()
    var refreshing by remember { mutableStateOf(false) }
    val executeWeatherRefresh = remember { mutableStateOf(false) }

    // Animation states
    val animationVisible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animationVisible.value = true
    }

    LaunchedEffect(refreshing) {
        if (refreshing) {
            executeWeatherRefresh.value = true
        }
    }

    when {
        executeWeatherRefresh.value -> {
            performWeatherDataRefresh(mainVMService)
            executeWeatherRefresh.value = false
            refreshing = false
        }
    }

    // Current date for the header
    val currentDate = remember {
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d"))
    }

    // Rotation animation for refresh button
    val refreshRotation by animateFloatAsState(
        targetValue = if (refreshing) 360f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "refresh rotation"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = refreshing),
            onRefresh = { refreshing = true },
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header section with greeting and profile
                HeaderSection(
                    animationVisible = animationVisible.value,
                    currentDate = currentDate,
                    onRefresh = { refreshing = true },
                    refreshRotation = refreshRotation
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Weather widget section
                AnimatedVisibility(
                    visible = animationVisible.value,
                    enter = fadeIn(tween(durationMillis = 500, delayMillis = 300)) +
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                        )
                    ) {
                        WeatherWidget(
                            viewModel = mainVMService,
                            modifier = Modifier
                                .fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Water usage summary
                AnimatedVisibility(
                    visible = animationVisible.value,
                    enter = fadeIn(tween(durationMillis = 500, delayMillis = 400)) +
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    WaterUsageSummary()
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Tank status section
                AnimatedVisibility(
                    visible = animationVisible.value,
                    enter = fadeIn(tween(durationMillis = 500, delayMillis = 500)) +
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    Column {
                        SectionHeader(title = "TANK STATUS")

                        Spacer(modifier = Modifier.height(8.dp))

                        // Tanks grid
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            EnhancedDashboardTile(
                                title = "Tank #1",
                                description = "Unfiltered Water",
                                status = "Normal",
                                level = 78,
                                lastUpdated = "1 min ago",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    parentNav.navigate("${MainRoutes.ViewTank.route}/t1")
                                }
                            )

                            EnhancedDashboardTile(
                                title = "Tank #2",
                                description = "Rapid Sand Filter",
                                status = "Good",
                                level = 92,
                                lastUpdated = "1 min ago",
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    parentNav.navigate("${MainRoutes.ViewTank.route}/t2")
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Add button to view all tanks or add future tanks
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "View All Tanks",
                                    fontFamily = productsans_family,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Water conservation tips section
                AnimatedVisibility(
                    visible = animationVisible.value,
                    enter = fadeIn(tween(durationMillis = 500, delayMillis = 600)) +
                            slideInVertically(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                                ),
                                initialOffsetY = { it / 2 }
                            )
                ) {
                    WaterConservationTips()
                }

                // Add some space at the bottom
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun HeaderSection(
    animationVisible: Boolean,
    currentDate: String,
    onRefresh: () -> Unit,
    refreshRotation: Float
) {
    AnimatedVisibility(
        visible = animationVisible,
        enter = fadeIn(tween(durationMillis = 500)) +
                slideInVertically(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    initialOffsetY = { -50 }
                )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 36.dp)
        ) {
            // Date and greeting
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = currentDate,
                    fontSize = 14.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Good ${getTimeOfDay()}",
                        fontSize = 30.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Action buttons
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Refresh button
                IconButton(
                    onClick = onRefresh,
                    modifier = Modifier
                        .size(36.dp)

                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.graphicsLayer {
                            rotationZ = refreshRotation
                        }
                    )
                }


                // Profile button
                IconButton(
                    onClick = { /* Handle profile click */ },
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dummy_acc),
                        contentDescription = "Profile",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = productsans_family,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "See All",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = productsans_family,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun WaterUsageSummary() {
    Column {
        SectionHeader(title = "WATER USAGE")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Today's Usage",
                            fontSize = 14.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Text(
                            text = "120 liters",
                            fontSize = 24.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Weekly Average",
                            fontSize = 14.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )

                        Text(
                            text = "132 liters",
                            fontSize = 24.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "You're using 9% less water than last week. Great job conserving water!",
                    fontSize = 14.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun EnhancedDashboardTile(
    title: String,
    description: String,
    status: String,
    level: Int,
    lastUpdated: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val statusColor = when (status.lowercase()) {
        "good" -> MaterialTheme.colorScheme.primary
        "normal" -> Color(0xFF4CAF50)
        "warning" -> Color(0xFFFFC107)
        "critical" -> Color(0xFFF44336)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Tank title and status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = description,
                        fontSize = 12.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color = statusColor, shape = CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Water level indicator
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Water Level",
                        fontSize = 12.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "$level%",
                        fontSize = 12.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(level / 100f)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(statusColor)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Last updated
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.waterdrop),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(statusColor),
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.size(4.dp))

                Text(
                    text = "Updated $lastUpdated",
                    fontSize = 10.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun WaterConservationTips() {
    Column {
        SectionHeader(title = "WATER SAVING TIPS")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.waterdrop),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onTertiaryContainer),
                        modifier = Modifier
                            .size(40.dp)
                            .padding(end = 16.dp)
                    )

                    Text(
                        text = "Fix Leaky Faucets",
                        fontSize = 18.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A dripping faucet can waste up to 3,000 gallons of water per year. Check and fix any leaky faucets in your home to conserve water and reduce your utility bill.",
                    fontSize = 14.sp,
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "More Tips",
                        fontSize = 14.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
    }
}

@Composable
fun getTimeOfDay(): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 0..11 -> "Morning"
        in 12..15 -> "Afternoon"
        else -> "Evening"
    }
}