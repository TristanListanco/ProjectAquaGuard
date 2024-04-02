package com.aquaguardian.projectaquaguard.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTile(
    titleText: String,
    subText: String,
    cardStatus: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = modifier.wrapContentHeight().clickable {
            if (onClick != null) {
                onClick()
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            Text(
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                fontSize = 28.sp,
                text = titleText
            )
            Text(
                text = subText,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Normal,
                fontFamily = productsans_family,
                fontSize = 14.sp,
            )
        }
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = cardStatus,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Normal,
                fontFamily = productsans_family,
                fontSize = 12.sp,
                textAlign = TextAlign.End
            )
            Image(painter = icon, contentDescription = "Icon")
        }
    }
}