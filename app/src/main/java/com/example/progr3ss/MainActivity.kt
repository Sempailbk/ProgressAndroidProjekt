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
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.progr3ss.data.remote.Network
import com.example.progr3ss.data.remote.SignInRequest
import com.example.progr3ss.ui.navigation.AppNavigation
import com.example.progr3ss.ui.navigation.Routes
import com.example.progr3ss.ui.theme.Progr3ssTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var startDestination by mutableStateOf<String?>(null)
        splashScreen.setKeepOnScreenCondition { startDestination == null }

        val app = applicationContext as Progr3ssApp
        lifecycleScope.launch {
            startDestination = decideStartDestination(app)
        }

        setContent {
            Progr3ssTheme {
                startDestination?.let { AppNavigation(startDestination = it) }
            }
        }
    }
}

private suspend fun decideStartDestination(app: Progr3ssApp): String{
    val hasRefreshToken = app.tokenStore.refreshToken.first() != null
    if(!hasRefreshToken) return Routes.LOGIN

    return try {
        app.network.api.getMyProfile()
        Routes.HOME
    } catch(e: Exception){
        Routes.LOGIN
    }
}
