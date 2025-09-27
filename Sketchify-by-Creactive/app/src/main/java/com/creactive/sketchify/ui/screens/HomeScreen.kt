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
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.creactive.sketchify.R
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale

@Composable
fun HomeScreen(navController: NavController, windowSizeClass: WindowSizeClass) {
    var currentImage by remember { mutableStateOf<String?>(null) }
    var hasPhoto by remember { mutableStateOf(false) }
    var modo by remember { mutableStateOf("") }

    // Ajustes dinámicos según pantalla
    val imageHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 500.dp
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

    val btnHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 60.dp
        WindowWidthSizeClass.Medium -> 90.dp
        WindowWidthSizeClass.Expanded -> 120.dp
        else -> 60.dp
    }

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 🖼️ IMAGEN DE FONDO
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // 📱 CONTENIDO ENCIMA
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.homescreentext),
                    contentDescription = "Texto homescreen",
                    modifier = Modifier.fillMaxWidth()
                        .height(150.dp)
                )

                // 📦 Área de imagen
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.homescreenlogo),
                        contentDescription = "Imagen de la box",
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // empuja botones abajo

                val interactionSource1 = remember { MutableInteractionSource() }
                val isPressed1 by interactionSource1.collectIsPressedAsState()

                val interactionSource2 = remember { MutableInteractionSource() }
                val isPressed2 by interactionSource2.collectIsPressedAsState()

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
                                .scale(if (isPressed1) 0.95f else 1f)
                                .alpha(if (isPressed1) 0.8f else 1f),
                            interactionSource = interactionSource1,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp),
                            border = null,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.upload_button),
                                contentDescription = "Subir desde galería",
                                modifier = Modifier.fillMaxWidth()
                                    .height(btnHeight),
                                contentScale = ContentScale.FillBounds
                            )
                        }

                        Button(
                            onClick = {
                                modo = "camara"
                                navController.navigate("PhotoType?modo=camara")
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = buttonSpacing)
                                .scale(if (isPressed2) 0.95f else 1f)
                                .alpha(if (isPressed2) 0.8f else 1f),
                            interactionSource = interactionSource2,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                                contentColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(0.dp),
                            border = null,
                            elevation = ButtonDefaults.buttonElevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.take_button),
                                contentDescription = "Tomar foto con cámara",
                                modifier = Modifier.fillMaxWidth()
                                    .height(btnHeight),
                                contentScale = ContentScale.FillBounds
                            )
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