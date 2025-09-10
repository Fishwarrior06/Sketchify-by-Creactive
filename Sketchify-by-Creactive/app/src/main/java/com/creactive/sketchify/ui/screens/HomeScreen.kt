package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔝 Mensaje superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {}
            Text(
                text = "Placeholder",
                style = MaterialTheme.typography.headlineMedium
            )

            // 📦 Espacio para imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(550.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text("Aquí irá la imagen")
            }

            // 🔘 Botones inferiores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = { /* TODO: lógica subir foto */ },
                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                ) {
                    Text("Subir foto")
                }
                Button(
                    onClick = { navController.navigate("PhotoType") }, // Aquí navega
                    modifier = Modifier.weight(1f).padding(start = 8.dp)
                ) {
                    Text("Tomar foto")
                }
            }
        }
    }
}