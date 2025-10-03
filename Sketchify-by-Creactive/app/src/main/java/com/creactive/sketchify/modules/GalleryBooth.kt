package com.creactive.sketchify.modules

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.creactive.sketchify.data.PhotoFrame

/**
 * Un Composable que lanza la galería para seleccionar una o varias imágenes,
 * dependiendo del número de 'slots' del marco de fotos seleccionado.
 */
@Composable
fun GalleryLauncher(
    navController: NavController,
    selectedFrame: PhotoFrame
) {
    // Función común para navegar al resultado, para no repetir código.
    val navigateToResult = { uris: List<Uri> ->
        if (uris.isNotEmpty()) {
            // ¡IMPORTANTE! Esta línea guarda los datos en el "estado" de la pantalla actual (PhotoBooth)
            // antes de navegar a la siguiente.
            Log.d("SOLUCION", "Guardando ${uris.size} fotos y el frame '${selectedFrame.name}'")
            navController.currentBackStackEntry?.savedStateHandle?.set("photos", ArrayList(uris))
            navController.currentBackStackEntry?.savedStateHandle?.set("frame", selectedFrame)
            navController.navigate("PhotoResult")
        } else {
            navController.popBackStack() // Si el usuario cancela, simplemente regresa.
        }
    }

    // --- CASO 1: Solo se necesita UNA foto ---
    val singlePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            // Si el usuario eligió una foto (uri no es null), la envolvemos en una lista y navegamos.
            navigateToResult(if (uri != null) listOf(uri) else emptyList())
        }
    )

    // --- CASO 2: Se necesitan VARIAS fotos ---
    // Este lanzador solo se usará si slots > 1.
    val multiplePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(
            // Le ponemos un mínimo de 2 para evitar el crash si por error se llamara con slots=1
            maxItems = maxOf(2, selectedFrame.slots)
        ),
        onResult = { uris: List<Uri> ->
            // El resultado ya es una lista, así que la pasamos directamente.
            navigateToResult(uris)
        }
    )

    // --- Lógica para decidir cuál lanzador usar ---
    LaunchedEffect(Unit) {
        val request = PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        if (selectedFrame.slots == 1) {
            singlePhotoLauncher.launch(request)
        } else {
            multiplePhotoLauncher.launch(request)
        }
    }

    // La UI de carga no cambia, se muestra mientras el selector de fotos está abierto.
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.7f)),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}