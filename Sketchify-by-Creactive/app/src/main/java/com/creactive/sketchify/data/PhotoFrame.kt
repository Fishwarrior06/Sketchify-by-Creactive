package com.creactive.sketchify.data
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.creactive.sketchify.R

@Parcelize
data class PhotoFrame (
    val id: Int,
    val name: String,
    val slots: Int,
    val drawableRes: Int
) : Parcelable


val frames = listOf(
    PhotoFrame(1, "Rista de 3", 3, R.drawable.frame1),
    PhotoFrame(2, "Marco doble", 2, R.drawable.frame2),
    PhotoFrame(3, "Marco simple", 1, R.drawable.frame2),
    PhotoFrame(4, "Marco cuadrado", 4, R.drawable.frame1)
)