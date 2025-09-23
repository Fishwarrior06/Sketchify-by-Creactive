package com.creactive.sketchify.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.creactive.sketchify.modules.PhotoBoothController
import com.creactive.sketchify.data.PhotoFrame

@Composable
fun PhotoBooth(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
    windowSizeClass: androidx.compose.material3.windowsizeclass.WindowSizeClass,
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
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Compact -> 350.dp
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Medium -> 175.dp
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Expanded -> 200.dp
        else -> 175.dp
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
            previewView = controller.previewView,
            onShutterClick = { controller.startPhotoSession() },
            onSwitchCameraClick = { controller.switchCamera() },
            showFlash = controller.showFlash,
            onFlashComplete = { controller.hideFlash() },
            showSnackbar = controller.showSnackbar,
            onSnackbarShown = { controller.hideSnackbar() },
            countdown = controller.countdown
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
    previewView: androidx.camera.view.PreviewView,
    onShutterClick: () -> Unit = {},
    onSwitchCameraClick: () -> Unit = {},
    showFlash: Boolean = false,
    onFlashComplete: () -> Unit = {},
    showSnackbar: Boolean = false,
    onSnackbarShown: () -> Unit = {},
    countdown: Int? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(showSnackbar) {
        if (showSnackbar) {
            snackbarHostState.showSnackbar("📸 Foto tomada")
            onSnackbarShown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(200.dp))

            // Camera Preview
            Box(
                modifier = Modifier
                    .width(boxWidth)
                    .height(boxWidth),
                contentAlignment = Alignment.Center
            ) {
                AndroidView(
                    factory = { previewView },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(200.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onSwitchCameraClick) { Text("🔄") }
                Button(onClick = onShutterClick) { Text("📸") }
            }
        }

        // Flash effect
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

        // Countdown overlay
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}