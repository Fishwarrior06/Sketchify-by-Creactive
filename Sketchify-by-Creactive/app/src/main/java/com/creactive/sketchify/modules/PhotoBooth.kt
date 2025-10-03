package com.creactive.sketchify.modules

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.creactive.sketchify.data.PhotoFrame
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PhotoBoothController(
    private val context: Context,
    lifecycleOwner: LifecycleOwner,
    private val navController: NavController,
    private val selectedFrame: PhotoFrame
) {
    // Estados (SIN CAMBIOS)
    var photos by mutableStateOf<List<Uri>>(emptyList())
        private set
    var showFlash by mutableStateOf(false)
        private set
    var showSnackbar by mutableStateOf(false)
        private set
    var countdown by mutableStateOf<Int?>(null)
        private set
    var takingPhotos by mutableStateOf(false)
        private set
    var imageCapture by mutableStateOf<ImageCapture?>(null)
        private set

    // Camera (SIN CAMBIOS)
    val previewView = PreviewView(context)
    private val cameraController = CameraController(context, lifecycleOwner, previewView)

    fun initializeCamera() {
        Log.d("PhotoBooth", "🔸 Inicializando cámara...")
        cameraController.startCamera {
            imageCapture = cameraController.imageCapture
            Log.d("PhotoBooth", "🔸 Cámara lista! imageCapture: $imageCapture")
        }
    }

    // Funciones de la sesión de fotos (SIN CAMBIOS)
    fun startPhotoSession() {
        if (!takingPhotos) {
            Log.d("PhotoBooth", "🔸 Iniciando sesión de fotos - Frame slots: ${selectedFrame.slots}")
            photos = emptyList()
            takingPhotos = true
            countdown = 3
        }
    }

    fun handleCountdown() {
        val current = countdown
        Log.d("PhotoBooth", "🔸 Countdown: $current")
        if (current != null && current > 0) {
            countdown = current - 1
        } else if (current == 0) {
            Log.d("PhotoBooth", "🔸 Countdown terminado, tomando foto")
            countdown = null
            takePhoto()
        }
    }

    private fun takePhoto() {
        Log.d("PhotoBooth", "🔸 takePhoto() INICIADA")
        val capture = imageCapture
        if (capture == null) {
            Log.e("PhotoBooth", "🔴 ERROR: imageCapture es NULL")
            return
        }
        Log.d("PhotoBooth", "🔸 imageCapture OK, creando archivo...")
        val file = File(
            context.cacheDir,
            "photo_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}.jpg"
        )
        val output = ImageCapture.OutputFileOptions.Builder(file).build()

        Log.d("PhotoBooth", "🔸 Archivo creado: ${file.absolutePath}")
        Log.d("PhotoBooth", "🔸 Iniciando captura...")

        capture.takePicture(
            output,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    Log.d("PhotoBooth", "🔸 onImageSaved - Foto guardada exitosamente!")
                    val uri = Uri.fromFile(file)
                    photos = photos + uri
                    showFlash = true
                    showSnackbar = true
                    Log.d("PhotoBooth", "🔸 Total photos: ${photos.size}, Required slots: ${selectedFrame.slots}")

                    if (photos.size < selectedFrame.slots) {
                        Log.d("PhotoBooth", "🔸 Necesita más fotos, iniciando countdown")
                        countdown = 3
                    } else {
                        Log.d("PhotoBooth", "🔸 ¡Todas las fotos tomadas! Llamando finishPhotoSession()")
                        finishPhotoSession()
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("PhotoBooth", "🔴 ERROR en captura de foto: ${exception.message}", exception)
                    takingPhotos = false
                    countdown = null
                }
            }
        )
        Log.d("PhotoBooth", "🔸 takePicture() llamado, esperando callback...")
    }

    // ========================================================== //
    // =========== ¡AQUÍ ESTÁ EL CAMBIO FUNDAMENTAL! ============ //
    // ========================================================== //

    private fun finishPhotoSession() {
        takingPhotos = false
        countdown = null

        Log.d("PhotoBooth", "🔸 Finalizando sesión con ${photos.size} fotos")
        Log.d("PhotoBooth", "🔸 Frame: ${selectedFrame.name}")

        // PASO 1: Guardar los datos en el "casillero" de la pantalla ACTUAL (PhotoBooth).
        // PhotoResult los leerá desde aquí usando `previousBackStackEntry`.
        navController.currentBackStackEntry?.savedStateHandle?.apply {
            set("photos", ArrayList(photos))
            set("frame", selectedFrame)
            Log.d("PhotoBooth", "🔸 Datos guardados en el casillero de PhotoBooth.")
        }

        // PASO 2: Ahora que los datos están guardados, navegamos.
        navController.navigate("PhotoResult")
    }


    fun hideFlash() {
        showFlash = false
    }

    fun hideSnackbar() {
        showSnackbar = false
    }
}