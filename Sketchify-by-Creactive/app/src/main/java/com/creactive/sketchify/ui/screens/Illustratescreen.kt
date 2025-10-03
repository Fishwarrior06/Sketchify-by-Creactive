package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
fun IllustrateScreen(
    navController: NavController,
    windowSizeClass: WindowSizeClass
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // --- Fondo de pantalla (se mantiene) ---
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop // Corregido a Crop para mejor ajuste
            )

            // Columna principal que organiza TODA la pantalla
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopNavButtons(navController = navController, windowSizeClass = windowSizeClass)

                // Columna interna para el resto del contenido
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.weight(0.5f))

                    // --- 2. Imagen de instrucciones, AHORA MÁS GRANDE ---
                    Image(
                        painter = painterResource(id = R.drawable.instructionsbox),
                        contentDescription = "Caja de instrucciones",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(500.dp) // <-- CAMBIO AQUÍ: AUMENTADO DE 350dp A 450dp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // --- 3. Botón inferior, ahora más grande ---
                    BottomButton(windowSizeClass = windowSizeClass)

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// --- COMPOSABLE PARA LOS BOTONES SUPERIORES (SIN CAMBIOS) ---
@Composable
private fun TopNavButtons(navController: NavController, windowSizeClass: WindowSizeClass) {
    val btnWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 60.dp
        WindowWidthSizeClass.Medium -> 90.dp
        WindowWidthSizeClass.Expanded -> 120.dp
        else -> 60.dp
    }
    val btnHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 50.dp
        WindowWidthSizeClass.Medium -> 80.dp
        WindowWidthSizeClass.Expanded -> 110.dp
        else -> 50.dp
    }
    val interactionSourceBack = remember { MutableInteractionSource() }
    val isPressedBack by interactionSourceBack.collectIsPressedAsState()

    val interactionSourceHome = remember { MutableInteractionSource() }
    val isPressedHome by interactionSourceHome.collectIsPressedAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .scale(if (isPressedBack) 0.95f else 1f)
                .alpha(if (isPressedBack) 0.8f else 1f),
            interactionSource = interactionSourceBack,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.backbutton),
                contentDescription = "BackButton",
                modifier = Modifier
                    .width(btnWidth)
                    .height(btnHeight)
            )
        }

        Button(
            onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .scale(if (isPressedHome) 0.95f else 1f)
                .alpha(if (isPressedHome) 0.8f else 1f),
            interactionSource = interactionSourceHome,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.home_button),
                contentDescription = "HomeButton",
                modifier = Modifier
                    .width(btnWidth)
                    .height(btnHeight)
            )
        }
    }
}

// --- COMPOSABLE PARA EL BOTÓN INFERIOR, AHORA MÁS GRANDE ---
@Composable
private fun BottomButton(windowSizeClass: WindowSizeClass) {
    val btnHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 75.dp
        WindowWidthSizeClass.Medium -> 110.dp
        WindowWidthSizeClass.Expanded -> 140.dp
        else -> 75.dp
    }
    // Ya no necesitas 'interactionSource' si el botón no es interactivo
    // val interactionSource = remember { MutableInteractionSource() }
    // val isPressed by interactionSource.collectIsPressedAsState()

    Button(
        onClick = { /* No hará nada */ },
        enabled = false, // <-- ¡LA MAGIA ESTÁ AQUÍ!
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(btnHeight),
        // Ya no necesitas las animaciones manuales, 'enabled = false' las anula
        // .scale(if (isPressed) 0.95f else 1f)
        // .alpha(if (isPressed) 0.8f else 1f),
        // interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent // Para que no cambie de color al deshabilitarse
        ),
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.dmbtn),
            contentDescription = "Siguiente",
            // El propio botón aplicará un alpha de deshabilitado.
            // Si no te gusta ese efecto, puedes añadir .alpha(1f) aquí.
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit // FillBounds suele ser mejor para botones
        )
    }
}