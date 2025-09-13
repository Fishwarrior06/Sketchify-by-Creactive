package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PhotoTypeScreen(navController: NavController, modo: String) {
    var selectedBox by remember { mutableStateOf(-1) } // Para saber qué box seleccionaste

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

            Spacer(modifier = Modifier.height(24.dp))

            // 📦 Grid de 4 boxes
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (row in 0 until 2) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        for (col in 0 until 2) {
                            val index = row * 2 + col
                            Box(
                                modifier = Modifier
                                    .width(175.dp)
                                    .height(310.dp)
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

            Spacer(modifier = Modifier.height(24.dp))

            // Botón seleccionar marco
            Button(
                onClick = {
                    if (selectedBox != -1) {
                        // Guardamos la selección en el previousBackStackEntry
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("selectedBox", selectedBox)
                        navController.previousBackStackEntry
                            ?.savedStateHandle
                            ?.set("modo", modo)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Seleccionar marco")
            }
        }
    }
}