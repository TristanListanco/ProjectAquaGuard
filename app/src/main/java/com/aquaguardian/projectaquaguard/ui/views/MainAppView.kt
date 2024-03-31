package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.ui.core.PageRouter

@Composable
fun MainAppView(mainNavController: NavHostController){
    Row(modifier = Modifier.fillMaxSize()) {
        PageRouter(mainNavController)
    }
}