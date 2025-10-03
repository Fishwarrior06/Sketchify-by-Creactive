package com.creactive.sketchify

// Imports de ambos archivos combinados
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraProvider
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.extensions.ExtensionMode
import androidx.camera.extensions.ExtensionsManager
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.creactive.sketchify.data.PhotoFrame
import com.creactive.sketchify.data.frames
import com.creactive.sketchify.modules.NotificationReceiver
import com.creactive.sketchify.ui.screens.HomeScreen
import com.creactive.sketchify.ui.screens.IllustrateScreen
import com.creactive.sketchify.ui.screens.PastSessionsScreen
import com.creactive.sketchify.ui.screens.PhotoBooth
import com.creactive.sketchify.ui.screens.PhotoResultScreen
import com.creactive.sketchify.ui.screens.PhotoTypeScreen
import com.creactive.sketchify.ui.theme.SketchifyTheme
import kotlinx.coroutines.delay

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        // --- INICIO DE CÓDIGO SIN CAMBIOS ---
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        var uiIsReady by mutableStateOf(false)
        splashScreen.setKeepOnScreenCondition { !uiIsReady }

        enableEdgeToEdge()

        createNotificationChannel()

        val lifecycleOwner = this
        val cameraProviderFuture = ProcessCameraProvider.getInstance(applicationContext)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            val extensionsManagerFuture =
                ExtensionsManager.getInstanceAsync(
                    applicationContext,
                    cameraProvider as CameraProvider
                )
            extensionsManagerFuture.addListener({
                val extensionsManager = extensionsManagerFuture.get()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                if (extensionsManager.isExtensionAvailable(cameraSelector, ExtensionMode.NIGHT)) {
                    try {
                        cameraProvider.unbindAll()
                        val nightCameraSelector =
                            extensionsManager.getExtensionEnabledCameraSelector(
                                cameraSelector,
                                ExtensionMode.NIGHT
                            )
                        val imageCapture = ImageCapture.Builder().build()
                        val preview = Preview.Builder().build()
                        val previewView = PreviewView(this)
                        preview.surfaceProvider = previewView.surfaceProvider
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
        // --- FIN DE CÓDIGO SIN CAMBIOS ---

        setContent {
            LaunchedEffect(Unit) {
                delay(300)
                uiIsReady = true
            }

            if (uiIsReady) {
                SketchifyTheme {
                    val permissionLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            if (isGranted) {
                                scheduleNotification()
                            } else {
                                showPermissionDeniedToast()
                            }
                        }
                    )

                    LaunchedEffect(Unit) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        } else {
                            scheduleNotification()
                        }
                    }

                    val navController = rememberNavController()
                    val windowSizeClass: WindowSizeClass = calculateWindowSizeClass(this)

                    NavHost(navController = navController, startDestination = "home") {

                        composable("home") {
                            HomeScreen(navController, windowSizeClass)
                        }

                        composable(
                            route = "PhotoType/{modo}",
                            arguments = listOf(navArgument("modo") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val modo = backStackEntry.arguments?.getString("modo") ?: "camara"
                            PhotoTypeScreen(navController, modo, windowSizeClass)
                        }

                        composable("PastSessions") {
                            PastSessionsScreen(navController, windowSizeClass)
                        }

                        composable(
                            route = "PhotoBooth/{modo}/{frameName}",
                            arguments = listOf(
                                navArgument("modo") { type = NavType.StringType },
                                navArgument("frameName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val modo = backStackEntry.arguments?.getString("modo") ?: "camara"
                            val frameName = backStackEntry.arguments?.getString("frameName")
                                ?: frames.first().name
                            val selectedFrame =
                                frames.find { it.name == frameName } ?: frames.first()

                            PhotoBooth(
                                navController = navController,
                                lifecycleOwner = this@MainActivity,
                                windowSizeClass = windowSizeClass,
                                selectedFrame = selectedFrame,
                                modo = modo
                            )
                        }

                        // <-- ¡LA SOLUCIÓN ESTÁ AQUÍ! -->
                        composable("PhotoResult") {
                            // En lugar de buscar en el "casillero" de la pantalla actual (que está vacío),
                            // buscamos en el "casillero" de la PANTALLA ANTERIOR (`previousBackStackEntry`),
                            // que es donde `GalleryLauncher` dejó los datos.
                            val photos =
                                navController.previousBackStackEntry?.savedStateHandle?.get<ArrayList<Uri>>("photos")
                                    ?.toList() ?: emptyList()
                            val frame = navController.previousBackStackEntry?.savedStateHandle?.get<PhotoFrame>("frame")
                                ?: frames.first()

                            Log.d("SOLUCION", "PhotoResult ha RECIBIDO ${photos.size} fotos y el frame '${frame.name}'")

                            PhotoResultScreen(
                                photos = photos,
                                frame = frame,
                                onSave = {
                                    // Después de guardar, volvemos al inicio limpiando la pila.
                                    navController.navigate("home") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                },
                                windowSizeClass = windowSizeClass,
                                navController = navController
                            )
                        }
                        composable (route = "Illustrate") {
                            IllustrateScreen(navController, windowSizeClass)
                        }
                    }
                }
            }
        }
    }

    // --- INICIO DE CÓDIGO SIN CAMBIOS ---
    private fun createNotificationChannel() {
        val channelId = "reminder_channel_id"
        val name = "Recordatorios de Sketchify"
        val descriptionText = "Canal para notificar recordatorios de la aplicación."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun scheduleNotification() {
        NotificationReceiver.scheduleRandomAlarm(this)
    }

    private fun showPermissionDeniedToast() {
        Log.d(TAG, "Permiso de notificaciones denegado.")
    }
}