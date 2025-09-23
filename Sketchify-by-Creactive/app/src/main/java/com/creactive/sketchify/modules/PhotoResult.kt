package com.creactive.sketchify.modules

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import com.creactive.sketchify.data.PhotoFrame
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

class PhotoResultController(
    private val context: Context
) {

    fun savePhotosToGallery(uris: List<Uri>) {
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

    fun showSaveSuccessMessage() {
        Toast.makeText(context, "Fotos guardadas en galería", Toast.LENGTH_SHORT).show()
    }

    fun handleSave(photos: List<Uri>, onComplete: () -> Unit = {}) {
        savePhotosToGallery(photos)
        showSaveSuccessMessage()
        onComplete()
    }
}