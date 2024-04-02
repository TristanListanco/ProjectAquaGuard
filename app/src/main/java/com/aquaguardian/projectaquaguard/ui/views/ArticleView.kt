package com.aquaguardian.projectaquaguard.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun ArticleView(navHostController: NavHostController){
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = (32.dp + 16.dp))
            .verticalScroll(rememberScrollState())
    ) {
        val (title,info) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.ic_info),
            contentDescription = "About Me Icon",
            modifier = Modifier
                .wrapContentSize()
                .constrainAs(info) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }


        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .constrainAs(title) {
                    top.linkTo(info.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .padding(16.dp)
        ) {
            Text(
                fontSize = 50.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal,
                text = "Browse"
            )
            Text(
                fontSize = 50.sp,
                fontFamily = productsans_family,
                fontWeight = FontWeight.Normal,
                text = "Articles"
            )

        }

    }
}