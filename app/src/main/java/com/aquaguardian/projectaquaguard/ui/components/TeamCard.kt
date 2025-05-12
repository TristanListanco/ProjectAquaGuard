package com.aquaguardian.projectaquaguard.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun TeamCard(
    teamName: String,
    teamRole: String = "",
    teamAvatar: Painter,
    teamContactLink: Array<Pair<String, String>>,
    modifier: Modifier = Modifier,
    cardWidth: Int = 120,
    cardHeight: Int = 160,
    avatarSize: Int = 70,
    onClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .width(cardWidth.dp)
            .height(cardHeight.dp)
            .clickable {
                onClick?.invoke()
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp,
            hoveredElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Avatar Image
            Image(
                painter = teamAvatar,
                contentDescription = "Profile picture of $teamName",
                modifier = Modifier
                    .size(avatarSize.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Team Member Name
            Text(
                text = teamName,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(
                    iterations = Int.MAX_VALUE,
                )
            )

            // Role/Position (if provided)
            if (teamRole.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = teamRole,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = productsans_family,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeamCardPreview() {
    val dummyContacts = arrayOf(
        Pair("Email", "team.member@aquaguard.com")
    )

    TeamCard(
        teamName = "John Doe",
        teamRole = "Lead Developer",
        teamAvatar = painterResource(id = R.drawable.ic_dummy_acc),
        teamContactLink = dummyContacts
    )
}