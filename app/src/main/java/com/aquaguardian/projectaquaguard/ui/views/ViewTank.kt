package com.aquaguardian.projectaquaguard.ui.views

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.CalendarViewMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.BuildConfig
import com.aquaguardian.projectaquaguard.ui.components.SensorCardEnhanced
import com.aquaguardian.projectaquaguard.ui.core.MainRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Data class to hold sensor data
data class SensorData(
    val value: String,
    val timestamp: String,
    val status: Status = Status.NORMAL,
    val unit: String = ""
)

// Enum for water quality status
enum class Status {
    NORMAL, WARNING, CRITICAL, UNKNOWN
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewTank(nav: NavHostController, tankId: String? = null) {
    val ctx = LocalContext.current

    // State for all sensor data
    val wlvlData = remember { mutableStateOf<SensorData?>(null) }
    val wtempData = remember { mutableStateOf<SensorData?>(null) }
    val pHData = remember { mutableStateOf<SensorData?>(null) }
    val turbidityData = remember { mutableStateOf<SensorData?>(null) }
    val tdsData = remember { mutableStateOf<SensorData?>(null) }

    // State for historical data
    val phDataList = remember { mutableListOf<Pair<String, String>>() }
    val tdsDataList = remember { mutableListOf<Pair<String, String>>() }
    val turbidityDataList = remember { mutableListOf<Pair<String, String>>() }
    val wtempDataList = remember { mutableListOf<Pair<String, String>>() }
    val wlvlDataList = remember { mutableListOf<Pair<String, String>>() }

    // UI States
    val isLoading = remember { mutableStateOf(true) }
    val isRefreshing = remember { mutableStateOf(false) }
    val error = remember { mutableStateOf<String?>(null) }
    val openAboutTankDialog = remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(true) }
    val waterLevelHeightDP = remember { mutableIntStateOf(0) }

    // Get database references
    val database = FirebaseDatabase.getInstance()
    val uid = "pvbkhv0iJ4RViT9qlJzBeNkndPS2"

    val wlvlPath = "usrdata/$uid/$tankId/wlvl"
    val wtempPath = "usrdata/$uid/$tankId/wtemp"
    val pHDataPath = "usrdata/$uid/$tankId/phData"
    val turbidityDataPath = "usrdata/$uid/$tankId/turbidity"
    val tdsPath = "usrdata/$uid/$tankId/tds"

    val ref1 = database.getReference(wlvlPath)
    val ref2 = database.getReference(wtempPath)
    val ref3 = database.getReference(pHDataPath)
    val ref4 = database.getReference(turbidityDataPath)
    val ref5 = database.getReference(tdsPath)

