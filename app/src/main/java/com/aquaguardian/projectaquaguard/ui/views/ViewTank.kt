package com.aquaguardian.projectaquaguard.ui.views

import android.widget.Toast
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.ui.core.MainRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewTank(nav: NavHostController, tankId: String? = null) {
    val ctx = LocalContext.current
    val scrollState = rememberScrollState()

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
            .padding(top = 32.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) {

        val (titleText, statusCard) = createRefs()

        Text(
            fontSize = 50.sp,
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal,
            text = when (tankId) {
                "t1" -> "Tank 1"
                "t2" -> "Tank 2"
                "t3" -> "Tank 3"
                "t4" -> "Tank 4"
                "t5" -> "Tank 5"
                "t6" -> "Tank 6"
                null -> "NULL!"
                else -> "??"
            },
            modifier = Modifier.constrainAs(titleText) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
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
                Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Bottom) {
                    Text(
                        text = "Water Tank Level",
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Normal,
                        fontFamily = productsans_family,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(1f).padding(8.dp),
                    )

                    Text(
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 36.sp,
                        text = "80%",
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

    }
}