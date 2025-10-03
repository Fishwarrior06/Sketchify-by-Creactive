package com.creactive.sketchify.modules

import android.content.Context
import android.util.Log
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner

// CameraController.kt
class CameraController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    var previewView: PreviewView
) {
    var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    fun startCamera(onCameraReady: (() -> Unit)? = null) {  // ← Agrega callback
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            try {
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().apply {
                    surfaceProvider = previewView.surfaceProvider
                }
                val capture = ImageCapture.Builder().build()
                imageCapture = capture  // ← Asigna AQUÍ

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    capture
                )

                Log.d("CameraController", "🔸 Cámara inicializada exitosamente")
                onCameraReady?.invoke()  // ← Llama al callback cuando esté listo

            } catch (exc: Exception) {
                Log.e("CameraController", "🔴 Error al inicializar cámara: ${exc.message}", exc)
            }
        }, ContextCompat.getMainExecutor(context))
    }

    fun switchCamera() {
        cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
            CameraSelector.DEFAULT_BACK_CAMERA
        } else {
            CameraSelector.DEFAULT_FRONT_CAMERA
        }
        startCamera()
    }
}