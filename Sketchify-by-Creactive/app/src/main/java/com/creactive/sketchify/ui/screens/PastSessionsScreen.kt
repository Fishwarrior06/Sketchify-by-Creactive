package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun PastSessionsScreen(navController: NavController, windowSizeClass: WindowSizeClass) {
    // Lista de sesiones (dummy)
    val sesiones = remember {
        mutableStateListOf(
            "Sesión 1 - 12/09/2025",
            "Sesión 2 - 13/09/2025",
            "Sesión 3 - 14/09/2025"
        )
    }

    // Ajustes dinámicos según widthSizeClass
    val spacing = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 8.dp
        WindowWidthSizeClass.Medium -> 12.dp
        WindowWidthSizeClass.Expanded -> 16.dp
        else -> 12.dp
    }

    val itemPadding = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 12.dp
        WindowWidthSizeClass.Medium -> 16.dp
        WindowWidthSizeClass.Expanded -> 20.dp
        else -> 16.dp
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // 🔙 Botón regresar
            Button(onClick = { navController.popBackStack() }) {
                Text("Regresar")
            }

            Spacer(modifier = Modifier.height(spacing))

            Text(
                "Sesiones pasadas",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(spacing))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                items(sesiones) { sesion ->
                    SesionItem(sesion, itemPadding)
                }
            }
        }
    }
}

@Composable
fun SesionItem(nombre: String, padding: Dp) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray, shape = MaterialTheme.shapes.medium)
            .padding(padding)
            .clickable { expanded = !expanded }
    ) {
        Text(nombre, style = MaterialTheme.typography.bodyLarge)

        if (expanded) {
            Spacer(modifier = Modifier.height(padding))
            Text("📸 Fotos de esta sesión aparecerán aquí...")
        }
    }
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun PastSessionsScreenPreviewCompact() {
    PastSessionsScreen(
        navController = rememberNavController(),
        windowSizeClass = WindowSizeClass.calculateFromSize(androidx.compose.ui.unit.DpSize(400.dp, 800.dp))
    )
}