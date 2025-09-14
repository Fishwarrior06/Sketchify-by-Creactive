package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(navController: NavController) {
    var currentImage by remember { mutableStateOf<String?>(null) }
    var hasPhoto by remember { mutableStateOf(false) }
    var modo by remember { mutableStateOf("") }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔝 Título
            Text(
                text = if (hasPhoto) "Foto cargada" else "Placeholder",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 📦 Área de imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(645.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (currentImage != null) {
                    Text("Aquí mostrarías la foto: $currentImage")
                } else {
                    Text("Aquí irá la imagen")
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // 👈 Empuja los botones hacia abajo

            // 🔘 Bloque de botones
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            modo = "galeria"
                            navController.navigate("PhotoType?modo=galeria")
                        },
                        modifier = Modifier.weight(1f).padding(end = 8.dp)
                    ) {
                        Text("Subir foto")
                    }

                    Button(
                        onClick = {
                            modo = "camara"
                            navController.navigate("PhotoType?modo=camara")
                        },
                        modifier = Modifier.weight(1f).padding(start = 8.dp)
                    ) {
                        Text("Tomar foto")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(onClick = {
                    navController.navigate("PastSessions")}) {
                    Text("Sesiones pasadas")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}