    // Fetch data on launch
    LaunchedEffect(tankId) {
        if (tankId == null) {
            nav.navigate(MainRoutes.MainNav.route)
            Toast.makeText(ctx, "Internal Error: Tank ID Not Specified", Toast.LENGTH_SHORT).show()
            return@LaunchedEffect
        }

        // Set up Firebase listeners
        val wlvlListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.lastOrNull()?.let { recentSnapshot ->
                    val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                    val value = sortedEntries[0].value.toString().replace(".00", "")
                    wlvlData.value = SensorData(
                        value = value,
                        timestamp = recentSnapshot.key.toString(),
                        status = getStatusForWaterLevel(value),
                        unit = "%"
                    )

                    // Update water level display
                    val wlvl = value.toIntOrNull() ?: 0
                    waterLevelHeightDP.intValue = wlvl.map(0, 100, 0, 150)
                    expanded = !expanded

                    ref1.removeEventListener(this)
                    isLoading.value = false
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        val wtempListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.lastOrNull()?.let { recentSnapshot ->
                    val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                    val value = removeDecimal(sortedEntries[0].value.toString())
                    wtempData.value = SensorData(
                        value = value,
                        timestamp = recentSnapshot.key.toString(),
                        status = getStatusForTemp(value),
                        unit = "°C"
                    )

                    ref2.removeEventListener(this)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        val phListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.lastOrNull()?.let { recentSnapshot ->
                    val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                    val value = sortedEntries[0].value.toString()
                    pHData.value = SensorData(
                        value = value,
                        timestamp = recentSnapshot.key.toString(),
                        status = getStatusForPh(value),
                        unit = ""
                    )

                    ref3.removeEventListener(this)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        val turbidityListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.lastOrNull()?.let { recentSnapshot ->
                    val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                    val value = sortedEntries[0].value.toString()
                    turbidityData.value = SensorData(
                        value = value,
                        timestamp = recentSnapshot.key.toString(),
                        status = getStatusForTurbidity(value),
                        unit = "NTU"
                    )

                    ref4.removeEventListener(this)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        val tdsListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.lastOrNull()?.let { recentSnapshot ->
                    val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                    val value = sortedEntries[0].value.toString()
                    tdsData.value = SensorData(
                        value = value,
                        timestamp = recentSnapshot.key.toString(),
                        status = getStatusForTds(value),
                        unit = "ppm"
                    )

                    ref5.removeEventListener(this)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
            }
        }

        // Set up child event listeners
        setupChildEventListener(ref1, wlvlDataList) {
            ref1.addListenerForSingleValueEvent(wlvlListener)
        }

        setupChildEventListener(ref2, wtempDataList) {
            ref2.addListenerForSingleValueEvent(wtempListener)
        }

        setupChildEventListener(ref3, phDataList) {
            ref3.addListenerForSingleValueEvent(phListener)
        }

        setupChildEventListener(ref4, turbidityDataList) {
            ref4.addListenerForSingleValueEvent(turbidityListener)
        }

        setupChildEventListener(ref5, tdsDataList) {
            ref5.addListenerForSingleValueEvent(tdsListener)
        }
    }

    // Show dialog if requested
    if (openAboutTankDialog.value) {
        AboutTankDialog(openAboutTankDialog, tankId)
    }

    // Main view
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp)
        ) {
            // Top app bar
            TopBar(
                tankName = when (tankId) {
                    "t1" -> "Tank 1"
                    "t2" -> "Tank 2"
                    "t3" -> "Tank 3"
                    "t4" -> "Tank 4"
                    else -> "Unknown Tank"
                },
                onBackClick = { nav.popBackStack() },
                onInfoClick = { openAboutTankDialog.value = true },
                onRefreshClick = {
                    isRefreshing.value = true
                    // In a real implementation, this would re-fetch data
                    // For this demo, we'll just simulate a refresh
                    kotlinx.coroutines.MainScope().launch {
                        kotlinx.coroutines.delay(2000)
                        isRefreshing.value = false
                    }
                },
                isRefreshing = isRefreshing.value,
                onDebugClick = if (BuildConfig.ENABLE_DATA_DEBUGGER) {{ nav.navigate(MainRoutes.DataDebugger.route) }} else null
            )

            // Loading state
            AnimatedVisibility(
                visible = isLoading.value && !isRefreshing.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(500.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(60.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 5.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading tank data...",
                            style = MaterialTheme.typography.bodyMedium,
                            fontFamily = productsans_family
                        )
                    }
                }
            }

            // Content when loaded
            AnimatedVisibility(
                visible = !isLoading.value || isRefreshing.value,
                enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Water level indicator
                    WaterLevelCard(
                        level = wlvlData.value?.value?.toFloatOrNull()?.toInt() ?: 0,
                        isRefreshing = isRefreshing.value,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sensor cards section
                    SensorSection(
                        pHData = pHData.value,
                        tdsData = tdsData.value,
                        turbidityData = turbidityData.value,
                        wtempData = wtempData.value
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Tabular data section
                    TabularDataSection(
                        phDataList = phDataList,
                        tdsDataList = tdsDataList,
                        turbidityDataList = turbidityDataList,
                        wtempDataList = wtempDataList,
                        wlvlDataList = wlvlDataList
                    )
                }
            }
        }
    }
}

