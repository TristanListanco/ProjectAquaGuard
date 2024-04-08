package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.ui.components.TeamCard
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun AboutView(navHostController: NavHostController) {
    val teamCardsScroll = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = (32.dp + 16.dp + 40.dp))
            .verticalScroll(rememberScrollState())
    ) {
        val (title, topCardContainer, singleCard, label1, teamContainer) = createRefs()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(title) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(16.dp)
        ) {
            Text(
                fontSize = 50.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal,
                text = "About"
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .constrainAs(topCardContainer) {
                top.linkTo(title.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(end = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.waterdrop),
                        contentDescription = "AquaGuardian Logo",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .padding(bottom = 24.dp)
                    )
                    Text(
                        text = "AquaGuardian",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 20.sp
                    )
                    Text(
                        text = "v1.0",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = 8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .width(80.dp)
                            .height(80.dp)
                            .padding(bottom = 24.dp)
                            .aspectRatio(1f / 1f)
                            .clip(CircleShape),
                        painter = painterResource(id = R.drawable.unesco_logo),
                        contentDescription = "Unesco Logo",
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "UNESCO",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontFamily = productsans_family,
                        fontSize = 20.sp
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(singleCard){
                    top.linkTo(topCardContainer.bottom, 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .width(80.dp)
                        .height(80.dp)
                        .padding(end = 16.dp)
                        .aspectRatio(1f / 1f)
                        .clip(CircleShape),
                    painter = painterResource(id = R.drawable.twa_logo),
                    contentDescription = "The Water Agency Logo",
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "The Water Agency",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontFamily = productsans_family,
                    fontSize = 20.sp
                )
            }
        }
        Text(
            text = "THE TEAM",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = productsans_family,
            modifier = Modifier
                .constrainAs(label1) {
                    top.linkTo(singleCard.bottom)
                    start.linkTo(parent.start)
                }
                .padding(top = 16.dp, bottom = 16.dp)
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .constrainAs(teamContainer) {
                top.linkTo(label1.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            .horizontalScroll(teamCardsScroll)) {
            TeamCard(
                teamName = "Listanco T.",
                teamAvatar = painterResource(id = R.drawable.listanco),
                teamContactLink = arrayOf(
                    Pair("facebook", "https://www.facebook.com/TreeStanee")
                ),
                modifier = Modifier.padding(end = 8.dp)
            )
            TeamCard(
                teamName = "Fajardo P.",
                teamAvatar = painterResource(id = R.drawable.fajardo),
                teamContactLink = arrayOf(
                    Pair("facebook", "https://www.facebook.com/princess.fajardo.549")
                ),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            )
            TeamCard(
                teamName = "Pondang I.",
                teamAvatar = painterResource(id = R.drawable.isha),
                teamContactLink = arrayOf(
                    Pair("facebook", "https://facebook.com")
                ),
                modifier = Modifier.padding(start = 8.dp)
            )

        }

    }
}