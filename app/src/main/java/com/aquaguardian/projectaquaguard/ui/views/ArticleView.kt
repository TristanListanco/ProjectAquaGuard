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
import com.aquaguardian.projectaquaguard.ui.components.ArticleCard
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun ArticleView(navHostController: NavHostController) {
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = (32.dp + 16.dp))
            .verticalScroll(scrollState)
    ) {
        val (title, info, articlesContainer) = createRefs()

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
        Column(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .constrainAs(articlesContainer) {
                top.linkTo(title.bottom, 16.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        ) {
            ArticleCard(
                headlines = "NEWS | Team AquaGuardians Philippines Triumphs in UNESCO Water Resilience Challenge 2023, Set Sails for Bali 2024",
                forwardLink = "https://www.facebook.com/CCSCouncilOfficial/posts/pfbid0WT48U8A1RqsZWNmfVS5N3bVXrfpL9WGAxNXgnYYjxMTnAhUG46gFwXqRGqXgQTJql",
                coverImageUrl = "https://scontent.fdvo1-2.fna.fbcdn.net/v/t39.30808-6/430982690_807151331447342_5140497949492155228_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeELdO3nkoPBhdKPMGGocc5ddaJF0CGPpnt1okXQIY-me5lraVgpP7UxJ3M-rZib-vX6Z2n7gTy9ou47a7h4Ml1l&_nc_ohc=g39PsHd46h8Ab7IxrCi&_nc_ht=scontent.fdvo1-2.fna&oh=00_AfA5rQVPbb6SKNwIQkb-o-4Lyw1DFWIlBYV0VgafP4Z9Ww&oe=661896A2"
            )

            ArticleCard(
                headlines = "NEWS | Team AquaGuardians Philippines Triumphs in UNESCO Water Resilience Challenge 2023, Set Sails for Bali 2024",
                forwardLink = "https://www.facebook.com/CCSCouncilOfficial/posts/pfbid0WT48U8A1RqsZWNmfVS5N3bVXrfpL9WGAxNXgnYYjxMTnAhUG46gFwXqRGqXgQTJql",
                coverImageUrl = "https://scontent.fdvo1-2.fna.fbcdn.net/v/t39.30808-6/430982690_807151331447342_5140497949492155228_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeELdO3nkoPBhdKPMGGocc5ddaJF0CGPpnt1okXQIY-me5lraVgpP7UxJ3M-rZib-vX6Z2n7gTy9ou47a7h4Ml1l&_nc_ohc=g39PsHd46h8Ab7IxrCi&_nc_ht=scontent.fdvo1-2.fna&oh=00_AfA5rQVPbb6SKNwIQkb-o-4Lyw1DFWIlBYV0VgafP4Z9Ww&oe=661896A2"
            )

            ArticleCard(
                headlines = "NEWS | Team AquaGuardians Philippines Triumphs in UNESCO Water Resilience Challenge 2023, Set Sails for Bali 2024",
                forwardLink = "https://www.facebook.com/CCSCouncilOfficial/posts/pfbid0WT48U8A1RqsZWNmfVS5N3bVXrfpL9WGAxNXgnYYjxMTnAhUG46gFwXqRGqXgQTJql",
                coverImageUrl = "https://scontent.fdvo1-2.fna.fbcdn.net/v/t39.30808-6/430982690_807151331447342_5140497949492155228_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeELdO3nkoPBhdKPMGGocc5ddaJF0CGPpnt1okXQIY-me5lraVgpP7UxJ3M-rZib-vX6Z2n7gTy9ou47a7h4Ml1l&_nc_ohc=g39PsHd46h8Ab7IxrCi&_nc_ht=scontent.fdvo1-2.fna&oh=00_AfA5rQVPbb6SKNwIQkb-o-4Lyw1DFWIlBYV0VgafP4Z9Ww&oe=661896A2"
            )
            ArticleCard(
                headlines = "NEWS | Team AquaGuardians Philippines Triumphs in UNESCO Water Resilience Challenge 2023, Set Sails for Bali 2024",
                forwardLink = "https://www.facebook.com/CCSCouncilOfficial/posts/pfbid0WT48U8A1RqsZWNmfVS5N3bVXrfpL9WGAxNXgnYYjxMTnAhUG46gFwXqRGqXgQTJql",
                coverImageUrl = "https://scontent.fdvo1-2.fna.fbcdn.net/v/t39.30808-6/430982690_807151331447342_5140497949492155228_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeELdO3nkoPBhdKPMGGocc5ddaJF0CGPpnt1okXQIY-me5lraVgpP7UxJ3M-rZib-vX6Z2n7gTy9ou47a7h4Ml1l&_nc_ohc=g39PsHd46h8Ab7IxrCi&_nc_ht=scontent.fdvo1-2.fna&oh=00_AfA5rQVPbb6SKNwIQkb-o-4Lyw1DFWIlBYV0VgafP4Z9Ww&oe=661896A2"
            )
            ArticleCard(
                headlines = "NEWS | Team AquaGuardians Philippines Triumphs in UNESCO Water Resilience Challenge 2023, Set Sails for Bali 2024",
                forwardLink = "https://www.facebook.com/CCSCouncilOfficial/posts/pfbid0WT48U8A1RqsZWNmfVS5N3bVXrfpL9WGAxNXgnYYjxMTnAhUG46gFwXqRGqXgQTJql",
                coverImageUrl = "https://scontent.fdvo1-2.fna.fbcdn.net/v/t39.30808-6/430982690_807151331447342_5140497949492155228_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=5f2048&_nc_eui2=AeELdO3nkoPBhdKPMGGocc5ddaJF0CGPpnt1okXQIY-me5lraVgpP7UxJ3M-rZib-vX6Z2n7gTy9ou47a7h4Ml1l&_nc_ohc=g39PsHd46h8Ab7IxrCi&_nc_ht=scontent.fdvo1-2.fna&oh=00_AfA5rQVPbb6SKNwIQkb-o-4Lyw1DFWIlBYV0VgafP4Z9Ww&oe=661896A2"
            )


        }


    }
}