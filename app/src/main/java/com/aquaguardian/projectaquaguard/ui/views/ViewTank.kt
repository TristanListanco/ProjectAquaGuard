package com.aquaguardian.projectaquaguard.ui.views

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.BuildConfig
import com.aquaguardian.projectaquaguard.ui.components.SensorCard
import com.aquaguardian.projectaquaguard.ui.core.MainRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family


@Composable
fun ViewTank(nav: NavHostController, tankId: String? = null) {

    val openAboutTankDialog = remember { mutableStateOf(false) }

    when {
        openAboutTankDialog.value -> {
            AboutTankDialog(openAboutTankDialog, tankId)
        }
    }

    val ctx = LocalContext.current
    val scrollState = rememberScrollState()
    val sensorCardGroupScroll = rememberScrollState()

    var expanded by remember { mutableStateOf(true) }
    val height = animateDpAsState(if (expanded) 100.dp else 0.dp, label = "Transition")

    if (tankId == null) {
        nav.navigate(MainRoutes.MainNav.route)
        Toast.makeText(ctx, "Internal Error: Tank ID Not Specified", Toast.LENGTH_SHORT).show()
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp)
    ) {

        val (backIcon, titleText, statusCard, label1, sensorsContainer, label2, graphContainer, debugButton) = createRefs()

        Row(modifier = Modifier
            .constrainAs(backIcon) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
            .padding(start = 8.dp, top = 8.dp)) {
            IconButton(onClick = {
                nav.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Arrow Forward Icon"
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(BuildConfig.ENABLE_DATA_DEBUGGER){
                    Button(onClick = {
                        nav.navigate(MainRoutes.DataDebugger.route)
                    },
                        modifier = Modifier.padding(end = 8.dp, top = 8.dp, bottom = 8.dp)) {
                        Text(text = "Debug", fontSize = 11.sp, fontFamily = productsans_family, fontWeight = FontWeight.Normal)
                    }
                }
                IconButton(onClick = {
                    openAboutTankDialog.value = !openAboutTankDialog.value
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Info Icon"
                    )
                }
            }
        }

        Text(
            fontSize = 50.sp,
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal,
            text = when (tankId) {
                "t1" -> "Tank 1"
                "t2" -> "Tank 2"
                "t3" -> "Tank 3"
                "t4" -> "Tank 4"
                null -> "NULL!"
                else -> "??"
            },
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(backIcon.bottom)
                    start.linkTo(parent.start)
                }
                .padding(start = 24.dp, top = 32.dp)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 24.dp, end = 24.dp)
                .constrainAs(statusCard) {
                    top.linkTo(titleText.bottom, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    .compositeOver(
                        MaterialTheme.colorScheme.surface
                    ),
                contentColor = MaterialTheme.colorScheme.tertiaryContainer
            ),
            onClick = {
                expanded = !expanded
            }
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomStart
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(height.value)
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {

                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = stringResource(id = R.string.waterLevel_text),
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Normal,
                        fontFamily = productsans_family,
                        fontSize = 20.sp,
                        modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(8.dp),
                    )

                    Text(
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 40.sp,
                        text = "80%",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        Text(
            text = "WATER QUALITY PARAMETER DATA",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = productsans_family,
            modifier = Modifier
                .constrainAs(label1) {
                    top.linkTo(statusCard.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 32.dp, bottom = 16.dp, start = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .horizontalScroll(sensorCardGroupScroll)
                .constrainAs(sensorsContainer) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    top.linkTo(label1.bottom)
                }) {
            SensorCard(
                sensorType = stringResource(id = R.string.ph_text), sensorValue = "7.5", cardStatus = "Updated - Just Now",
                icon =
                painterResource(id = R.drawable.waterdrop),
                modifier = Modifier.padding(start = 16.dp, end = 8.dp)
            )
            SensorCard(
                sensorType = stringResource(id = R.string.turbidityTSS_text),
                sensorValue = "7.5",
                cardStatus = "Updated - Just Now",
                icon =
                painterResource(id = R.drawable.waterdrop),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            SensorCard(
                sensorType = stringResource(id = R.string.tds_text),
                sensorValue = "7.5",
                cardStatus = "Updated - Just Now",
                icon =
                painterResource(id = R.drawable.waterdrop),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            SensorCard(
                sensorType = stringResource(id = R.string.total_soluble_text),
                sensorValue = "7.5",
                cardStatus = "Updated - Just Now",
                icon =
                painterResource(id = R.drawable.waterdrop),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            SensorCard(
                sensorType = stringResource(id = R.string.temp_text), sensorValue = "7.5", cardStatus = "Updated - Just Now",
                icon =
                painterResource(id = R.drawable.waterdrop),
                modifier = Modifier.padding(start = 8.dp, end = 16.dp)
            )
        }

        Text(
            text = "VISUALIZED DATA",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = productsans_family,
            modifier = Modifier
                .constrainAs(label2) {
                    top.linkTo(sensorsContainer.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 32.dp, bottom = 16.dp, start = 16.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(start = 16.dp, end = 16.dp)
                .constrainAs(graphContainer) {
                    top.linkTo(label2.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = { /*TODO*/ }, contentPadding = PaddingValues(8.dp)) {
                        Text(text = "View as Graph", fontSize = 12.sp)
                    }
                    Button(onClick = { /*TODO*/ }, contentPadding = PaddingValues(8.dp)) {
                        Text(text = "View All", fontSize = 12.sp)
                    }
                }
                
            }
        }
    }
}

@Composable
fun AboutTankDialog(openAboutTankDialog: MutableState<Boolean>, tankId: String?) {
    AlertDialog(onDismissRequest = { }, confirmButton = {
        TextButton(onClick = {
            openAboutTankDialog.value = !openAboutTankDialog.value
        }) {
            Text(
                text = "Got It!", color = MaterialTheme.colorScheme.primary
            )
        }
    }, title = {
        Text(
            text = when (tankId) {
                "t1" -> stringResource(id = R.string.tank1_details_title)
                "t2" -> stringResource(id = R.string.tank2_details_title)
                "t3" -> stringResource(id = R.string.tank3_details_title)
                "t4" -> stringResource(id = R.string.tank4_details_title)
                else -> "NULL"
            },
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal
        )
    }, text = {
        Text(
            text = when (tankId) {
                "t1" -> stringResource(id = R.string.tank1_details_description)
                "t2" -> stringResource(id = R.string.tank2_details_description)
                "t3" -> stringResource(id = R.string.tank3_details_description)
                "t4" -> stringResource(id = R.string.tank4_details_description)
                else -> "NULL"
            },
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal,
        )
    }, icon = {
        Icon(imageVector = Icons.Outlined.Info, contentDescription = "About Tank Info Icon")
    })
}
