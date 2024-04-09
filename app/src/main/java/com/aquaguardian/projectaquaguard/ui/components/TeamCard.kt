package com.aquaguardian.projectaquaguard.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun TeamCard(
    teamName: String,
    teamAvatar: Painter,
    teamContactLink: Array<Pair<String, String>>,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current
    Card(modifier = modifier
        .width(100.dp)
        .height(100.dp)
        .clickable {
            if (onClick != null) {
                onClick()
            }
        }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = teamAvatar,
                contentDescription = "Profile of Tristan Listanco",
                modifier = Modifier
                    .width(50.dp)
                    .height(50.dp)
                    .clip(CircleShape)
            )
            Text(
                text = teamName,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}