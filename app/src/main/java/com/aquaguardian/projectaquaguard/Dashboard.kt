package com.aquaguardian.projectaquaguard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aquaguardian.projectaquaguard.ui.theme.AquaGuardTheme

class Dashboard : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AquaGuardTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,

                ) {
                    Greeting2("Android")
                }
            }
        }
    }
}



@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.CenterStart
    ){
        Column {
            Text(
                text = "Good Morning!",
                modifier = modifier ,
                style = TextStyle(fontSize = 50.sp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "WEATHER UPDATE",
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "SYSTEM TANKS",
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "MORE ARTICLES",
                style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AquaGuardTheme {
        Greeting2("Android")
    }
}