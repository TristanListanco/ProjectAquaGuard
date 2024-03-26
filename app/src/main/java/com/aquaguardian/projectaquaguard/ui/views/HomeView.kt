package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.ui.core.AquaGuardianRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family
import com.google.android.material.navigation.NavigationView

@Composable
fun HomeView(nav: NavHostController){
    Column(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 32.dp, start = 32.dp, end = 32.dp, bottom = 32.dp)) {
            Text(fontSize = 50.sp, fontFamily = productsans_family, fontWeight = FontWeight.Normal , text = "Good")

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(8.dp))

            Text(fontSize = 50.sp, fontFamily = productsans_family, fontWeight = FontWeight.Normal , text = "Morning!")
        }

        Button(onClick = {
            nav.navigate(AquaGuardianRoutes.ViewTank.route)
        }) {
            Text(text = "Open Tank View")
        }
    }
}