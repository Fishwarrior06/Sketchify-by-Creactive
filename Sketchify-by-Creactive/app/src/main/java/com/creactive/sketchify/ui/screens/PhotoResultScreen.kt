package com.creactive.sketchify.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.creactive.sketchify.R
import com.creactive.sketchify.data.PhotoFrame
import com.creactive.sketchify.modules.PhotoResultController
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoResultScreen(
    navController: NavController,
    photos: List<Uri>,
    frame: PhotoFrame,
    onSave: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    val context = LocalContext.current
    val controller = remember { PhotoResultController(context) }
    val captureController = rememberCaptureController()
    val coroutineScope = rememberCoroutineScope()

    // Tamaños responsivos para el marco
    val containerWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 585.dp
        WindowWidthSizeClass.Medium -> 700.dp
        WindowWidthSizeClass.Expanded -> 800.dp
        else -> 585.dp
    }
    val containerHeight = containerWidth * 1.5f

    Scaffold(
        // Hacemos el Scaffold transparente para ver nuestro fondo
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Fondo de pantalla
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Columna principal que organiza la pantalla verticalmente
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TopNavButtons(navController = navController, windowSizeClass = windowSizeClass)

                // Columna interna para distribuir el espacio restante
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Box flexible para centrar el marco
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        PhotoFrameOverlay(
                            photos = photos,
                            frame = frame,
                            containerWidth = containerWidth,
                            containerHeight = containerHeight,
                            captureController = captureController
                        )
                    }

                    // Spacer que ya tenías para separar el marco de los botones inferiores
                    Spacer(modifier = Modifier.height(16.dp))

                    // Barra de botones inferior
                    PhotoResultBottomBar(
                        navController = navController,
                        controller = controller,
                        captureController = captureController,
                        coroutineScope = coroutineScope,
                        onSave = onSave,
                        windowSizeClass = windowSizeClass
                    )
                }
            }
        }
    }
}

// --- NUEVO COMPOSABLE PARA LOS BOTONES SUPERIORES ---
@Composable
private fun TopNavButtons(navController: NavController, windowSizeClass: WindowSizeClass) {
    // Tamaños responsivos para los botones superiores
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

    // Interaction sources para las animaciones
    val interactionSourceBack = remember { MutableInteractionSource() }
    val isPressedBack by interactionSourceBack.collectIsPressedAsState()

    val interactionSourceHome = remember { MutableInteractionSource() }
    val isPressedHome by interactionSourceHome.collectIsPressedAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp), // Padding para separarlos de los bordes
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // BOTÓN 1 - VOLVER
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

        // BOTÓN 2 - IR A INICIO
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


// --- BARRA DE BOTONES INFERIOR ---
@Composable
private fun PhotoResultBottomBar(
    modifier: Modifier = Modifier,
    navController: NavController,
    controller: PhotoResultController,
    captureController: CaptureController,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onSave: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    val interactionSourceSave = remember { MutableInteractionSource() }
    val isPressedSave by interactionSourceSave.collectIsPressedAsState()

    val interactionSourceIllustrate = remember { MutableInteractionSource() }
    val isPressedIllustrate by interactionSourceIllustrate.collectIsPressedAsState()

    val btnHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 60.dp
        WindowWidthSizeClass.Medium -> 90.dp
        WindowWidthSizeClass.Expanded -> 120.dp
        else -> 60.dp
    }

    val buttonSpacing = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 8.dp
        WindowWidthSizeClass.Medium -> 12.dp
        WindowWidthSizeClass.Expanded -> 20.dp
        else -> 12.dp
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // --- BOTÓN DE GUARDAR ---
        Button(
            onClick = {
                coroutineScope.launch {
                    try {
                        val imageBitmap = captureController.captureAsync().await()
                        val androidBitmap: Bitmap = imageBitmap.asAndroidBitmap()
                        controller.saveCapturedBitmap(androidBitmap) { success ->
                            if (success) {
                                onSave()
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("PhotoResult", "Error capturing composable", e)
                    }
                }
            },
            modifier = Modifier
                .weight(1f)
                .padding(end = buttonSpacing) // Corregido: padding final
                .scale(if (isPressedSave) 0.95f else 1f)
                .alpha(if (isPressedSave) 0.8f else 1f),
            interactionSource = interactionSourceSave,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.savebtn),
                contentDescription = "Guardar Marco",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(btnHeight),
                contentScale = ContentScale.FillBounds
            )
        }

        // --- BOTÓN DE ILUSTRAR ---
        Button(
            onClick = {
                navController.navigate("illustrate")
            },
            modifier = Modifier
                .weight(1f)
                .padding(start = buttonSpacing)
                .scale(if (isPressedIllustrate) 0.95f else 1f)
                .alpha(if (isPressedIllustrate) 0.8f else 1f),
            interactionSource = interactionSourceIllustrate,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp, pressedElevation = 0.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.illustratebtn),
                contentDescription = "Ilustrar",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(btnHeight),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}