@Composable
fun TopBar(
    tankName: String,
    onBackClick: () -> Unit,
    onInfoClick: () -> Unit,
    onRefreshClick: () -> Unit,
    isRefreshing: Boolean,
    onDebugClick: (() -> Unit)? = null
) {
    val refreshRotation by animateFloatAsState(
        targetValue = if (isRefreshing) 360f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "refresh rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        // Back button
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .shadow(elevation = 2.dp, shape = CircleShape)
                .background(MaterialTheme.colorScheme.surface, CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        // Title
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = tankName,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Water Quality Monitoring",
                fontSize = 14.sp,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Action buttons
        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Debug button (if enabled)
            if (onDebugClick != null) {
                IconButton(
                    onClick = onDebugClick,
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(elevation = 2.dp, shape = CircleShape)
                        .background(MaterialTheme.colorScheme.errorContainer, CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Debug",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            // Refresh button
            IconButton(
                onClick = onRefreshClick,
                enabled = !isRefreshing,
                modifier = Modifier
                    .size(40.dp)
                    .shadow(elevation = 2.dp, shape = CircleShape)
                    .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Data",
                    tint = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.graphicsLayer(rotationZ = refreshRotation)
                )
            }

            // Info button
            IconButton(
                onClick = onInfoClick,
                modifier = Modifier
                    .size(40.dp)
                    .shadow(elevation = 2.dp, shape = CircleShape)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Tank Information",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Title section below the app bar
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Water Level",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = productsans_family,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}

@Composable
fun WaterLevelCard(
    level: Int,
    isRefreshing: Boolean,
    modifier: Modifier = Modifier
) {
    val animatedLevel by animateFloatAsState(
        targetValue = level.toFloat(),
        animationSpec = tween(1000),
        label = "water level"
    )

    val waveOffset by rememberInfiniteTransition(label = "wave").animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "wave animation"
    )

    // Get colors inside the Composable function
    val waterColor = when {
        level >= 80 -> MaterialTheme.colorScheme.primary
        level >= 40 -> MaterialTheme.colorScheme.tertiary
        else -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = modifier.height(200.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Water level background
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            )

            // Water fill with wave effect
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight((animatedLevel / 100f).coerceIn(0f, 1f))
                    .align(Alignment.BottomCenter)
                    .drawBehind {
                        // Access theme colors within the drawBehind block to ensure they're used in a Composable context
                        val alphaWater1 = 0.8f  // Use local variables instead of directly using theme colors
                        val alphaWater2 = 0.6f
                        val currentWaterColor = waterColor  // Store the current water color

                        // Draw wave effect at the top of the water
                        val path = Path()

                        val waveHeight = size.height * 0.05f
                        val startY = 0f

                        path.moveTo(0f, startY)

                        // Create wave pattern
                        val waveCount = 6
                        val waveWidth = size.width / waveCount

                        for (i in 0..waveCount) {
                            val x1 = waveWidth * i - waveOffset % waveWidth
                            val y1 = startY + if (i % 2 == 0) waveHeight else -waveHeight
                            val x2 = waveWidth * (i + 1) - waveOffset % waveWidth
                            val y2 = startY

                            path.quadraticBezierTo(
                                x1 + waveWidth / 2, y1,
                                x2, y2
                            )
                        }

                        // Close the path
                        path.lineTo(size.width, size.height)
                        path.lineTo(0f, size.height)
                        path.close()

                        // Draw the water with the stored color
                        drawPath(
                            path = path,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    currentWaterColor.copy(alpha = alphaWater1),
                                    currentWaterColor.copy(alpha = alphaWater2)
                                )
                            )
                        )
                    }
            )

            // Level text and percentage in the center
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${animatedLevel.toInt()}%",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = productsans_family,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = when {
                        level >= 80 -> "Optimal"
                        level >= 40 -> "Normal"
                        else -> "Low"
                    },
                    fontSize = 18.sp,
                    fontFamily = productsans_family,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (isRefreshing) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        modifier = Modifier.width(100.dp),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }

            // Level marker on the side
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(24.dp)
                    .align(Alignment.CenterEnd)
                    .padding(8.dp)
            ) {
                // Level marks
                for (i in 0..10) {
                    val y = i * 10

                    // Get the marker color inside the loop
                    val markerColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (i % 5 == 0) 1f else 0.6f)
                            .height(1.dp)
                            .background(markerColor)
                            .align(Alignment.TopStart)
                            .offset(y = (180 - y * 1.8).dp)
                    )

                    if (i % 5 == 0 && i > 0) {
                        Text(
                            text = "${i * 10}",
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(y = (177 - y * 1.8).dp, x = (-12).dp)
                        )
                    }
                }

                // Current level marker
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(
                            MaterialTheme.colorScheme.onSurface,
                            RoundedCornerShape(1.dp)
                        )
                        .align(Alignment.TopStart)
                        .offset(y = (180 - (animatedLevel * 1.8)).dp)
                )
            }
        }
    }
}

