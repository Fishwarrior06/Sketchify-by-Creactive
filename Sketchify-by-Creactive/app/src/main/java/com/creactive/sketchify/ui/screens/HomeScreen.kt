package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.DpSize
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

@Composable
fun HomeScreen(navController: NavController, windowSizeClass: WindowSizeClass) {
    var currentImage by remember { mutableStateOf<String?>(null) }
    var hasPhoto by remember { mutableStateOf(false) }
    var modo by remember { mutableStateOf("") }

    // Ajustes dinámicos según pantalla
    val imageHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 585.dp
        WindowWidthSizeClass.Medium -> 685.dp
        WindowWidthSizeClass.Expanded -> 590.dp
        else -> 585.dp
    }

    val buttonSpacing = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 8.dp
        WindowWidthSizeClass.Medium -> 12.dp
        WindowWidthSizeClass.Expanded -> 20.dp
        else -> 12.dp
    }

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
                    .height(imageHeight)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                if (currentImage != null) {
                    Text("Aquí mostrarías la foto: $currentImage")
                } else {
                    Text("Aquí irá la imagen")
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // empuja botones abajo

            // 🔘 Bloque de botones
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            modo = "galeria"
                            navController.navigate("PhotoType?modo=galeria")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = buttonSpacing)
                    ) {
                        Text("Subir foto")
                    }

                    Button(
                        onClick = {
                            modo = "camara"
                            navController.navigate("PhotoType?modo=camara")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = buttonSpacing)
                    ) {
                        Text("Tomar foto")
                    }
                }

                Spacer(modifier = Modifier.height(buttonSpacing))

                Button(onClick = { navController.navigate("PastSessions") }) {
                    Text("Sesiones pasadas")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun HomeScreenPreviewCompact() {
    HomeScreen(
        navController = rememberNavController(),
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp))
    )
}