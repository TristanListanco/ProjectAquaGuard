package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.ui.components.TeamCard
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

// Data class to store team member information
data class TeamMemberInfo(
    val name: String,
    val avatar: Int,
    val role: String,
    val bio: String,
    val contactLinks: Array<Pair<String, String>>
)

@Composable
fun AboutView(navHostController: NavHostController) {
    val mainScrollState = rememberScrollState()
    val teamCardsScroll = rememberScrollState()

    // Define team members with their details
    val teamMembers = listOf(
        TeamMemberInfo(
            name = "Listanco T.",
            avatar = R.drawable.listanco,
            role = "Lead Developer",
            bio = "Leads the development of AquaGuardian, focusing on architecture and implementation.",
            contactLinks = arrayOf(
                Pair("facebook", "https://www.facebook.com/TreeStanee")
            )
        ),
        TeamMemberInfo(
            name = "Fajardo P.",
            avatar = R.drawable.fajardo,
            role = "UI/UX Designer",
            bio = "Creates the user interface and experience for AquaGuardian.",
            contactLinks = arrayOf(
                Pair("facebook", "https://www.facebook.com/princess.fajardo.549")
            )
        ),
        TeamMemberInfo(
            name = "Pondang I.",
            avatar = R.drawable.isha,
            role = "Content Specialist",
            bio = "Manages content and educational resources for water conservation.",
            contactLinks = arrayOf(
                Pair("facebook", "https://facebook.com")
            )
        )
    )

    var selectedTeamMember by remember { mutableStateOf<TeamMemberInfo?>(null) }

    if (selectedTeamMember != null) {
        ShowTeamInfo(teamMember = selectedTeamMember!!) {
            selectedTeamMember = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(mainScrollState)
    ) {


        // App info card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.waterdrop),
                    contentDescription = "AquaGuardian Logo",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = "AquaGuardian",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontFamily = productsans_family,
                    fontSize = 28.sp
                )

                Text(
                    text = "Version 1.0",
                    fontSize = 16.sp,
                    fontFamily = productsans_family,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Divider(
                    modifier = Modifier.padding(bottom = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                Text(
                    text = "AquaGuardian is a water conservation app promoting sustainable water usage and education, developed in collaboration with UNESCO and The Water Agency.",
                    fontSize = 16.sp,
                    fontFamily = productsans_family,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }

        // Partners section
        Text(
            text = "OUR PARTNERS",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = productsans_family,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // UNESCO Card
            PartnerCard(
                logo = R.drawable.unesco_logo,
                name = "UNESCO",
                modifier = Modifier.weight(1f)
            )

            // The Water Agency Card
            PartnerCard(
                logo = R.drawable.twa_logo,
                name = "The Water Agency",
                modifier = Modifier.weight(1f)
            )
        }

        // Team section
        Text(
            text = "OUR TEAM",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = productsans_family,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .horizontalScroll(teamCardsScroll),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            teamMembers.forEach { member ->
                TeamCard(
                    teamName = member.name,
                    teamAvatar = painterResource(id = member.avatar),
                    teamContactLink = member.contactLinks,
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = {
                        selectedTeamMember = member
                    }
                )
            }
        }

        // Footer
        Spacer(modifier = Modifier.height(24.dp))
        Divider(color = MaterialTheme.colorScheme.outlineVariant)

        Text(
            text = "© 2025 AquaGuardian. All rights reserved.",
            fontSize = 14.sp,
            fontFamily = productsans_family,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp)
        )
    }
}

@Composable
fun PartnerCard(logo: Int, name: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(140.dp), // Slightly reduce card height
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp), // Reduce padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape),
                painter = painterResource(id = logo),
                contentDescription = "$name Logo",
                contentScale = ContentScale.Crop
            )

            Text(
                text = name,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowTeamInfo(teamMember: TeamMemberInfo, onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Team member avatar
            Image(
                painter = painterResource(id = teamMember.avatar),
                contentDescription = "${teamMember.name} Profile Photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .padding(bottom = 16.dp)
            )

            // Team member name
            Text(
                text = teamMember.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Team member role
            Text(
                text = teamMember.role,
                fontSize = 18.sp,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Team member bio
            Text(
                text = teamMember.bio,
                fontSize = 16.sp,
                fontFamily = productsans_family,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Contact links section
            if (teamMember.contactLinks.isNotEmpty()) {
                Text(
                    text = "CONTACT",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = productsans_family,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                teamMember.contactLinks.forEach { (platform, url) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Here you could add platform icons
                        Text(
                            text = platform.capitalize(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = productsans_family,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = 8.dp)
                        )

                        Text(
                            text = "View Profile",
                            fontSize = 16.sp,
                            fontFamily = productsans_family,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}