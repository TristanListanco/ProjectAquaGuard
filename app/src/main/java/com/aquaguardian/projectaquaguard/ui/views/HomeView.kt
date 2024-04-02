package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.ui.components.DashboardTile
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun HomeView(nav: NavHostController) {
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
                text = "Morning!"
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(overviewCard) {
                    top.linkTo(title.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_weather_sunny),
                        contentDescription = "Icon Weather Sunny",
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                    )
                    Text(
                        text = "Puerto Princesa Palawan",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Normal,
                        fontFamily = productsans_family,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(1f),
                        textAlign = TextAlign.End
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 36.sp,
                        text = "31°C"
                    )
                }
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Thin,
                        fontFamily = productsans_family,
                        fontSize = 12.sp,
                        text = "42°C"
                    )
                    Spacer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(16.dp)
                    )
                    Text(
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Thin,
                        fontFamily = productsans_family,
                        fontSize = 12.sp,
                        text = "39°C"
                    )
                }
            }
        }

        Text(
            text = "TANK STATUS",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = productsans_family,
            modifier = Modifier
                .constrainAs(tankLabel) {
                    top.linkTo(overviewCard.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 16.dp, bottom = 16.dp)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight().constrainAs(cardContainer){
                top.linkTo(tankLabel.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            DashboardTile(titleText = "Tank #1", subText = "Rapid Sand Filtered", icon = painterResource(id = R.drawable.waterdrop), cardStatus = "Updated 1 min ago.")
            DashboardTile(titleText = "Tank #2", subText = "Rapid Sand Filtered", icon = painterResource(id = R.drawable.waterdrop), cardStatus = "Updated 1 min ago.")
        }
    }
}