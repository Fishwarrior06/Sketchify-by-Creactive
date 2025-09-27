package com.creactive.sketchify.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.creactive.sketchify.R
import com.creactive.sketchify.data.PhotoFrame
import com.creactive.sketchify.modules.PhotoBoothController

@Composable
fun PhotoBooth(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
    windowSizeClass: WindowSizeClass,
    selectedFrame: PhotoFrame
) {
    val context = LocalContext.current

    // Controller que maneja toda la lógica
    val controller = remember {
        PhotoBoothController(context, lifecycleOwner, navController, selectedFrame)
    }

    // Permisos
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasPermission) launcher.launch(Manifest.permission.CAMERA)
    }

    // Responsive sizing
    val boxWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 350.dp
        WindowWidthSizeClass.Medium -> 450.dp
        WindowWidthSizeClass.Expanded -> 550.dp
        else -> 350.dp
    }

    val boxHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 500.dp
        WindowWidthSizeClass.Medium -> 600.dp
        WindowWidthSizeClass.Expanded -> 700.dp
        else -> 500.dp
    }

    val btnHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 50.dp
        WindowWidthSizeClass.Medium -> 80.dp
        WindowWidthSizeClass.Expanded -> 110.dp
        else -> 50.dp
    }

    val btnWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 60.dp
        WindowWidthSizeClass.Medium -> 90.dp
        WindowWidthSizeClass.Expanded -> 120.dp
        else -> 60.dp
    }

    val spacing = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 10.dp
        WindowWidthSizeClass.Medium -> 14.dp
        WindowWidthSizeClass.Expanded -> 16.dp
        else -> 8.dp
    }

    if (hasPermission) {
        // Inicializar cámara
        DisposableEffect(Unit) {
            controller.initializeCamera()
            onDispose { }
        }

        // Manejar countdown
        LaunchedEffect(controller.countdown) {
            if (controller.countdown != null && controller.countdown!! > 0) {
                kotlinx.coroutines.delay(1000)
                controller.handleCountdown()
            } else if (controller.countdown == 0) {
                controller.handleCountdown()
            }
        }

        // UI
        PhotoBoothUI(
            boxWidth = boxWidth,
            boxHeight = boxHeight,
            btnWidth = btnWidth,
            spacing = spacing,
            btnHeight = btnHeight,
            previewView = controller.previewView,
            onShutterClick = { controller.startPhotoSession() },
            showFlash = controller.showFlash,
            onFlashComplete = { controller.hideFlash() },
            showSnackbar = controller.showSnackbar,
            onSnackbarShown = { controller.hideSnackbar() },
            countdown = controller.countdown,
            navController = navController
        )
    } else {
        PermissionUI(onRequestPermission = { launcher.launch(Manifest.permission.CAMERA) })
    }
}

@Composable
private fun PermissionUI(onRequestPermission: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = onRequestPermission) {
            Text("Dar permiso de cámara")
        }
    }
}

