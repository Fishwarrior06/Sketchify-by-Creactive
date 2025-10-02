package com.creactive.sketchify

// Imports necesarios para las notificaciones

// Tus imports existentes
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.creactive.sketchify.data.PhotoFrame
import com.creactive.sketchify.data.frames
import com.creactive.sketchify.modules.NotificationReceiver
import com.creactive.sketchify.ui.screens.HomeScreen
import com.creactive.sketchify.ui.screens.PhotoResultScreen
import com.creactive.sketchify.ui.theme.SketchifyTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        createNotificationChannel()

        setContent {
            SketchifyTheme {
                // ---> PASO 2: Preparar el lanzador de permisos.
                // Esto crea el "contrato" para pedir un permiso y manejar el resultado.
                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (isGranted) {
                            // Si el usuario acepta, llamamos a la función para programar.
                            scheduleNotification()
                        } else {
                            // Si el usuario deniega, le informamos.
                            showPermissionDeniedToast()
                        }
                    }
                )

                // ---> PASO 3: Lanzar la petición de permiso al iniciar la UI.
                // LaunchedEffect(Unit) se ejecuta una sola vez cuando el composable aparece.
                LaunchedEffect(Unit) {
                    // Solo pedimos permiso en Android 13 (TIRAMISU) o superior.
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        // En versiones anteriores, el permiso está concedido por defecto.
                        // Podemos programar la notificación directamente.
                        scheduleNotification()
                    }
                }

                // El resto de tu código de navegación no necesita cambios.
                val navController = rememberNavController()
                val windowSizeClass: WindowSizeClass = calculateWindowSizeClass(this)

                NavHost(navController = navController, startDestination = "home") {
                    composable("home") {
                        HomeScreen(navController, windowSizeClass)
                    }
                    // ... el resto de tus rutas composable ...
                    composable("PhotoResult") { backStackEntry ->
                        val photos = backStackEntry.savedStateHandle.get<ArrayList<Uri>>("photos")?.toList() ?: emptyList()
                        val frame = backStackEntry.savedStateHandle.get<PhotoFrame>("frame") ?: frames.first()
                        PhotoResultScreen(
                            photos = photos,
                            frame = frame,
                            onRetake = { navController.popBackStack() },
                            onSave = { },
                            windowSizeClass = windowSizeClass
                        )
                    }
                }
            }
        }
    }

    // ---> PASO 4: Añade estas funciones auxiliares al final de tu clase MainActivity.

    /**
     * Crea el canal de notificación necesario para Android 8.0 y superior.
     */
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

    // En MainActivity.kt
    private fun scheduleNotification() {
        NotificationReceiver.scheduleRandomAlarm(this)
    }

    private fun showPermissionDeniedToast() {
    }
}