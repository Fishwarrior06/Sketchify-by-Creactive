package com.creactive.sketchify.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creactive.sketchify.R

@Composable
fun HomeScreen(navController: NavController, windowSizeClass: WindowSizeClass) {
    var modo by remember { mutableStateOf("") }

    // --- Ajustes dinámicos (sin cambios) ---
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
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.homescreentext),
                    contentDescription = "Texto homescreen",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )

                // ============ INICIO DE LOS CAMBIOS PARA LA IMAGEN CENTRAL ============

                // 1. Creamos un InteractionSource para la imagen, igual que para los botones.
                val imageInteractionSource = remember { MutableInteractionSource() }
                val isImagePressed by imageInteractionSource.collectIsPressedAsState()

                // 2. Determinar la imagen a mostrar basándose en si está presionada o no.
                val imagenActual = if (isImagePressed) {
                    R.drawable.homescreenlogo_pressed
                } else {
                    R.drawable.homescreenlogo
                }

                // 3. El Box ahora es clickable y usa el InteractionSource.
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight)
                        .clickable(
                            interactionSource = imageInteractionSource,
                            indication = null, // Desactiva el efecto de "ripple" (ondulación)
                            onClick = {
                                // Aquí puedes añadir una acción si quieres que pase algo
                                // al completar el clic (presionar Y soltar).
                                // Si solo quieres el efecto visual, déjalo vacío.
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // 4. Crossfade se encarga de la transición suave. ¡No necesita cambios!
                    Crossfade(targetState = imagenActual, label = "image_fade") { imagenResource ->
                        Image(
                            painter = painterResource(id = imagenResource),
                            contentDescription = "Imagen principal",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                // ==================== FIN DE LOS CAMBIOS ====================

                Spacer(modifier = Modifier.weight(1f))

                // --- Bloque de botones (tu código ya era perfecto aquí) ---
                val interactionSource1 = remember { MutableInteractionSource() }
                val isPressed1 by interactionSource1.collectIsPressedAsState()

                val interactionSource2 = remember { MutableInteractionSource() }
                val isPressed2 by interactionSource2.collectIsPressedAsState()

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { navController.navigate("PhotoType/galeria") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = buttonSpacing)
                                .scale(if (isPressed1) 0.95f else 1f)
                                .alpha(if (isPressed1) 0.8f else 1f),
                            interactionSource = interactionSource1,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.upload_button),
                                contentDescription = "Subir desde galería",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(btnHeight),
                                contentScale = ContentScale.FillBounds
                            )
                        }

                        Button(
                            onClick = { navController.navigate("PhotoType/camara") },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = buttonSpacing)
                                .scale(if (isPressed2) 0.95f else 1f)
                                .alpha(if (isPressed2) 0.8f else 1f),
                            interactionSource = interactionSource2,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues(0.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.take_button),
                                contentDescription = "Tomar foto con cámara",
                                modifier = Modifier
                                    .fillMaxWidth()
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