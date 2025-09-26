package com.creactive.sketchify.modules

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Rect
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.creactive.sketchify.data.PhotoFrame
import androidx.core.graphics.createBitmap
import java.io.IOException

class PhotoResultController(
    private val context: Context
) {
    companion object {
        private const val TAG = "PhotoResultController"
    }

    // ✅ NUEVA FUNCIÓN SIMPLE: Screenshot directo de la View
    fun captureFrameFromView(
        frameView: View,
        onComplete: (Boolean) -> Unit
    ) {
        try {
            val bitmap = createBitmap(frameView.width, frameView.height)

            val canvas = Canvas(bitmap)
            frameView.draw(canvas) // Screenshot directo

            val success = saveFrameToGallery(bitmap)
            Toast.makeText(
                context,
                if (success) "Marco guardado!" else "Error al guardar",
                Toast.LENGTH_SHORT
            ).show()

            onComplete(success)
        } catch (e: Exception) {
            Log.e(TAG, "Error capturing frame view", e)
            onComplete(false)
        }
    }

    // ✅ Mantener función existente por compatibilidad
    fun saveCapturedBitmap(bitmap: Bitmap, onComplete: (Boolean) -> Unit) {
        try {
            val success = saveFrameToGallery(bitmap)
            Toast.makeText(
                context,
                if (success) "Marco guardado!" else "Error al guardar",
                Toast.LENGTH_SHORT
            ).show()
            onComplete(success)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving bitmap", e)
            onComplete(false)
        }
    }

    // ✅ Mantener por si la necesitas (mejorar dimensiones por defecto)
    fun captureFrameManually(
        photos: List<Uri>,
        frame: PhotoFrame,
        width: Int = 1080, // ✅ Mejores dimensiones por defecto
        height: Int = 1080,
        onComplete: (Boolean) -> Unit
    ) {
        try {
            val bitmap = createManualBitmap(photos, frame, width, height)
            val success = saveFrameToGallery(bitmap)

            Toast.makeText(
                context,
                if (success) "Marco guardado!" else "Error al guardar",
                Toast.LENGTH_SHORT
            ).show()
            onComplete(success)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating frame", e)
            onComplete(false)
        }
    }

    private fun createManualBitmap(
        photos: List<Uri>,
        frame: PhotoFrame,
        width: Int,
        height: Int
    ): Bitmap {
        val bitmap = createBitmap(width, height)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        // Frame
        try {
            val frameDrawable = ContextCompat.getDrawable(context, frame.drawableRes)
            frameDrawable?.let {
                it.setBounds(0, 0, width, height)
                it.draw(canvas)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error drawing frame", e)
        }

        // Fotos usando PhotoSlot directamente
        photos.take(frame.slots).forEachIndexed { index, uri ->
            val slot = frame.photoSlots.getOrNull(index)
            if (slot != null) {
                try {
                    val photoBitmap = loadBitmapFromUriWithCorrectOrientation(uri)
                    if (photoBitmap != null) {
                        val left = (width * slot.x).toInt()
                        val top = (height * slot.y).toInt()
                        val photoWidth = (width * slot.width).toInt()
                        val photoHeight = (height * slot.height).toInt()

                        val destRect = Rect(left, top, left + photoWidth, top + photoHeight)
                        val srcRect = Rect(0, 0, photoBitmap.width, photoBitmap.height)

                        canvas.drawBitmap(photoBitmap, srcRect, destRect, null)
                        photoBitmap.recycle()
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error drawing photo $index", e)
                }
            }
        }
        return bitmap
    }

    private fun loadBitmapFromUriWithCorrectOrientation(uri: Uri): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            bitmap?.let { correctBitmapOrientation(uri, it) }
        } catch (e: Exception) {
            Log.e(TAG, "Error loading bitmap", e)
            null
        }
    }

    private fun correctBitmapOrientation(uri: Uri, bitmap: Bitmap): Bitmap {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val exif = ExifInterface(inputStream!!)
            inputStream.close()

            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            val matrix = Matrix()

            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
                ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.preScale(-1f, 1f)
                ExifInterface.ORIENTATION_FLIP_VERTICAL -> matrix.preScale(1f, -1f)
                else -> return bitmap
            }

            val rotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            if (rotated != bitmap) bitmap.recycle()
            rotated
        } catch (e: IOException) {
            Log.e(TAG, "Error reading EXIF", e)
            bitmap
        }
    }

    fun saveFrameToGallery(bitmap: Bitmap): Boolean {
        return try {
            val resolver = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "sketchify_frame_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Sketchify")
            }

            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                resolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
                }
                true
            } else false
        } catch (e: Exception) {
            Log.e(TAG, "Error saving frame", e)
            false
        }
    }

    fun showSaveSuccessMessage() {
        Toast.makeText(context, "Fotos guardadas en galería", Toast.LENGTH_SHORT).show()
    }
}