package com.aquaguardian.projectaquaguard.ui.views

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.core.vms.MainVMService
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CsvDownloader(
    navHostController: NavHostController,
    mainVMService: MainVMService,
    tankId: String?,
    sensorId: String?
) {

    val scrollState = rememberScrollState()

    val phDataList = remember { mutableListOf<Pair<String, String>?>(null) }
    val tdsDataList = remember { mutableListOf<Pair<String, String>?>(null) }
    val turbidityDataList = remember { mutableListOf<Pair<String, String>?>(null) }
    val wtempDataList = remember { mutableListOf<Pair<String, String>?>(null) }
    val wlvlDataList = remember { mutableListOf<Pair<String, String>?>(null) }

    val startDate = remember { mutableStateOf("") }
    val endDate = remember { mutableStateOf("") }

    var isStartDatePickerExpanded by remember { mutableStateOf(false) }
    var isEndDatePickerExpanded by remember { mutableStateOf(false) }

    val startPickerShape = if (isStartDatePickerExpanded) RoundedCornerShape(8.dp).copy(
        bottomEnd = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
    else RoundedCornerShape(8.dp)

    val endPickerShape = if (isStartDatePickerExpanded) RoundedCornerShape(8.dp).copy(
        bottomEnd = CornerSize(0.dp),
        bottomStart = CornerSize(0.dp)
    )
    else RoundedCornerShape(8.dp)


    val ctx = LocalContext.current
    val database = FirebaseDatabase.getInstance()
    val uid = "pvbkhv0iJ4RViT9qlJzBeNkndPS2"

    val wlvlPath = "usrdata/$uid/$tankId/wlvl"


    val ref1 = database.getReference(wlvlPath)

    DisposableEffect(true) {
        if(sensorId != null){
            Toast.makeText(ctx, "From $sensorId!", Toast.LENGTH_SHORT).show()
        }
        val wlevelQuery = ref1.orderByKey().limitToLast(20)
        val wlevelListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                val timestamp = dataSnapshot.key ?: ""
                val value = dataSnapshot.value as? Map<*, *>
                if (value?.containsKey("wlvl") == true) {
                    wlvlDataList.add(Pair(timestamp, value.values.first().toString()))
                    endDate.value = wlvlDataList[wlvlDataList.lastIndex]?.first ?: ""
                    startDate.value = wlvlDataList[1]?.first ?: ""
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        wlevelQuery.addChildEventListener(wlevelListener)

        onDispose {
            wlevelQuery.removeEventListener(wlevelListener)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .verticalScroll(scrollState)
            .padding(bottom = 16.dp), animateChanges = true
    ) {
        val (topButtonGroup, titleText, label1, selectDate) = createRefs()

        Row(modifier = Modifier
            .constrainAs(topButtonGroup) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
            .padding(start = 8.dp, top = 8.dp)) {
            IconButton(onClick = {
                navHostController.popBackStack()
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Arrow Forward Icon"
                )
            }
        }

        Text(fontSize = 50.sp,
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal,
            text = "Download Data",
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(topButtonGroup.bottom)
                    start.linkTo(parent.start)
                }
                .padding(start = 24.dp, top = 32.dp)
        )

        Text(
            text = "START & END DATE",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = productsans_family,
            modifier = Modifier.constrainAs(label1){
                top.linkTo(titleText.bottom, 16.dp)
                start.linkTo(parent.start, 16.dp)
            }
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .constrainAs(selectDate) {
                top.linkTo(label1.bottom, 8.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .padding(start = 16.dp, end = 16.dp)) {

            ExposedDropdownMenuBox(
                expanded = isStartDatePickerExpanded,
                onExpandedChange = { isStartDatePickerExpanded = !isStartDatePickerExpanded },
                modifier = Modifier.weight(1f).padding(end = 8.dp),
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    textStyle = TextStyle.Default.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    ),
                    value = startDate.value,
                    onValueChange = {},
                    label = {
                        Text(
                            "Start",
                            fontWeight = FontWeight.Bold,
                            fontFamily = productsans_family,
                            fontSize = 11.sp
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isStartDatePickerExpanded) },
                    shape = startPickerShape,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                DropdownMenu(
                    expanded = isStartDatePickerExpanded,
                    onDismissRequest = { isStartDatePickerExpanded = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    wlvlDataList.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                if (selectionOption != null) {
                                    Text(selectionOption.first)
                                }
                            },
                            onClick = {
                                if (selectionOption != null) {
                                    startDate.value = selectionOption.first
                                }
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }

            ExposedDropdownMenuBox(
                expanded = isEndDatePickerExpanded,
                onExpandedChange = { isEndDatePickerExpanded = !isEndDatePickerExpanded },
                modifier = Modifier.weight(1f).padding(start = 8.dp),
            ) {
                TextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    readOnly = true,
                    textStyle = TextStyle.Default.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Light
                    ),
                    value = endDate.value,
                    onValueChange = {},
                    label = {
                        Text(
                            "End",
                            fontWeight = FontWeight.Bold,
                            fontFamily = productsans_family,
                            fontSize = 11.sp
                        )
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isEndDatePickerExpanded) },
                    shape = endPickerShape,
                    colors = ExposedDropdownMenuDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                DropdownMenu(
                    expanded = isEndDatePickerExpanded,
                    onDismissRequest = { isEndDatePickerExpanded = false },
                    modifier = Modifier.exposedDropdownSize()
                ) {
                    wlvlDataList.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = {
                                if (selectionOption != null) {
                                    Text(selectionOption.first)
                                }
                            },
                            onClick = {
                                if (selectionOption != null) {
                                    endDate.value = selectionOption.first
                                }
                            },
                            contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                        )
                    }
                }
            }
        }


    }
}


@Composable
fun removeTimeFromTimestamp(timestamp: String): String {
    val inputFormat = SimpleDateFormat("MM-dd-yyyy-HH:mm:ss", Locale.ROOT)
    val outputFormat = SimpleDateFormat("MM-dd-yyyy", Locale.ROOT)

    return try {
        val date = inputFormat.parse(timestamp)
        outputFormat.format(date)
    } catch (e: Exception) {

        timestamp
    }
}