package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.core.BuildConfig
import com.aquaguardian.projectaquaguard.ui.core.MainRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun WelcomeScreen(navHostController: NavHostController) {

    val openPrivacyPolicyDialog = remember { mutableStateOf(false) }

    when {
        openPrivacyPolicyDialog.value -> {
            PrivacyPolicyDialog(openPrivacyPolicyDialog)
        }
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)

    ) {
        val (titleText, btnGo, btnPrivacy) = createRefs()
        val topGuideline = createGuidelineFromTop(.6f)

        Column(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(titleText) {
                top.linkTo(parent.top)
                bottom.linkTo(btnGo.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }) {
            Text(
                text = stringResource(id = R.string.welcome_screen_title1),
                fontSize = 57.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = stringResource(id = R.string.welcome_screen_title2),
                fontSize = 57.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal
            )
            Text(
                text = stringResource(id = R.string.welcome_screen_subtext),
                fontSize = 24.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal,
                lineHeight = 32.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = stringResource(id = R.string.unescoEntry_text),
                fontSize = 10.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Thin,
                lineHeight = 20.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }


        Button(onClick = {
            if(BuildConfig.BYPASS_AUTH_SCREEN_UI){
                navHostController.navigate(MainRoutes.MainNav.route)
            } else {
                // Don't forget implement Authenticated User Check before launching MainNav
                navHostController.navigate(MainRoutes.AuthenticationView.route)
            }
        }, modifier = Modifier.constrainAs(btnGo) {
            // top.linkTo(titleSub.top)
            bottom.linkTo(btnPrivacy.top, margin = 32.dp)
            start.linkTo(titleText.start)
            end.linkTo(titleText.end)

        }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Arrow Forward Icon"
            )
            Text(
                text = stringResource(id = R.string.welcome_screen_btn_label),
                fontSize = 14.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal
            )
        }

        TextButton(onClick = {
            openPrivacyPolicyDialog.value = !openPrivacyPolicyDialog.value
        }, modifier = Modifier.constrainAs(btnPrivacy) {
            // top.linkTo(btnGo.bottom)
            bottom.linkTo(parent.bottom, margin = 16.dp)
            start.linkTo(btnGo.start)
            end.linkTo(btnGo.end)
        }) {
            Text(
                text = stringResource(id = R.string.welcome_screen_sub_btn_label),
                fontSize = 11.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal,

                )
        }

    }
}


@Composable
fun PrivacyPolicyDialog(openPrivacyPolicyDialog: MutableState<Boolean>) {
    AlertDialog(onDismissRequest = { }, confirmButton = {
        TextButton(onClick = {
            openPrivacyPolicyDialog.value = !openPrivacyPolicyDialog.value
        }) {
            Text(
                text = "OK", color = MaterialTheme.colorScheme.primary
            )
        }
    }, title = {
        Text(
            text = stringResource(id = R.string.welcome_screen_dialog_privacy_policy_title),
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal
        )
    }, text = {
        Text(
            text = stringResource(id = R.string.welcome_screen_dialog_privacy_policy),
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal,
        )
    }, icon = {
        Icon(imageVector = Icons.Outlined.Info, contentDescription = "Privacy Policy Info Icon")
    })
}