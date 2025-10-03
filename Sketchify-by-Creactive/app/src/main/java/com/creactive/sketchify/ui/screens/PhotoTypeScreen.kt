package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.creactive.sketchify.R
import com.creactive.sketchify.data.PhotoFrame
import com.creactive.sketchify.data.frames

@Composable
fun PhotoTypeScreen(navController: NavController, modo: String, windowSizeClass: WindowSizeClass) {
    var selectedIndex by remember { mutableIntStateOf(-1) }

    val boxWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 175.dp
        WindowWidthSizeClass.Medium -> 175.dp
        WindowWidthSizeClass.Expanded -> 200.dp
        else -> 175.dp
    }

    val boxHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 310.dp
        WindowWidthSizeClass.Medium -> 310.dp
        WindowWidthSizeClass.Expanded -> 350.dp
        else -> 310.dp
    }

    val btnHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 50.dp
        WindowWidthSizeClass.Medium -> 80.dp
        WindowWidthSizeClass.Expanded -> 110.dp
        else -> 50.dp
    }

    val btnWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 60.dp
        WindowWidthSizeClass.Medium -> 90.dp
        WindowWidthSizeClass.Expanded -> 120.dp
        else -> 60.dp
    }

    val framebtnHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 65.dp
        WindowWidthSizeClass.Medium -> 130.dp
        WindowWidthSizeClass.Expanded -> 170.dp
        else -> 100.dp
    }

    val framebtnWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 250.dp
        WindowWidthSizeClass.Medium -> 280.dp
        WindowWidthSizeClass.Expanded -> 320.dp
        else -> 250.dp
    }

    val spacing = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 10.dp
        WindowWidthSizeClass.Medium -> 14.dp
        WindowWidthSizeClass.Expanded -> 16.dp
        else -> 8.dp
    }

    val frameHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 225.dp
        WindowWidthSizeClass.Medium -> 285.dp
        WindowWidthSizeClass.Expanded -> 385.dp
        else -> 225.dp
    }

    val imgWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 500.dp
        WindowWidthSizeClass.Medium -> 700.dp
        WindowWidthSizeClass.Expanded -> 900.dp
        else -> 500.dp
    }

    val imgHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 400.dp
        WindowWidthSizeClass.Medium -> 600.dp
        WindowWidthSizeClass.Expanded -> 800.dp
        else -> 400.dp
    }

    val navigateToPhotoBooth = { selectedFrame: PhotoFrame ->
        // Construimos la nueva ruta con el modo y el nombre del frame
        navController.navigate("PhotoBooth/${modo}/${selectedFrame.name}")
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 🖼️ IMAGEN DE FONDO
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                val interactionSource1 = remember { MutableInteractionSource() }
                val isPressed1 by interactionSource1.collectIsPressedAsState()

                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .scale(if (isPressed1) 0.95f else 1f)
                        .alpha(if (isPressed1) 0.8f else 1f),
                    interactionSource = interactionSource1,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    border = null,
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.home_button),
                        contentDescription = "Home_button",
                        modifier = Modifier
                            .width(btnWidth)
                            .height(btnHeight)
                    )
                }

                Spacer(modifier = Modifier.height(spacing))

                // 🎯 BOXES INDIVIDUALES CON NAVEGACIÓN
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        modifier = Modifier.width(375.dp)
                            .height(310.dp),
                    ) {
                        // BOX 1
                        // Ejemplo completo para el BOX 1
                        FrameBox(
                            frame = frames.getOrNull(0),
                            isSelected = selectedIndex == 0,
                            boxWidth = boxWidth,
                            boxHeight = boxHeight,
                            frameHeight = frameHeight,
                            imgWidth = imgWidth,
                            imgHeight = imgHeight,
                            onBoxClick = {
                                selectedIndex = 0
                                frames.getOrNull(0)?.let { selectedFrame ->
                                    navigateToPhotoBooth(selectedFrame)
                                }
                            }
                        )

                        // BOX 2
                        FrameBox(
                            frame = frames.getOrNull(1),
                            isSelected = selectedIndex == 1,
                            boxWidth = boxWidth,
                            boxHeight = boxHeight,
                            frameHeight = frameHeight,
                            imgWidth = imgWidth,
                            imgHeight = imgHeight,
                            onBoxClick = {
                                selectedIndex = 1
                                frames.getOrNull(1)?.let { selectedFrame ->
                                    navigateToPhotoBooth(selectedFrame)
                                }
                            }
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing),
                        modifier = Modifier.width(375.dp)
                            .height(310.dp),
                        ) {
                        // BOX 3
                        FrameBox(
                            frame = frames.getOrNull(2),
                            isSelected = selectedIndex == 2,
                            boxWidth = boxWidth,
                            boxHeight = boxHeight,
                            frameHeight = frameHeight,
                            imgWidth = imgWidth,
                            imgHeight = imgHeight,
                            onBoxClick = {
                                selectedIndex = 2
                                frames.getOrNull(2)?.let { selectedFrame ->
                                    navigateToPhotoBooth(selectedFrame)
                                }
                            }
                        )

                        // BOX 4
                        FrameBox(
                            frame = frames.getOrNull(3),
                            isSelected = selectedIndex == 3,
                            boxWidth = boxWidth,
                            boxHeight = boxHeight,
                            frameHeight = frameHeight,
                            imgWidth = imgWidth,
                            imgHeight = imgHeight,
                            onBoxClick = {
                                selectedIndex = 3
                                frames.getOrNull(3)?.let { selectedFrame ->
                                    navigateToPhotoBooth(selectedFrame)
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing))

                // 🖼️ SOLO LA IMAGEN DEL BOTÓN (SIN FUNCIONALIDAD)
                Image(
                    painter = painterResource(id = R.drawable.frame_button),
                    contentDescription = "Frame_button",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(framebtnWidth)
                        .height(framebtnHeight)
                )
            }
        }
    }
}

