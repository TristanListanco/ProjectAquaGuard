package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.BuildConfig
import com.aquaguardian.projectaquaguard.ui.core.MainRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import kotlinx.coroutines.delay

@Composable
fun WelcomeScreen(navHostController: NavHostController) {
    val openPrivacyPolicyDialog = remember { mutableStateOf(false) }

    // Animation states
    val logoVisible = remember { mutableStateOf(false) }
    val titleVisible = remember { mutableStateOf(false) }
    val subtitleVisible = remember { mutableStateOf(false) }
    val buttonVisible = remember { mutableStateOf(false) }

    // Trigger animations sequentially
    LaunchedEffect(Unit) {
        logoVisible.value = true
        delay(300)
        titleVisible.value = true
        delay(200)
        subtitleVisible.value = true
        delay(200)
        buttonVisible.value = true
    }

    if (openPrivacyPolicyDialog.value) {
        PrivacyPolicyDialog(openPrivacyPolicyDialog)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top section with logo and titles
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Logo
                AnimatedVisibility(
                    visible = logoVisible.value,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessLow)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessLow)) { -50 }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.waterdrop),
                        contentDescription = "AquaGuardian Logo",
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 24.dp)
                    )
                }

                // Title
                AnimatedVisibility(
                    visible = titleVisible.value,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessLow)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessLow)) { -50 }
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.welcome_screen_title1),
                            fontSize = 48.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = stringResource(id = R.string.welcome_screen_title2),
                            fontSize = 48.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Subtitle
                AnimatedVisibility(
                    visible = subtitleVisible.value,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessLow)) +
                            slideInVertically(spring(stiffness = Spring.StiffnessLow)) { -50 }
                ) {
                    Column {
                        Text(
                            text = stringResource(id = R.string.welcome_screen_subtext),
                            fontSize = 20.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 28.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // UNESCO text in a card
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = stringResource(id = R.string.unescoEntry_text),
                                fontSize = 12.sp,
                                fontFamily = productsans_family,
                                fontWeight = FontWeight.Medium,
                                lineHeight = 18.sp,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Bottom section with buttons
            AnimatedVisibility(
                visible = buttonVisible.value,
                enter = fadeIn(spring(stiffness = Spring.StiffnessLow)) +
                        slideInVertically(spring(stiffness = Spring.StiffnessLow)) { 50 }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Main button
                    Button(
                        onClick = {
                            if (BuildConfig.BYPASS_AUTH_SCREEN_UI) {
                                navHostController.navigate(MainRoutes.MainNav.route)
                            } else {
                                // Don't forget implement Authenticated User Check before launching MainNav
                                navHostController.navigate(MainRoutes.AuthenticationView.route)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(
                            text = stringResource(id = R.string.welcome_screen_btn_label),
                            fontSize = 16.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.padding(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Arrow Forward Icon"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Privacy policy button
                    TextButton(
                        onClick = {
                            openPrivacyPolicyDialog.value = true
                        }
                    ) {
                        Text(
                            text = stringResource(id = R.string.welcome_screen_sub_btn_label),
                            fontSize = 14.sp,
                            fontFamily = productsans_family,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Version number
                    Text(
                        text = "Version 1.0",
                        fontSize = 12.sp,
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun PrivacyPolicyDialog(openPrivacyPolicyDialog: MutableState<Boolean>) {
    AlertDialog(
        onDismissRequest = {
            openPrivacyPolicyDialog.value = false
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        title = {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Info,
                        contentDescription = "Privacy Policy Info Icon",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.welcome_screen_dialog_privacy_policy_title),
                        fontFamily = productsans_family,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Divider(color = MaterialTheme.colorScheme.outlineVariant)
            }
        },
        text = {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = stringResource(id = R.string.welcome_screen_dialog_privacy_policy),
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    openPrivacyPolicyDialog.value = false
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "I Understand",
                    fontFamily = productsans_family,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openPrivacyPolicyDialog.value = false
                }
            ) {
                Text(
                    text = "Close",
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = productsans_family
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp)
    )
}

