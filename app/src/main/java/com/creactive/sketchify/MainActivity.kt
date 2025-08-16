package com.creactive.sketchify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creactive.sketchify.ui.theme.SketchifyTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SketchifyTheme {
                AppContent() // 👈 aquí empieza el flujo
            }
        }
    }
}

@Composable
fun AppContent() {
    var showLogin by remember { mutableStateOf(false) }

    // Simula carga de 3 segundos
    LaunchedEffect(Unit) {
        delay(3000)
        showLogin = true
    }

    if (showLogin) {
        LoginScreen()
    } else {
        SplashScreen()
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "✨ Bienvenido a Sketchify ✨", style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun LoginScreen() {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Inicia sesión", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Usuario") })
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Contraseña") })
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* aquí pondremos Firebase después */ }) {
                Text("Entrar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    SketchifyTheme {
        AppContent()
    }
}