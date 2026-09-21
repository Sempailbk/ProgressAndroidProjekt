package com.example.progr3ss


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.progr3ss.data.remote.Network
import com.example.progr3ss.data.remote.SignInRequest
import com.example.progr3ss.ui.theme.Progr3ssTheme
import retrofit2.HttpException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Progr3ssTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Progr3ssTheme {
        Greeting("Android")
    }
}

@Composable
fun ConnectionTest() {
    val app = LocalContext.current.applicationContext as Progr3ssApp
    var result by remember { mutableStateOf("Working…") }

    LaunchedEffect(Unit) {
        result = try {
            val auth = app.network.api.signIn(SignInRequest("YOUR_TEST_EMAIL", "YOUR_TEST_PASSWORD"))
            app.tokenStore.save(auth.tokens)

            val me = app.network.api.getMyProfile()   // the interceptor adds the token
            "Signed in and fetched profile:\n${me.username} / ${me.email}"
        } catch (e: HttpException) {
            "Server answered ${e.code()}:\n${e.response()?.errorBody()?.string()}"
        } catch (e: Exception) {
            "Could not reach the server:\n${e.message}"
        }
    }

    Text(result, modifier = Modifier.systemBarsPadding().padding(24.dp))
}