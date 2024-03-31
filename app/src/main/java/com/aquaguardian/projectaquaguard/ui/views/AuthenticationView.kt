package com.aquaguardian.projectaquaguard.ui.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.aquaguardian.projectaquaguard.R
import com.aquaguardian.projectaquaguard.ui.core.MainRoutes
import com.aquaguardian.projectaquaguard.ui.theme.productsans_family

@Composable
fun AuthenticationView(nav: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        val (titleText, emailField, passField, enterField, changeModeField) = createRefs()

        createVerticalChain(
            titleText, emailField, passField, enterField, changeModeField,
            chainStyle = ChainStyle.Packed,
        )

        Text(
            text = stringResource(id = R.string.authentication_screen_login_title),
            fontSize = 57.sp,
            fontFamily = productsans_family,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .constrainAs(titleText) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 16.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                emailError = if (isValidEmail(it)) "" else "Invalid email address"
            },
            label = {
                Text(
                    "Email", fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal
                )
            },
            isError = emailError.isNotEmpty(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .constrainAs(emailField) {
                    top.linkTo(titleText.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                passwordError =
                    if (it.length < 6) "Password must be at least 6 characters" else ""
            },
            label = {
                Text(
                    "Password", fontFamily = productsans_family,
                    fontWeight = FontWeight.Normal
                )
            },
            isError = passwordError.isNotEmpty(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .constrainAs(passField) {
                    top.linkTo(emailField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        )

        val viewCtx = LocalView.current.context
        Button(
            onClick = {
                if(emailError == "" && passwordError == ""){
                    nav.navigate(MainRoutes.MainNav.route)
                } else {
                    Toast.makeText(viewCtx, "Error: $emailError & $passwordError", Toast.LENGTH_SHORT ).show()
                }
            },
            modifier = Modifier
                .constrainAs(enterField) {
                    top.linkTo(passField.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(changeModeField.top)
                }
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            Text(
                "Login", fontFamily = productsans_family,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

fun isValidEmail(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}