package com.creactive.sketchify.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap // ✅ IMPORT NECESARIO
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.creactive.sketchify.data.PhotoFrame
import com.creactive.sketchify.modules.PhotoResultController
import dev.shreyaspatil.capturable.controller.CaptureController
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoResultScreen(
    photos: List<Uri>,
    frame: PhotoFrame,
    onRetake: () -> Unit,
    onSave: () -> Unit,
    windowSizeClass: WindowSizeClass
) {
    val context = LocalContext.current
    val controller = remember { PhotoResultController(context) }
    val captureController = rememberCaptureController()
    val coroutineScope = rememberCoroutineScope()

    // Responsive sizing
    val containerWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 585.dp
        WindowWidthSizeClass.Medium -> 700.dp
        WindowWidthSizeClass.Expanded -> 800.dp
        else -> 585.dp
    }

    val containerHeight = containerWidth * 1.5f

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Resultados - ${photos.size} fotos")
                }
            )
        },
        bottomBar = {
            PhotoResultBottomBar(
                controller = controller,
                captureController = captureController,
                coroutineScope = coroutineScope,
                onRetake = onRetake,
                onSave = onSave
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            PhotoFrameOverlay(
                photos = photos,
                frame = frame,
                containerWidth = containerWidth,
                containerHeight = containerHeight,
                captureController = captureController
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PhotoResultBottomBar(
    controller: PhotoResultController,
    captureController: CaptureController,
    coroutineScope: kotlinx.coroutines.CoroutineScope,
    onRetake: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(onClick = onRetake) {
            Text("📷 Repetir")
        }

        Button(onClick = {
            Log.d("PhotoResult", "Capturing composable...")

            coroutineScope.launch {
                try {
                    // ✅ CAPTURAR ImageBitmap y convertir a Android Bitmap
                    val imageBitmap = captureController.captureAsync().await()
                    val androidBitmap: Bitmap = imageBitmap.asAndroidBitmap() // ✅ CONVERSIÓN

                    Log.d("PhotoResult", "Composable captured successfully!")
                    controller.saveCapturedBitmap(androidBitmap) { success ->
                        if (success) {
                            Log.d("PhotoResult", "Screenshot saved to gallery!")
                            onSave()
                        } else {
                            Log.e("PhotoResult", "Failed to save screenshot")
                        }
                    }
                } catch (e: Exception) {
                    Log.e("PhotoResult", "Error capturing composable", e)
                }
            }
        }) {
            Text("💾 Guardar Marco")
        }
    }
}