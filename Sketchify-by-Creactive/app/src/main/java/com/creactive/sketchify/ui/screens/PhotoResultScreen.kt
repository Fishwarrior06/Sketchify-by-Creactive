package com.creactive.sketchify.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.creactive.sketchify.R
import com.creactive.sketchify.data.PhotoFrame
import com.creactive.sketchify.data.PhotoSlot
import com.creactive.sketchify.modules.PhotoResultController
import com.creactive.sketchify.ui.components.PhotoFrameOverlay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoResultScreen(
    photos: List<Uri>,
    frame: PhotoFrame,
    onRetake: () -> Unit,
    onSave: () -> Unit,
    windowSizeClass: WindowSizeClass,
) {
    val context = LocalContext.current
    val controller = remember { PhotoResultController(context) }

    // Responsive sizing
    val containerWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 585.dp
        WindowWidthSizeClass.Medium -> 700.dp
        WindowWidthSizeClass.Expanded -> 800.dp
        else -> 585.dp
    }

    val containerHeight = containerWidth * 1.5f // Ajusta según tu ratio de frame

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resultados") }) },
        bottomBar = {
            PhotoResultBottomBar(
                onRetake = onRetake,
                onSave = {
                    controller.handleSave(photos)
                    onSave()
                }
            )
        }
    ) { padding ->
        PhotoResultContent(
            photos = photos,
            frame = frame,
            containerWidth = containerWidth,
            containerHeight = containerHeight,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
private fun PhotoResultBottomBar(
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
            Text("Repetir")
        }
        Button(onClick = onSave) {
            Text("Guardar")
        }
    }
}

