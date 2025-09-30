package com.creactive.sketchify.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.creactive.sketchify.R

@Parcelize
data class PhotoSlot(
    val x: Float,        // Posición X como porcentaje (0.0 - 1.0)
    val y: Float,        // Posición Y como porcentaje (0.0 - 1.0)
    val width: Float,    // Ancho como porcentaje (0.0 - 1.0)
    val height: Float    // Alto como porcentaje (0.0 - 1.0)
) : Parcelable

@Parcelize
data class PhotoFrame(
    val id: Int,
    val name: String,
    val slots: Int,
    val drawableRes: Int,
    val photoSlots: List<PhotoSlot>  // ✅ Posiciones de cada recuadro
) : Parcelable

val frames = listOf(
    // Frame de 3 fotos verticales (como tu imagen)
    PhotoFrame(
        id = 1,
        name = "Ristra de 3",
        slots = 3,
        drawableRes = R.drawable.frame1,
        photoSlots = listOf(
            PhotoSlot(x = 0.082f, y = 0.022f, width = 0.50f, height = 0.10f),  // Foto 1 (arriba)
            PhotoSlot(x = 0.082f, y = 0.29f, width = 0.53f, height = 0.24f),  // Foto 2 (centro)
            PhotoSlot(x = 0.082f, y = 0.56f, width = 0.53f, height = 0.25f)   // Foto 3 (abajo)
        )
    ),

    // Frame de 2 fotos horizontales
    PhotoFrame(
        id = 2,
        name = "Marco doble",
        slots = 2,
        drawableRes = R.drawable.frame2,
        photoSlots = listOf(
            PhotoSlot(x = 0.05f, y = 0.1f, width = 0.4f, height = 0.8f),   // Foto izquierda
            PhotoSlot(x = 0.55f, y = 0.1f, width = 0.4f, height = 0.8f)    // Foto derecha
        )
    ),

    // Frame de 1 foto
    PhotoFrame(
        id = 3,
        name = "Marco simple",
        slots = 1,
        drawableRes = R.drawable.frame3,
        photoSlots = listOf(
            PhotoSlot(x = 0.1f, y = 0.1f, width = 0.8f, height = 0.8f)     // Foto centrada
        )
    ),

    // Frame de 4 fotos (cuadrícula 2x2)
    PhotoFrame(
        id = 4,
        name = "Marco cuadrado",
        slots = 4,
        drawableRes = R.drawable.frame4,
        photoSlots = listOf(
            PhotoSlot(x = 0.05f, y = 0.05f, width = 0.4f, height = 0.4f),  // Arriba izquierda
            PhotoSlot(x = 0.55f, y = 0.05f, width = 0.4f, height = 0.4f),  // Arriba derecha
            PhotoSlot(x = 0.05f, y = 0.55f, width = 0.4f, height = 0.4f),  // Abajo izquierda
            PhotoSlot(x = 0.55f, y = 0.55f, width = 0.4f, height = 0.4f)   // Abajo derecha
        )
    )
)