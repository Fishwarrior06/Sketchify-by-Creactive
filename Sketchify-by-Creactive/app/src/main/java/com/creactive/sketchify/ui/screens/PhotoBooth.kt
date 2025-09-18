package com.creactive.sketchify.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.creactive.sketchify.modules.CameraController
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "PhotoBooth"

@Composable
fun PhotoBooth(
    lifecycleOwner: LifecycleOwner,
    windowSizeClass: WindowSizeClass,
    onImageCaptured: (Uri) -> Unit = {}
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    // Cámara modular
    val cameraController = remember { CameraController(context, lifecycleOwner, previewView) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var capturedUri by remember { mutableStateOf<Uri?>(null) }

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

    // Tamaño del box según window class
    val boxWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 175.dp
        WindowWidthSizeClass.Medium -> 175.dp
        WindowWidthSizeClass.Expanded -> 200.dp
        else -> 175.dp
    }

    if (hasPermission) {
        DisposableEffect(Unit) {
            cameraController.startCamera()
            imageCapture = cameraController.imageCapture
            onDispose { /* opcional: unbind si quieres */ }
        }

        PhotoBoothUI(
            boxWidth = boxWidth,
            capturedUri = capturedUri,
            previewView = previewView,
            onShutterClick = {
                val file = File(
                    context.cacheDir,
                    "photo_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
                )
                val output = ImageCapture.OutputFileOptions.Builder(file).build()
                imageCapture?.takePicture(
                    output,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            val uri = Uri.fromFile(file)
                            capturedUri = uri
                            onImageCaptured(uri)
                        }

                        override fun onError(exception: androidx.camera.core.ImageCaptureException) {
                            Log.e(TAG, "Photo capture failed: ${exception.message}", exception)
                        }
                    }
                )
            }
        )
    } else {
        // ❌ Permisos no dados
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                Text("Dar permiso de cámara")
            }
        }
    }
}

// ------------------------
// UI modular separada
// ------------------------
@Composable
fun PhotoBoothUI(
    boxWidth: Dp,
    capturedUri: Uri? = null,
    previewView: PreviewView? = null,
    onShutterClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 📷 Recuadro de cámara
        Box(
            modifier = Modifier
                .width(boxWidth)
                .height(boxWidth),
            contentAlignment = Alignment.BottomCenter
        ) {
            previewView?.let { pv ->
                AndroidView(factory = { pv }, modifier = Modifier.fillMaxSize())
            } ?: Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    "Recuadro de cámara",
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Button(onClick = onShutterClick) {
                Text("📸")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 👀 Preview de la foto tomada
        capturedUri?.let { uri ->
            Text("Preview de la foto:")
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Foto tomada",
                modifier = Modifier
                    .size(150.dp)
                    .padding(top = 8.dp)
            )
        }
    }
}

// ------------------------
// Preview Compose (solo UI, no cámara real)
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun PhotoBoothUIPreview() {
    PhotoBoothUI(boxWidth = 175.dp)
}