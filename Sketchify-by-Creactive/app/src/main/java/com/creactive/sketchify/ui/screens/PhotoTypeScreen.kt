package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PhotoTypeScreen(navController: NavController, modo: String, windowSizeClass: WindowSizeClass) {
    var selectedBox by remember { mutableIntStateOf(-1) }

    // Ajustes dinámicos según tamaño de pantalla
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
            // 🔙 Botón regresar
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Text("Regresar")
            }

            Spacer(modifier = Modifier.height(spacing))

            // 📦 Grid de 4 boxes
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(spacing),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (row in 0 until 2) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing)
                    ) {
                        for (col in 0 until 2) {
                            val index = row * 2 + col
                            Box(
                                modifier = Modifier
                                    .width(boxWidth)
                                    .height(boxHeight)
                                    .background(
                                        if (selectedBox == index) Color.Green else Color.LightGray,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { selectedBox = index },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Box ${index + 1}")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(spacing))

            // Botón seleccionar marco
            Button(
                onClick = {
                    if (selectedBox != -1) {
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedBox", selectedBox)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("modo", modo)
                        navController.navigate("PhotoBooth")
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Seleccionar marco")
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
        windowSizeClass = WindowSizeClass.calculateFromSize(androidx.compose.ui.unit.DpSize(400.dp, 800.dp))
    )
}