@Composable
private fun PhotoResultContent(
    photos: List<Uri>,
    frame: PhotoFrame,
    containerWidth: Dp,
    containerHeight: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // ✅ Usa el nuevo PhotoFrameOverlay en lugar de mostrar frame + fotos separados
        PhotoFrameOverlay(
            photos = photos,
            frame = frame,
            containerWidth = containerWidth,
            containerHeight = containerHeight
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ===== ESTAS FUNCIONES YA NO SE USAN PERO LAS DEJO POR SI ACASO =====

@Composable
private fun PhotosLayout(
    photos: List<Uri>,
    slots: Int,
    containerWidth: Dp,
    photoPadding: Dp
) {
    when (slots) {
        1 -> SinglePhotoLayout(photos = photos, containerWidth = containerWidth, photoPadding = photoPadding)
        2 -> TwoPhotosLayout(photos = photos, containerWidth = containerWidth, photoPadding = photoPadding)
        3 -> ThreePhotosLayout(photos = photos, containerWidth = containerWidth, photoPadding = photoPadding)
        4 -> FourPhotosLayout(photos = photos, containerWidth = containerWidth, photoPadding = photoPadding)
    }
}

@Composable
private fun SinglePhotoLayout(
    photos: List<Uri>,
    containerWidth: Dp,
    photoPadding: Dp
) {
    photos.firstOrNull()?.let { uri ->
        AsyncImage(
            model = uri,
            contentDescription = null,
            modifier = Modifier
                .width(containerWidth)
                .aspectRatio(1f)
                .padding(photoPadding),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun TwoPhotosLayout(
    photos: List<Uri>,
    containerWidth: Dp,
    photoPadding: Dp
) {
    Row(
        modifier = Modifier
            .width(containerWidth)
            .aspectRatio(2f / 1f)
            .padding(photoPadding)
    ) {
        photos.take(2).forEach { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun ThreePhotosLayout(
    photos: List<Uri>,
    containerWidth: Dp,
    photoPadding: Dp
) {
    Column(
        modifier = Modifier
            .width(containerWidth)
            .padding(photoPadding)
    ) {
        photos.take(3).forEach { uri ->
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(containerWidth / 3)
                    .padding(4.dp),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
private fun FourPhotosLayout(
    photos: List<Uri>,
    containerWidth: Dp,
    photoPadding: Dp
) {
    Column(
        modifier = Modifier
            .width(containerWidth)
            .aspectRatio(1f)
            .padding(photoPadding)
    ) {
        for (row in 0 until 2) {
            Row(modifier = Modifier.weight(1f)) {
                photos.drop(row * 2).take(2).forEach { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

// ===== PREVIEWS ACTUALIZADOS =====

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, heightDp = 800, name = "Compact - 4 Photos")
@Composable
fun PhotoResultCompactPreview() {
    val mockFrame = PhotoFrame(
        id = 1,
        name = "Marco cuadrado",
        slots = 4,
        drawableRes = R.drawable.ic_launcher_foreground,
        photoSlots = listOf(
            PhotoSlot(x = 0.05f, y = 0.05f, width = 0.4f, height = 0.4f),  // Arriba izquierda
            PhotoSlot(x = 0.55f, y = 0.05f, width = 0.4f, height = 0.4f),  // Arriba derecha
            PhotoSlot(x = 0.05f, y = 0.55f, width = 0.4f, height = 0.4f),  // Abajo izquierda
            PhotoSlot(x = 0.55f, y = 0.55f, width = 0.4f, height = 0.4f)   // Abajo derecha
        )
    )
    val mockPhotos = (1..4).map { "https://picsum.photos/300/300?random=$it".toUri() }

    PhotoResultScreen(
        photos = mockPhotos,
        frame = mockFrame,
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(400.dp, 800.dp)),
        onRetake = { },
        onSave = { }
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 600, heightDp = 800, name = "Medium - 3 Photos")
@Composable
fun PhotoResultMediumPreview() {
    val mockFrame = PhotoFrame(
        id = 1,
        name = "Ristra de 3",
        slots = 3,
        drawableRes = R.drawable.ic_launcher_foreground,
        photoSlots = listOf(
            PhotoSlot(x = 0.075f, y = 0.05f, width = 0.85f, height = 0.28f),  // Foto 1 (arriba)
            PhotoSlot(x = 0.075f, y = 0.36f, width = 0.85f, height = 0.28f),  // Foto 2 (centro)
            PhotoSlot(x = 0.075f, y = 0.67f, width = 0.85f, height = 0.28f)   // Foto 3 (abajo)
        )
    )
    val mockPhotos = (1..3).map { "https://picsum.photos/300/300?random=${it+10}".toUri() }

    PhotoResultScreen(
        photos = mockPhotos,
        frame = mockFrame,
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(600.dp, 800.dp)),
        onRetake = { },
        onSave = { }
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 900, heightDp = 800, name = "Expanded - 2 Photos")
@Composable
fun PhotoResultExpandedPreview() {
    val mockFrame = PhotoFrame(
        id = 2,
        name = "Marco doble",
        slots = 2,
        drawableRes = R.drawable.ic_launcher_foreground,
        photoSlots = listOf(
            PhotoSlot(x = 0.05f, y = 0.1f, width = 0.4f, height = 0.8f),   // Foto izquierda
            PhotoSlot(x = 0.55f, y = 0.1f, width = 0.4f, height = 0.8f)    // Foto derecha
        )
    )
    val mockPhotos = (1..2).map { "https://picsum.photos/300/300?random=${it+20}".toUri() }

    PhotoResultScreen(
        photos = mockPhotos,
        frame = mockFrame,
        windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(900.dp, 800.dp)),
        onRetake = { },
        onSave = { }
    )
}

// Preview para testear el PhotoFrameOverlay
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, name = "Frame Overlay Test")
@Composable
fun PhotoFrameOverlayPreview() {
    val mockFrame = PhotoFrame(
        id = 1,
        name = "Ristra de 3",
        slots = 3,
        drawableRes = R.drawable.ic_launcher_foreground,
        photoSlots = listOf(
            PhotoSlot(x = 0.075f, y = 0.05f, width = 0.85f, height = 0.28f),
            PhotoSlot(x = 0.075f, y = 0.36f, width = 0.85f, height = 0.28f),
            PhotoSlot(x = 0.075f, y = 0.67f, width = 0.85f, height = 0.28f)
        )
    )
    val mockPhotos = (1..3).map { "https://picsum.photos/400/600?random=$it".toUri() }

    Column {
        Text("Frame: ${mockFrame.name}")
        PhotoFrameOverlay(
            photos = mockPhotos,
            frame = mockFrame,
            containerWidth = 300.dp,
            containerHeight = 450.dp
        )
    }
}