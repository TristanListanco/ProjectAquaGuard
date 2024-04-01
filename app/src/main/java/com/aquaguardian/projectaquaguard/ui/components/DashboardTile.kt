package com.aquaguardian.projectaquaguard.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun DashboardTile(
    titleText: String,
    subText: String,
    cardStatus: String,
    icon: Painter
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                fontSize = 36.sp,
                text = titleText
            )
            Text(
                text = subText,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                fontWeight = FontWeight.Normal,
                fontFamily = productsans_family,
                fontSize = 20.sp,
                textAlign = TextAlign.End
            )
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(100.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()) {
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