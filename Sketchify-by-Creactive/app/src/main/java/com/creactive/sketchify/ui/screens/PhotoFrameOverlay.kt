package com.creactive.sketchify.ui.screens

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import com.creactive.sketchify.data.PhotoFrame
import dev.shreyaspatil.capturable.capturable
import dev.shreyaspatil.capturable.controller.CaptureController

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PhotoFrameOverlay(
    photos: List<Uri>,
    frame: PhotoFrame,
    containerWidth: Dp,
    containerHeight: Dp,
    modifier: Modifier = Modifier,
    captureController: CaptureController? = null // ✅ NUEVO parámetro opcional
) {
    Box(
        modifier = modifier
            .size(containerWidth, containerHeight)
            .let { mod ->
                // ✅ Solo aplicar capturable si se proporciona el controller
                if (captureController != null) {
                    mod.capturable(captureController)
                } else {
                    mod
                }
            }
    ) {
        // Frame de fondo
        Image(
            painter = painterResource(id = frame.drawableRes),
            contentDescription = "Frame",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // ✅ Fotos usando PhotoSlot directamente (igual que antes)
        photos.take(frame.slots).forEachIndexed { index, uri ->
            val slot = frame.photoSlots.getOrNull(index)
            if (slot != null) {
                AsyncImage(
                    model = uri,
                    contentDescription = "Photo $index",
                    modifier = Modifier
                        .size(
                            width = containerWidth * slot.width,
                            height = containerHeight * slot.height
                        )
                        .offset(
                            x = containerWidth * slot.x,
                            y = containerHeight * slot.y
                        )
                        .clip(RectangleShape),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}