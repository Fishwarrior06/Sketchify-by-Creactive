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
    // Frame de 1 foto
    PhotoFrame(
        id = 1,
        name = "Marco simple",
        slots = 1,
        drawableRes = R.drawable.frame1,
        photoSlots = listOf(
            PhotoSlot(x = 0.065f, y = 0.12f, width = 0.537f, height = 0.475f)     // Foto centrada
        )
    ),

    // Frame de 2 fotos horizontales
    PhotoFrame(
        id = 2,
        name = "Marco doble",
        slots = 2,
        drawableRes = R.drawable.frame2,
        photoSlots = listOf(
            PhotoSlot(x = 0.143f, y = 0.231f, width = 0.413f, height = 0.162f),   // Foto izquierda
            PhotoSlot(x = 0.143f, y = 0.400f, width = 0.413f, height = 0.162f)    // Foto derecha
        )
    ),

    // Frame de 3 fotos verticales (como tu imagen)
    PhotoFrame(
        id = 3,
        name = "Ristra de 3",
        slots = 3,
        drawableRes = R.drawable.frame3,
        photoSlots = listOf(
            PhotoSlot(x = 0.081f, y = 0.092f, width = 0.50f, height = 0.18f),  // Foto 1 (arriba)
            PhotoSlot(x = 0.081f, y = 0.277f, width = 0.50f, height = 0.176f),  // Foto 2 (centro)
            PhotoSlot(x = 0.081f, y = 0.457f, width = 0.50f, height = 0.177f)   // Foto 3 (abajo)
        )
    ),

    // Frame de 4 fotos (cuadrícula 2x2)
    PhotoFrame(
        id = 4,
        name = "Marco cuadrado",
        slots = 4,
        drawableRes = R.drawable.frame4,
        photoSlots = listOf(
            PhotoSlot(x = 0.0423f, y = 0.192f, width = 0.275f, height = 0.177f),  // Arriba izquierda
            PhotoSlot(x = 0.34f, y = 0.192f, width = 0.275f, height = 0.177f),  // Arriba derecha
            PhotoSlot(x = 0.0423f, y = 0.386f, width = 0.275f, height = 0.177f),  // Abajo izquierda
            PhotoSlot(x = 0.34f, y = 0.386f, width = 0.275f, height = 0.177f)   // Abajo derecha
        )
    )
)