package com.aquaguardian.projectaquaguard.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun SensorCard(
    sensorType: String,
    sensorValue: String,
    cardStatus: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
){
    Card(
        modifier = modifier
            .height(150.dp)
            .width(150.dp)
            .clickable {
                if (onClick != null) {
                    onClick()
                }
            },
//        colors = CardDefaults.cardColors(
//            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
//            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
//        ),
    ) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(
            text = sensorType,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Normal,
            fontFamily = productsans_family,
            fontSize = 14.sp,
            maxLines = 1,
//                modifier = Modifier.basicMarquee()
        )
            Text(
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                fontSize = 40.sp,
                text = sensorValue
            )

        }
    }
}