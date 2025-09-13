package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {

    // Estados principales
    var currentImage by remember { mutableStateOf<String?>(null) } // URL o path de la foto
    var hasPhoto by remember { mutableStateOf(false) } // Si ya hay foto
    var modo by remember { mutableStateOf("") } // "camara" o "galeria"

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 🔝 Mensaje superior
            Text(
                text = if (hasPhoto) "Foto cargada" else "Placeholder",
                style = MaterialTheme.typography.headlineMedium
            )

            // 📦 Espacio para imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (currentImage != null) {
                    Text("Aquí mostrarías la foto: $currentImage")
                } else {
                    Text("Aquí irá la imagen")
                }
            }

            // 🔘 Botones inferiores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Botón subir foto
                Button(
                    onClick = {
                        modo = "galeria"
                        navController.navigate("PhotoType?modo=galeria")
                    },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Subir foto")
                }

                // Botón tomar foto
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
        }
    }
}