package com.aquaguardian.projectaquaguard.ui.views

import android.util.Log
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Composable
fun ViewTank(nav: NavHostController, tankId: String? = null) {

    val wlvlData = remember { mutableStateOf<Pair<String, String>?>(null) }
    val wtempData = remember { mutableStateOf<Pair<String, String>?>(null) }
    val pHData = remember { mutableStateOf<Pair<String, String>?>(null) }
    val turbidityData = remember { mutableStateOf<Pair<String, String>?>(null) }
    val tdsData = remember { mutableStateOf<Pair<String, String>?>(null) }

    val ctx = LocalContext.current
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

    val wlvlListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.lastOrNull()?.let { recentSnapshot ->
                val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                wlvlData.value = sortedEntries[0].value.toString() to recentSnapshot.key.toString()

                ref1.removeEventListener(this)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    val wllvlChildListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            ref1.addListenerForSingleValueEvent(wlvlListener)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            ref1.addListenerForSingleValueEvent(wlvlListener)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            ref1.addListenerForSingleValueEvent(wlvlListener)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            ref1.addListenerForSingleValueEvent(wlvlListener)
        }

        override fun onCancelled(error: DatabaseError) {
            ref1.addListenerForSingleValueEvent(wlvlListener)
        }
    }

    ref1.addChildEventListener(wllvlChildListener)



    val wtempListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.lastOrNull()?.let { recentSnapshot ->
                val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                wtempData.value = sortedEntries[0].value.toString() to recentSnapshot.key.toString()

                ref2.removeEventListener(this)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    val wtempChildListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            ref2.addListenerForSingleValueEvent(wtempListener)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            ref2.addListenerForSingleValueEvent(wtempListener)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            ref2.addListenerForSingleValueEvent(wtempListener)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            ref2.addListenerForSingleValueEvent(wtempListener)
        }

        override fun onCancelled(error: DatabaseError) {
            ref2.addListenerForSingleValueEvent(wtempListener)
        }
    }

    ref2.addChildEventListener(wtempChildListener)

    val phListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.lastOrNull()?.let { recentSnapshot ->
                val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                pHData.value = sortedEntries[0].value.toString() to recentSnapshot.key.toString()

                ref3.removeEventListener(this)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    val phChildEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            ref3.addListenerForSingleValueEvent(phListener)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            ref3.addListenerForSingleValueEvent(phListener)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            ref3.addListenerForSingleValueEvent(phListener)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            ref3.addListenerForSingleValueEvent(phListener)
        }

        override fun onCancelled(error: DatabaseError) {
            ref3.addListenerForSingleValueEvent(phListener)
        }
    }

    ref3.addChildEventListener(phChildEventListener)

    val turbidityListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.lastOrNull()?.let { recentSnapshot ->
                val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                turbidityData.value = sortedEntries[0].value.toString() to recentSnapshot.key.toString()

                ref4.removeEventListener(this)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    val turbidityChildListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            ref4.addListenerForSingleValueEvent(turbidityListener)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            ref4.addListenerForSingleValueEvent(turbidityListener)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            ref4.addListenerForSingleValueEvent(turbidityListener)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            ref4.addListenerForSingleValueEvent(turbidityListener)
        }

        override fun onCancelled(error: DatabaseError) {
            ref4.addListenerForSingleValueEvent(turbidityListener)
        }
    }

    ref4.addChildEventListener(turbidityChildListener)

    val tdsListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.lastOrNull()?.let { recentSnapshot ->
                val sortedEntries = recentSnapshot.children.sortedByDescending { it.key }
                tdsData.value = sortedEntries[0].value.toString() to recentSnapshot.key.toString()

                ref5.removeEventListener(this)
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Toast.makeText(ctx, error.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    val tdsChildListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            ref5.addListenerForSingleValueEvent(tdsListener)
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            ref5.addListenerForSingleValueEvent(tdsListener)
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            ref5.addListenerForSingleValueEvent(tdsListener)
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            ref5.addListenerForSingleValueEvent(tdsListener)
        }

        override fun onCancelled(error: DatabaseError) {
            ref5.addListenerForSingleValueEvent(tdsListener)
        }
    }

    ref5.addChildEventListener(tdsChildListener)


    val openAboutTankDialog = remember { mutableStateOf(false) }

    when {
        openAboutTankDialog.value -> {
            AboutTankDialog(openAboutTankDialog, tankId)
        }
    }

    val scrollState = rememberScrollState()
    val sensorCardGroupScroll = rememberScrollState()

    var expanded by remember { mutableStateOf(true) }
    val waterLevelHeightDP = remember { mutableIntStateOf(0) }
    val height = animateDpAsState(if (expanded) waterLevelHeightDP.intValue.dp else waterLevelHeightDP.intValue.dp, label = "Transition")

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

        val (topButtonGroup, titleText, statusCard, label1, sensorsContainer, label2, graphContainer) = createRefs()

        Row(modifier = Modifier
            .constrainAs(topButtonGroup) {
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
                    top.linkTo(topButtonGroup.bottom)
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
            )
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

                    if(wlvlData.value != null){
                        val wlvl = wlvlData.value!!.first.toIntOrNull() ?: 0
                        waterLevelHeightDP.intValue = wlvl.map(0, 100, 0, 150)
                        expanded = !expanded
                        Text(
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontWeight = FontWeight.Bold,
                            fontFamily = productsans_family,
                            fontSize = 40.sp,
                            text = "${wlvlData.value!!.first}%",
                            modifier = Modifier.padding(8.dp)
                        )
                    } else {
                        Text(
                            color = MaterialTheme.colorScheme.onTertiary,
                            fontWeight = FontWeight.Bold,
                            fontFamily = productsans_family,
                            fontSize = 40.sp,
                            text = "0%",
                            modifier = Modifier.padding(8.dp)
                        )
                    }
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
            if(pHData.value != null) {
                SensorCard(
                    sensorType = stringResource(id = R.string.ph_text), sensorValue = pHData.value!!.first, cardStatus = "Updated - Just Now",
                    icon =
                    painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                )
            } else {
                SensorCard(
                    sensorType = stringResource(id = R.string.ph_text), sensorValue = "...", cardStatus = "Updated - Just Now",
                    icon =
                    painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                )
            }
            if(tdsData.value != null){
                SensorCard(
                    sensorType = stringResource(id = R.string.tds_text),
                    sensorValue = tdsData.value!!.first,
                    cardStatus = "Updated - Just Now",
                    icon =
                    painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            } else {
                SensorCard(
                    sensorType = stringResource(id = R.string.tds_text),
                    sensorValue = "...",
                    cardStatus = "Updated - Just Now",
                    icon =
                    painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            }
            if(turbidityData.value != null){
                SensorCard(
                    sensorType = stringResource(id = R.string.turbidityTSS_text),
                    sensorValue = turbidityData.value!!.second,
                    cardStatus = "Updated - Just Now",
                    icon =
                    painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            } else {
                SensorCard(
                    sensorType = stringResource(id = R.string.turbidityTSS_text),
                    sensorValue = "...",
                    cardStatus = "Updated - Just Now",
                    icon =
                    painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                )
            }
            if(wtempData.value !=null){
                SensorCard(
                    sensorType = stringResource(id = R.string.temp_text), sensorValue = "${wtempData.value!!.first} °C", cardStatus = "Updated - Just Now",
                    icon =
                    painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                )
            } else {
                SensorCard(
                    sensorType = stringResource(id = R.string.temp_text), sensorValue = "...", cardStatus = "Updated - Just Now",
                    icon =
                    painterResource(id = R.drawable.waterdrop),
                    modifier = Modifier.padding(start = 8.dp, end = 16.dp)
                )
            }
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

fun Int.map(fromMin: Int, fromMax: Int, toMin: Int, toMax: Int): Int {
    return ((this - fromMin) * (toMax - toMin) / (fromMax - fromMin)) + toMin
}
