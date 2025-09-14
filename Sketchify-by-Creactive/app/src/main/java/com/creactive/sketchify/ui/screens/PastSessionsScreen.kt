package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PastSessionsScreen(navController: NavController) {
    // Lista de sesiones (podrías cargar de BD, por ahora dummy)
    val sesiones = remember {
        mutableStateListOf(
            "Sesión 1 - 12/09/2025",
            "Sesión 2 - 13/09/2025",
            "Sesión 3 - 14/09/2025"
        )
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // 🔙 Botón para volver
            Button(onClick = { navController.popBackStack() }) {
                Text("Regresar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Sesiones pasadas",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 📜 Lista de sesiones
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sesiones) { sesion ->
                    SesionItem(sesion)
                }
            }
        }
    }
}

@Composable
fun SesionItem(nombre: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
            .padding(16.dp)
            .clickable { expanded = !expanded }
    ) {
        Text(nombre, style = MaterialTheme.typography.bodyLarge)

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text("📸 Fotos de esta sesión aparecerán aquí...")
        }
    }
}
