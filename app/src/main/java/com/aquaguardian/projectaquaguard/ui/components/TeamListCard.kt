package com.aquaguardian.projectaquaguard.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun TeamListCard(
    teamName: String,
    teamAvatar: Painter,
    teamContactLink: Array<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Card(modifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clickable {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(teamContactLink[0].second)
            ContextCompat.startActivity(context, intent, null)
        }) {
        Column(

        ) {

        }
    }
}