@Composable
fun SensorSection(
    pHData: SensorData?,
    tdsData: SensorData?,
    turbidityData: SensorData?,
    wtempData: SensorData?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Section title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "WATER QUALITY PARAMETERS",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1f))

            // View history button
            TextButton(
                onClick = { /* Navigate to history screen */ }
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "View History",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "History",
                        fontSize = 14.sp,
                        fontFamily = productsans_family,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        // Sensor cards grid
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // pH Card
                SensorCardEnhanced(
                    sensorType = stringResource(id = R.string.ph_text),
                    sensorValue = pHData?.value ?: "...",
                    cardStatus = "Updated - Just Now",
                    status = getStatusForPh(pHData?.value),
                    icon = painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.fillMaxWidth(),
                    description = "Acidity/Alkalinity",
                    idealRange = "6.5 - 8.5"
                )

                // Turbidity Card
                SensorCardEnhanced(
                    sensorType = stringResource(id = R.string.turbidityTSS_text),
                    sensorValue = turbidityData?.value ?: "...",
                    cardStatus = "Updated - Just Now",
                    status = getStatusForTurbidity(turbidityData?.value),
                    icon = painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.fillMaxWidth(),
                    description = "Water Clarity",
                    idealRange = "< 5 NTU"
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // TDS Card
                SensorCardEnhanced(
                    sensorType = stringResource(id = R.string.tds_text),
                    sensorValue = tdsData?.value ?: "...",
                    cardStatus = "Updated - Just Now",
                    status = getStatusForTds(tdsData?.value),
                    icon = painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.fillMaxWidth(),
                    description = "Dissolved Solids",
                    idealRange = "< 500 ppm"
                )

                // Temperature Card
                SensorCardEnhanced(
                    sensorType = stringResource(id = R.string.temp_text),
                    sensorValue = (wtempData?.value ?: "...") + if (wtempData?.value != null) " °C" else "",
                    cardStatus = "Updated - Just Now",
                    status = getStatusForTemp(wtempData?.value),
                    icon = painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.fillMaxWidth(),
                    description = "Water Temperature",
                    idealRange = "20 - 30 °C"
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabularDataSection(
    phDataList: List<Pair<String, String>>,
    tdsDataList: List<Pair<String, String>>,
    turbidityDataList: List<Pair<String, String>>,
    wtempDataList: List<Pair<String, String>>,
    wlvlDataList: List<Pair<String, String>>
) {
    // View options state - moved inside the Composable
    var selectedOption by remember { mutableStateOf(0) }
    val viewOptions = remember { listOf("Table", "Chart") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        // Section title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, start = 8.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "HISTORICAL DATA",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.weight(1f))

            // View options
            Row(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(20.dp)
                    )
                    .padding(4.dp)
            ) {
                viewOptions.forEachIndexed { index, option ->
                    // Get the colors inside the loop
                    val bgColor = if (selectedOption == index)
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        Color.Transparent
                    val textColor = if (selectedOption == index)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(bgColor)
                            .clickable { selectedOption = index }
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (index == 0) Icons.Default.CalendarViewMonth else Icons.Default.ShowChart,
                                contentDescription = option,
                                tint = textColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = option,
                                fontSize = 12.sp,
                                fontFamily = productsans_family,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }

        // Sensor data tabs
        val listOfSensors = listOf(
            "pH Data",
            "TDS Data",
            "Turbidity",
            "Temperature",
            "Water Level"
        )

        val pagerState = rememberPagerState(pageCount = { listOfSensors.size })
        val scope = rememberCoroutineScope()

        // Tab row
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            divider = {
                Divider(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        ) {
            listOfSensors.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        // Get the color inside the text Composable
                        val tabTextColor = if (pagerState.currentPage == index)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant

                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontFamily = productsans_family,
                            maxLines = 1,
                            color = tabTextColor
                        )
                    }
                )
            }
        }

        // Data view
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(top = 8.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                if (selectedOption == 0) { // Table view
                    TableView(
                        dataList = when (page) {
                            0 -> phDataList
                            1 -> tdsDataList
                            2 -> turbidityDataList
                            3 -> wtempDataList
                            4 -> wlvlDataList
                            else -> emptyList()
                        },
                        title = listOfSensors[page],
                        unit = when (page) {
                            0 -> "pH"
                            1 -> "ppm"
                            2 -> "NTU"
                            3 -> "°C"
                            4 -> "%"
                            else -> ""
                        }
                    )
                } else { // Chart view
                    ChartPlaceholder(
                        dataType = listOfSensors[page],
                        isEmpty = when (page) {
                            0 -> phDataList.isEmpty()
                            1 -> tdsDataList.isEmpty()
                            2 -> turbidityDataList.isEmpty()
                            3 -> wtempDataList.isEmpty()
                            4 -> wlvlDataList.isEmpty()
                            else -> true
                        }
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FilledTonalButton(
                onClick = { /* Export data */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(
                    text = "Export Data",
                    fontFamily = productsans_family,
                    fontSize = 14.sp
                )
            }

            Button(
                onClick = { /* View full history */ },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(vertical = 12.dp)
            ) {
                Text(
                    text = "View Full History",
                    fontFamily = productsans_family,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun TableView(
    dataList: List<Pair<String, String>>,
    title: String,
    unit: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "${dataList.size - 1} entries",
                fontSize = 12.sp,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Table header - get colors inside the Composable
        val headerBgColor = MaterialTheme.colorScheme.surfaceVariant
        val headerTextColor = MaterialTheme.colorScheme.onSurfaceVariant

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerBgColor, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Text(
                text = "TIMESTAMP",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                color = headerTextColor,
                modifier = Modifier.weight(1.5f)
            )

            Text(
                text = "VALUE ($unit)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                color = headerTextColor,
                textAlign = TextAlign.End,
                modifier = Modifier.weight(1f)
            )
        }

        // Data rows
        if (dataList.isEmpty() || dataList.size <= 1) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No data available",
                    fontSize = 14.sp,
                    fontFamily = productsans_family,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // Skip the first entry which is the header
                dataList.drop(1).forEachIndexed { index, (timestamp, value) ->
                    DataListItem(
                        timestamp = timestamp,
                        value = value + if (unit.isNotEmpty()) " $unit" else "",
                        isStartOfList = index == 0,
                        isEndOfList = index == dataList.size - 2
                    )
                }
            }
        }
    }
}

@Composable
fun DataListItem(
    timestamp: String,
    value: String,
    isStartOfList: Boolean,
    isEndOfList: Boolean
) {
    // Get colors inside the Composable
    val dividerColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val textColor = MaterialTheme.colorScheme.onSurface

    if (!isStartOfList) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            color = dividerColor
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Format timestamp
        val formattedTimestamp = try {
            val date = timestamp.toLongOrNull()?.let { Date(it) }
            date?.let {
                SimpleDateFormat("MMM dd, HH:mm:ss", Locale.getDefault()).format(it)
            } ?: timestamp
        } catch (e: Exception) {
            timestamp
        }

        Text(
            text = formattedTimestamp,
            fontSize = 12.sp,
            fontFamily = productsans_family,
            color = textColor,
            modifier = Modifier.weight(1.5f)
        )

        Text(
            text = value,
            fontSize = 12.sp,
            fontFamily = productsans_family,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ChartPlaceholder(
    dataType: String,
    isEmpty: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        // Get colors inside the Composable
        val emptyIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
        val textColor = MaterialTheme.colorScheme.onSurfaceVariant
        val chartPrimaryColor = MaterialTheme.colorScheme.primary
        val chartBgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        val axisColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        val gridColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
        val titleColor = MaterialTheme.colorScheme.onSurface

        if (isEmpty) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShowChart,
                    contentDescription = null,
                    tint = emptyIconColor,
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "No data available for charting",
                    fontFamily = productsans_family,
                    fontSize = 14.sp,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Placeholder for the chart
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(chartBgColor)
                        .drawBehind {
                            // Draw X and Y axis
                            drawLine(
                                color = axisColor,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 2f
                            )

                            drawLine(
                                color = axisColor,
                                start = Offset(0f, 0f),
                                end = Offset(0f, size.height),
                                strokeWidth = 2f
                            )

                            // Draw grid lines
                            val gridLines = 5
                            for (i in 1..gridLines) {
                                val y = size.height * i / gridLines
                                drawLine(
                                    color = gridColor,
                                    start = Offset(0f, size.height - y),
                                    end = Offset(size.width, size.height - y),
                                    strokeWidth = 1f
                                )
                            }

                            // Draw a fake chart line
                            val path = Path()
                            path.moveTo(0f, size.height * 0.8f)

                            val points = 12
                            for (i in 1..points) {
                                val x = size.width * i / points
                                val y = size.height * (0.2f + 0.6f * kotlin.math.sin(i * 0.5f))
                                path.lineTo(x, y)
                            }

                            drawPath(
                                path = path,
                                color = chartPrimaryColor,
                                style = Stroke(width = 3f, cap = StrokeCap.Round)
                            )
                        }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "$dataType Trend",
                    fontFamily = productsans_family,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = titleColor
                )

                Text(
                    text = "Interactive charts coming soon",
                    fontFamily = productsans_family,
                    fontSize = 12.sp,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun AboutTankDialog(openAboutTankDialog: MutableState<Boolean>, tankId: String?) {
    // Get colors inside the Composable
    val iconTint = MaterialTheme.colorScheme.primary
    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val bodyTextColor = MaterialTheme.colorScheme.onSurface
    val backgroundColor = MaterialTheme.colorScheme.surface

    AlertDialog(
        onDismissRequest = { openAboutTankDialog.value = false },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info Icon",
                    tint = iconTint,
                    modifier = Modifier.padding(end = 12.dp)
                )

                Text(
                    text = when (tankId) {
                        "t1" -> stringResource(id = R.string.tank1_details_title)
                        "t2" -> stringResource(id = R.string.tank2_details_title)
                        "t3" -> stringResource(id = R.string.tank3_details_title)
                        "t4" -> stringResource(id = R.string.tank4_details_title)
                        else -> "Tank Information"
                    },
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = titleTextColor
                )
            }
        },
        text = {
            Column {
                Text(
                    text = when (tankId) {
                        "t1" -> stringResource(id = R.string.tank1_details_description)
                        "t2" -> stringResource(id = R.string.tank2_details_description)
                        "t3" -> stringResource(id = R.string.tank3_details_description)
                        "t4" -> stringResource(id = R.string.tank4_details_description)
                        else -> "No information available for this tank."
                    },
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = bodyTextColor
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { openAboutTankDialog.value = false },
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Got It!",
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        containerColor = backgroundColor,
        shape = RoundedCornerShape(24.dp)
    )
}

// Helper function to set up Firebase child event listeners
fun setupChildEventListener(
    ref: Any,
    dataList: MutableList<Pair<String, String>>,
    onDataChange: () -> Unit
) {
    // This is a simplified version of the original implementation
    // In a real app, you would implement the full listener logic

    if (ref is com.google.firebase.database.DatabaseReference) {
        val query = ref.orderByKey().limitToLast(20)

        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                if (dataList.size < 30) {
                    val timestamp = dataSnapshot.key ?: ""
                    val value = dataSnapshot.value as? Map<*, *>
                    if (value != null) {
                        dataList.add(Pair(timestamp, value.values.first().toString()))
                    }
                }

                onDataChange()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                onDataChange()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                onDataChange()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                onDataChange()
            }

            override fun onCancelled(error: DatabaseError) {
                onDataChange()
            }
        })
    }
}

// Helper functions to determine status based on sensor values
fun getStatusForPh(value: String?): Status {
    if (value == null) return Status.UNKNOWN
    val ph = value.toFloatOrNull() ?: return Status.UNKNOWN
    return when {
        ph in 6.5..8.5 -> Status.NORMAL
        ph in 6.0..9.0 -> Status.WARNING
        else -> Status.CRITICAL
    }
}

fun getStatusForTds(value: String?): Status {
    if (value == null) return Status.UNKNOWN
    val tds = value.toFloatOrNull() ?: return Status.UNKNOWN
    return when {
        tds < 500 -> Status.NORMAL
        tds < 1000 -> Status.WARNING
        else -> Status.CRITICAL
    }
}

fun getStatusForTurbidity(value: String?): Status {
    if (value == null) return Status.UNKNOWN
    val turbidity = value.toFloatOrNull() ?: return Status.UNKNOWN
    return when {
        turbidity < 5 -> Status.NORMAL
        turbidity < 10 -> Status.WARNING
        else -> Status.CRITICAL
    }
}

fun getStatusForTemp(value: String?): Status {
    if (value == null) return Status.UNKNOWN
    val temp = value.toFloatOrNull() ?: return Status.UNKNOWN
    return when {
        temp in 20.0..30.0 -> Status.NORMAL
        temp in 15.0..35.0 -> Status.WARNING
        else -> Status.CRITICAL
    }
}

fun getStatusForWaterLevel(value: String?): Status {
    if (value == null) return Status.UNKNOWN
    val level = value.toFloatOrNull() ?: return Status.UNKNOWN
    return when {
        level >= 80 -> Status.NORMAL
        level >= 40 -> Status.WARNING
        else -> Status.CRITICAL
    }
}

// Helper function to remove decimal if it's .00
fun removeDecimal(value: String): String {
    val decimalIndex = value.indexOf('.')
    return if (decimalIndex != -1 && value.substring(decimalIndex) == ".00") {
        value.substring(0, decimalIndex)
    } else {
        value
    }
}

fun Int.map(fromMin: Int, fromMax: Int, toMin: Int, toMax: Int): Int {
    return ((this - fromMin) * (toMax - toMin) / (fromMax - fromMin)) + toMin
}