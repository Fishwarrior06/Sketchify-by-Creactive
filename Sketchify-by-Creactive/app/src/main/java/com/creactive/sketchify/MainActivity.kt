package com.creactive.sketchify
//Screens
import android.net.Uri
import com.creactive.sketchify.ui.screens.HomeScreen
import com.creactive.sketchify.ui.screens.PhotoTypeScreen
import com.creactive.sketchify.ui.screens.PastSessionsScreen
import com.creactive.sketchify.ui.screens.PhotoBooth

//Theme
import com.creactive.sketchify.ui.theme.SketchifyTheme

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.camera.extensions.ExtensionMode
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.creactive.sketchify.data.PhotoFrame
import com.creactive.sketchify.data.frames
import com.creactive.sketchify.ui.screens.PhotoResultScreen
import com.creactive.sketchify.ui.screens.savePhotosToGallery

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //Camera Stuff
        val lifecycleOwner = this
        val cameraProviderFuture = ProcessCameraProvider.getInstance(applicationContext)
        cameraProviderFuture.addListener({
            // Obtain an instance of a process camera provider
            // The camera provider provides access to the set of cameras associated with the device.
            // The camera obtained from the provider will be bound to the activity lifecycle.
            val cameraProvider = cameraProviderFuture.get()
            val extensionsManagerFuture =
                ExtensionsManager.getInstanceAsync(applicationContext,
                    cameraProvider as CameraProvider
                )
            extensionsManagerFuture.addListener({
                // Obtain an instance of the extensions manager
                // The extensions manager enables a camera to use extension capabilities available on
                // the device.
                val extensionsManager = extensionsManagerFuture.get()
                // Select the camera
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                // Query if extension is available.
                // Not all devices will support extensions or might only support a subset of
                // extensions.
                if (extensionsManager.isExtensionAvailable(cameraSelector, ExtensionMode.NIGHT)) {
                    // Unbind all use cases before enabling different extension modes.
                    try {
                        cameraProvider.unbindAll()
                        // Retrieve a night extension enabled camera selector
                        val nightCameraSelector =
                            extensionsManager.getExtensionEnabledCameraSelector(
                                cameraSelector,
                                ExtensionMode.NIGHT
                            )
                        // Bind image capture and preview use cases with the extension enabled camera
                        // selector.
                        val imageCapture = ImageCapture.Builder().build()
                        val preview = Preview.Builder().build()
                        // Connect the preview to receive the surface the camera outputs the frames
                        // to. This will allow displaying the camera frames in either a TextureView
                        // or SurfaceView. The SurfaceProvider can be obtained from the PreviewView.
                        val previewView = PreviewView(this)
                        preview.surfaceProvider = previewView.surfaceProvider
                        // Returns an instance of the camera bound to the lifecycle
                        // Use this camera object to control various operations with the camera
                        // Example: flash, zoom, focus metering etc.
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            nightCameraSelector,
                            imageCapture,
                            preview
                        )
                    } catch (e: Exception) {
                        Log.e(TAG, "Use case binding failed", e)
                    }
                }
            }, ContextCompat.getMainExecutor(this))
        }, ContextCompat.getMainExecutor(this))

        //Navigation Host and stuff
        setContent {
            SketchifyTheme {
                val navController = rememberNavController()

                // 👇 calcular window size una sola vez
                val windowSizeClass: WindowSizeClass = calculateWindowSizeClass(this)

                NavHost(navController = navController, startDestination = "home") {

                    composable("home") {
                        HomeScreen(navController, windowSizeClass)
                    }

                    composable(
                        route = "PhotoType?modo={modo}",
                        arguments = listOf(
                            navArgument("modo") {
                                type = NavType.StringType
                                defaultValue = "camara"
                                nullable = true
                            }
                        )
                    ) { backStackEntry ->
                        val modo = backStackEntry.arguments?.getString("modo") ?: "camara"
                        PhotoTypeScreen(navController, modo, windowSizeClass)
                    }

                    composable("PastSessions") {
                        PastSessionsScreen(navController, windowSizeClass)
                    }

                    composable("PhotoBooth") { backStackEntry ->
                        val selectedFrame = navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.get<PhotoFrame>("selectedFrame") ?: frames.first()

                        PhotoBooth(
                            navController = navController,
                            lifecycleOwner = this@MainActivity,
                            windowSizeClass = windowSizeClass,
                            selectedFrame = selectedFrame
                        )
                    }

                    composable("PhotoResult") { backStackEntry ->
                        // ✅ Obtén desde la entrada anterior (de donde vienes)
                        val previousEntry = navController.previousBackStackEntry
                        val photos = previousEntry?.savedStateHandle?.get<ArrayList<Uri>>("photos")?.toList() ?: emptyList()
                        val frame = previousEntry?.savedStateHandle?.get<PhotoFrame>("frame") ?: frames.first()
                        Log.d("PhotoResult", "Received photos: ${photos.size}, Frame: ${frame.name}")

                        val context = LocalContext.current

                        PhotoResultScreen(
                            photos = photos,
                            frame = frame,
                            onRetake = { navController.popBackStack() },
                            onSave = { savePhotosToGallery(context, photos) }
                        )
                    }
                }
            }
        }
    }
}