@Composable
fun PhotoBoothUI(
    boxWidth: Dp,
    boxHeight: Dp,
    btnWidth: Dp,
    btnHeight: Dp,
    spacing: Dp,
    previewView: androidx.camera.view.PreviewView? = null,
    onShutterClick: () -> Unit = {},
    showFlash: Boolean = false,
    onFlashComplete: () -> Unit = {},
    showSnackbar: Boolean = false,
    onSnackbarShown: () -> Unit = {},
    countdown: Int? = null,
    navController: NavController? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar("📸 Foto tomada")
            onSnackbarShown()
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
    ) {
        // 🖼️ IMAGEN DE FONDO
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 🔝 BOTONES SUPERIORES
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // BOTÓN 1 - VOLVER
            val interactionSource1 = remember { MutableInteractionSource() }
            val isPressed1 by interactionSource1.collectIsPressedAsState()

            Button(
                onClick = {
                    navController?.popBackStack()
                },
                modifier = Modifier
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
                    painter = painterResource(id= R.drawable.backbutton),
                    contentDescription = "BackButton",
                    modifier = Modifier
                        .width(btnWidth)
                        .height(btnHeight)
                )
            }

            // BOTÓN 2 - OTRA ACCIÓN
            val interactionSource2 = remember { MutableInteractionSource() }
            val isPressed2 by interactionSource2.collectIsPressedAsState()

            Button(
                onClick = {
                    // Acción del segundo botón
                },
                modifier = Modifier
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
                    painter = painterResource(id = R.drawable.home_button),
                    contentDescription = "HomeButton",
                    modifier = Modifier
                        .width(btnWidth)
                        .height(btnHeight)
                )
            }
        }

        // 📱 CONTENIDO CENTRADO
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // 📷 CAMERA PREVIEW CON SHUTTER BUTTON
                Box(
                    modifier = Modifier
                        .width(boxWidth),
                    contentAlignment = Alignment.Center
                ) {
                    // CAMERA BOX
                    Box(
                        modifier = Modifier
                            .width(boxWidth)
                            .height(boxHeight)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color.Gray),
                        contentAlignment = Alignment.Center
                    ) {
                        if (previewView != null) {
                            AndroidView(
                                factory = { previewView },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(20.dp))
                            )
                        } else {
                            Text(
                                text = "📷\nCamera Preview",
                                color = Color.White,
                                style = MaterialTheme.typography.headlineSmall
                            )
                        }
                    }

                    // SHUTTER BUTTON - Alineado con el borde inferior
                    val interactionSource3 = remember { MutableInteractionSource() }
                    val isPressed3 by interactionSource3.collectIsPressedAsState()

                    Button(
                        onClick = onShutterClick,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = spacing, end = spacing)
                            .scale(if (isPressed3) 0.95f else 1f)
                            .alpha(if (isPressed3) 0.8f else 1f),
                        interactionSource = interactionSource3,
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
                            painter = painterResource(id = R.drawable.shutterbutton),
                            contentDescription = "ShutterButton",
                            modifier = Modifier
                                .width(btnWidth)
                                .height(btnWidth)
                        )
                    }
                }
            }
        }

        // ⚡ FLASH EFFECT
        if (showFlash) {
            LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(150)
                onFlashComplete()
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f))
            )
        }

        // ⏰ COUNTDOWN OVERLAY
        countdown?.let { value ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value.toString(),
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White
                )
            }
        }

        // 📢 SNACKBAR
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

// ✅ PREVIEW NORMAL
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PhotoBoothUIPreview() {
    MaterialTheme {
        PhotoBoothUI(
            boxWidth = 350.dp,
            boxHeight = 500.dp,
            btnWidth = 60.dp,
            btnHeight = 50.dp,
            spacing = 10.dp,
            previewView = null,
            onShutterClick = {},
            showFlash = false,
            onFlashComplete = {},
            showSnackbar = false,
            onSnackbarShown = {},
            countdown = null,
            navController = rememberNavController()
        )
    }
}

// ✅ PREVIEW CON COUNTDOWN
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PhotoBoothUIPreviewWithCountdown() {
    MaterialTheme {
        PhotoBoothUI(
            boxWidth = 350.dp,
            boxHeight = 500.dp,
            btnWidth = 60.dp,
            btnHeight = 50.dp,
            spacing = 10.dp,
            previewView = null,
            countdown = 3,
            navController = rememberNavController()
        )
    }
}

// ✅ PREVIEW CON FLASH
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PhotoBoothUIPreviewWithFlash() {
    MaterialTheme {
        PhotoBoothUI(
            boxWidth = 350.dp,
            boxHeight = 500.dp,
            btnWidth = 60.dp,
            btnHeight = 50.dp,
            spacing = 10.dp,
            previewView = null,
            showFlash = true,
            navController = rememberNavController()
        )
    }
}