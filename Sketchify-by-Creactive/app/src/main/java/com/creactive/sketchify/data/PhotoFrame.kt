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
            PhotoSlot(x = 0.065f, y = 0.109f, width = 0.537f, height = 0.428f)     // Foto centrada
        )
    ),

    // Frame de 2 fotos horizontales
    PhotoFrame(
        id = 2,
        name = "Marco doble",
        slots = 2,
        drawableRes = R.drawable.frame2,
        photoSlots = listOf(
            PhotoSlot(x = 0.143f, y = 0.209f, width = 0.413f, height = 0.146f),   // Foto izquierda
            PhotoSlot(x = 0.143f, y = 0.362f, width = 0.413f, height = 0.145f)    // Foto derecha
        )
    ),

    // Frame de 3 fotos verticales (como tu imagen)
    PhotoFrame(
        id = 3,
        name = "Ristra de 3",
        slots = 3,
        drawableRes = R.drawable.frame3,
        photoSlots = listOf(
            PhotoSlot(x = 0.0811f, y = 0.082f, width = 0.50f, height = 0.165f),  // Foto 1 (arriba)
            PhotoSlot(x = 0.081f, y = 0.251f, width = 0.50f, height = 0.159f),  // Foto 2 (centro)
            PhotoSlot(x = 0.081f, y = 0.414f, width = 0.50f, height = 0.159f)   // Foto 3 (abajo)
        )
    ),

    // Frame de 4 fotos (cuadrícula 2x2)
    PhotoFrame(
        id = 4,
        name = "Marco cuadrado",
        slots = 4,
        drawableRes = R.drawable.frame4,
        photoSlots = listOf(
            PhotoSlot(x = 0.0423f, y = 0.174f, width = 0.275f, height = 0.160f),  // Arriba izquierda
            PhotoSlot(x = 0.34f, y = 0.174f, width = 0.275f, height = 0.160f),  // Arriba derecha
            PhotoSlot(x = 0.0423f, y = 0.349f, width = 0.275f, height = 0.160f),  // Abajo izquierda
            PhotoSlot(x = 0.34f, y = 0.349f, width = 0.275f, height = 0.160f)   // Abajo derecha
        )
    )
)