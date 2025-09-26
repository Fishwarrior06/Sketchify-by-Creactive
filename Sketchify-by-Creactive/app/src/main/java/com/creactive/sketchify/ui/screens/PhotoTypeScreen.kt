package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
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
        WindowWidthSizeClass.Compact -> 315.dp
        WindowWidthSizeClass.Medium -> 310.dp
        WindowWidthSizeClass.Expanded -> 350.dp
        else -> 310.dp
    }

    val spacing = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 12.dp
        WindowWidthSizeClass.Medium -> 16.dp
        WindowWidthSizeClass.Expanded -> 20.dp
        else -> 16.dp
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) { Text("Regresar") }

            Spacer(modifier = Modifier.height(spacing))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val rows = 2
                val cols = 2
                for (row in 0 until rows) {
                    Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                        for (col in 0 until cols) {
                            val index = row * cols + col
                            val frame = frames.getOrNull(index)

                            Box(
                                modifier = Modifier
                                    .width(boxWidth)
                                    .height(boxHeight)
                                    .background(
                                        if (selectedIndex == index) Color.Green else Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedIndex = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.padding(8.dp)
                                ) {
                                    // Imagen dinámica según cada frame
                                    frame?.drawableRes?.let { res ->
                                        Image(
                                            painter = painterResource(id = frame.drawableRes),
                                            contentDescription = frame.name,
                                            modifier = Modifier
                                                .size(250.dp)
                                                .padding(bottom = 8.dp),
                                            contentScale = ContentScale.Fit
                                        )
                                    }

                                    // Texto del frame
                                    Text(frame?.name ?: "Box ${index + 1}")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing))
            Button(
                onClick = {
                    val selectedFrame = frames.getOrNull(selectedIndex)
                    if (selectedFrame != null) {
                        // ✅ Pasar el objeto completo
                        navController.currentBackStackEntry?.savedStateHandle?.set("selectedFrame", selectedFrame)
                        navController.currentBackStackEntry?.savedStateHandle?.set("modo", modo)

                        if (modo == "camara") {
                            navController.navigate("PhotoBooth")
                        } else {
                            navController.navigate("OtroModoScreen")
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) { Text("Seleccionar marco") }
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