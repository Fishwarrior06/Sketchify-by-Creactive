package com.creactive.sketchify.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
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
import com.creactive.sketchify.modules.CameraController
import com.creactive.sketchify.data.PhotoFrame
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "PhotoBooth"

@Composable
fun PhotoBooth(
    navController: NavController,
    lifecycleOwner: LifecycleOwner,
    windowSizeClass: androidx.compose.material3.windowsizeclass.WindowSizeClass,
    selectedFrame: PhotoFrame
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }
    val cameraController = remember { CameraController(context, lifecycleOwner, previewView) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var photos by remember { mutableStateOf<List<Uri>>(emptyList()) }

    var showFlash by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf<Int?>(null) }
    var takingPhotos by remember { mutableStateOf(false) }

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
    LaunchedEffect(Unit) { if (!hasPermission) launcher.launch(Manifest.permission.CAMERA) }

    val boxWidth = when (windowSizeClass.widthSizeClass) {
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Compact -> 350.dp
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Medium -> 175.dp
        androidx.compose.material3.windowsizeclass.WindowWidthSizeClass.Expanded -> 200.dp
        else -> 175.dp
    }

    if (hasPermission) {
        DisposableEffect(Unit) {
            cameraController.startCamera()
            imageCapture = cameraController.imageCapture
            onDispose { }
        }

        // lógica de tomar foto
        fun takePhoto() {
            val capture = imageCapture ?: return
            val file = File(
                context.cacheDir,
                "photo_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
            )
            val output = ImageCapture.OutputFileOptions.Builder(file).build()

            capture.takePicture(
                output,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val uri = Uri.fromFile(file)
                        photos = photos + uri
                        showFlash = true
                        showSnackbar = true

                        if (photos.size < selectedFrame.slots) {
                            countdown = 3 // siguiente foto
                        } else {
                            takingPhotos = false
                            countdown = null

                            // Navegar a PhotoResultScreen pasando frame y fotos
                            // Al terminar de tomar fotos en PhotoBooth
                            // Dentro de PhotoBooth, cuando terminas de tomar fotos
                            navController.currentBackStackEntry?.savedStateHandle?.set("photos", ArrayList(photos))
                            navController.currentBackStackEntry?.savedStateHandle?.set("frame", selectedFrame)
                            Log.d("PhotoBooth", "Photos saved: ${photos.size}, Frame: ${selectedFrame.name}")
                            // Luego navega
                            navController.navigate("PhotoResult")
                        }
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                        takingPhotos = false
                        countdown = null
                    }
                }
            )
        }

        // controlar countdown
        LaunchedEffect(countdown) {
            val current = countdown
            if (current != null && current > 0) {
                kotlinx.coroutines.delay(1000)
                countdown = current - 1
            } else if (current == 0) {
                countdown = null
                takePhoto()
            }
        }

        PhotoBoothUI(
            boxWidth = boxWidth,
            previewView = previewView,
            onShutterClick = {
                if (!takingPhotos) {
                    photos = emptyList()
                    takingPhotos = true
                    countdown = 3 // primera foto
                }
            },
            onSwitchCameraClick = { cameraController.switchCamera() },
            showFlash = showFlash,
            onFlashComplete = { showFlash = false },
            showSnackbar = showSnackbar,
            onSnackbarShown = { showSnackbar = false },
            countdown = countdown
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                Text("Dar permiso de cámara")
            }
        }
    }
}

@Composable
fun PhotoBoothUI(
    boxWidth: Dp,
    previewView: PreviewView? = null,
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

            Box(
                modifier = Modifier
                    .width(boxWidth)
                    .height(boxWidth),
                contentAlignment = Alignment.Center
            ) {
                previewView?.let { pv ->
                    AndroidView(factory = { pv }, modifier = Modifier.fillMaxSize())
                } ?: Box(modifier = Modifier.fillMaxSize()) {
                    Text("Recuadro de cámara", modifier = Modifier.align(Alignment.Center))
                }
            }

            Spacer(modifier = Modifier.height(200.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onSwitchCameraClick) { Text("🔄") }
                Button(onClick = onShutterClick) { Text("📸") }
            }
        }

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