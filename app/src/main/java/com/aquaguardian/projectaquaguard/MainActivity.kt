package com.aquaguardian.projectaquaguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aquaguardian.projectaquaguard.ui.theme.AquaGuardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaGuardTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("AquaGuard")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = modifier.padding(16.dp)
        ) {
            Text(
                text = "Project $name!",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 45.sp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "IoT Based Water Quality Monitoring Device",
                modifier = Modifier.fillMaxWidth(),
                style = TextStyle(fontSize = 15.sp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            val privacyPolicyText = buildAnnotatedString {
                withStyle(style = SpanStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, textDecoration = TextDecoration.Underline)) {
                    append("View Privacy Policy")
                }
            }
            ClickableText(
                text = privacyPolicyText,
                onClick = { setShowDialog(true) },
                modifier = Modifier.fillMaxWidth().clickable { }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {},
            ) {
                Text("Continue")
            }
            if (showDialog) {
                PrivacyPolicyDialog(onAccept = {
                    setShowDialog(false) // Dismiss the dialog when accept button is clicked
                })
            }
        }
    }
}

@Composable
fun PrivacyPolicyDialog(onAccept: () -> Unit) {

    AlertDialog(
        onDismissRequest = { /* Handle dismiss */ },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_info_24), // Your privacy icon resource
                    contentDescription = null, // Description for the icon
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Privacy Policy",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        },
        text = {
            Text(
                text = "This Android Application is an entry for UWRC2024 made by MSU-IIT",
                textAlign = TextAlign.Start,
                fontSize = 16.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onAccept
            ) {
                Text("Accept")
            }
        },
        dismissButton = null
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AquaGuardTheme {
        Greeting("Android")
    }
}