@Composable
fun FrameBox(
    frame: PhotoFrame?, // ✅ Tipo específico en lugar de Any?
    isSelected: Boolean,
    boxWidth: Dp,
    boxHeight: Dp,
    onBoxClick: () -> Unit,
    frameHeight: Dp?,
    imgWidth: Dp?,
    imgHeight: Dp?
){
    FrameBox(frame,
    isSelected,
    boxWidth,
    boxHeight,
    onBoxClick,
    frameHeight,
        imgWidth,
        imgHeight
    )
}

// 📦 COMPONENTE INDIVIDUAL PARA CADA BOX (CORREGIDO)
@Composable
fun FrameBox(
    frame: PhotoFrame?, // ✅ Tipo específico en lugar de Any?
    isSelected: Boolean,
    boxWidth: Dp,
    boxHeight: Dp,
    onBoxClick: () -> Unit,
    frameHeight: Dp,
    imgWidth: Dp,
    imgHeight: Dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    Box(
        modifier = Modifier
            .width(boxWidth)
            .height(boxHeight)
            .padding(4.dp)
            .scale(if (isPressed) 0.95f else 1f)
            .alpha(if (isPressed) 0.8f else 1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onBoxClick() },
        contentAlignment = Alignment.Center
    ) {
        // 🖼️ Imagen como fondo
        Image(
            painter = painterResource(id = R.drawable.frameboxbg),
            contentDescription = "FrameBoxBG",
            modifier = Modifier
                .width(imgWidth)
                .height(imgHeight),
            contentScale = ContentScale.FillBounds
        )

        // ✅ Indicador de selección
        if (isSelected) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(4.dp)
                    )
            )
        }

        // 📷 Contenido del frame (CORREGIDO)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            frame?.let { frameData -> // ✅ Safe call
                Image(
                    painter = painterResource(id = frameData.drawableRes), // ✅ Acceso correcto
                    contentDescription = frameData.name, // ✅ Acceso correcto
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .height(frameHeight),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PhotoTypeScreenPreviewCompact() {
    PhotoTypeScreen(
        navController = rememberNavController(),
        modo = "camara",
        windowSizeClass = WindowSizeClass.calculateFromSize(
            DpSize(400.dp, 800.dp)
        )
    )
}