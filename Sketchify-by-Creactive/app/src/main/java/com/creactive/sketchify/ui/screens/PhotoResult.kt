package com.creactive.sketchify.ui.screens

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.creactive.sketchify.data.PhotoFrame
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoResultScreen(
    photos: List<Uri>,
    frame: PhotoFrame,
    onRetake: () -> Unit,
    onSave: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Resultados") }) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = onRetake) { Text("Repetir") }

                Button(onClick = {
                    onSave()
                    Toast.makeText(context, "Fotos guardadas en galería", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Guardar")
                }
            }
        }
    ) { padding ->
        // 👇 aquí mantienes toda tu lógica para mostrar fotos según slots
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = frame.drawableRes),
                contentDescription = "Guía del frame",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (frame.slots) {
                1 -> photos.firstOrNull()?.let { uri ->
                    AsyncImage(
                        model = uri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .padding(8.dp),
                        contentScale = ContentScale.Crop
                    )
                }
                2 -> Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(2f / 1f)
                        .padding(8.dp)
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
                3 -> Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    photos.take(3).forEach { uri ->
                        AsyncImage(
                            model = uri,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .padding(4.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                4 -> Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(8.dp)
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
        }
    }
}

// Función auxiliar para guardar fotos en galería
fun savePhotosToGallery(context: Context, uris: List<Uri>) {
    uris.forEachIndexed { index, uri ->
        try {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "photo_${System.currentTimeMillis()}_$index.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Sketchify")
            }

            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                resolver.openOutputStream(imageUri).use { outputStream: OutputStream? ->
                    val inputStream = resolver.openInputStream(uri) ?: FileInputStream(File(uri.path!!))
                    inputStream.copyTo(outputStream!!)
                    inputStream.close()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}