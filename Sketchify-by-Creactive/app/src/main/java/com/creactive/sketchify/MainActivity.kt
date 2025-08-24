package com.creactive.sketchify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.creactive.sketchify.data.UserService
import com.creactive.sketchify.data.ProfileService
import com.creactive.sketchify.models.User
import com.creactive.sketchify.ui.screens.HomeScreen
import com.creactive.sketchify.ui.screens.LoginScreen
import com.creactive.sketchify.ui.screens.RegisterScreen
import com.creactive.sketchify.ui.theme.SketchifyTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SketchifyTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    var currentScreen by remember { mutableStateOf("splash") }
    var loggedUser by remember { mutableStateOf<User?>(null) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // 🔹 Cargar usuarios desde JSON al inicio
        UserService.loadUsersFromJson(context)

        // 🔹 Simula splash screen
        delay(3000)
        currentScreen = "login"
    }

    when (currentScreen) {
        "splash" -> SplashScreen()
        "login" -> LoginScreen(
            onLoginSuccess = { user ->
                loggedUser = user
                currentScreen = "home"
            },
            onNavigateToRegister = {
                currentScreen = "register"
            }
        )
        "register" -> RegisterScreen(
            onRegisterSuccess = { user ->
                loggedUser = user
                currentScreen = "home"
            },
            onNavigateBack = {
                currentScreen = "login"
            }
        )
        "home" -> HomeScreen(loggedUser!!)
    }
}

@Composable
fun SplashScreen() {
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.material3.Text(
            text = "✨ Bienvenido a Sketchify ✨",
            style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
        )
    }
}
