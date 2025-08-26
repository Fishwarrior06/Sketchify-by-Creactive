package com.creactive.sketchify.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.creactive.sketchify.data.ProfileService

//ESTO POR EL MOMENTO ES UN PLACEHOLDER

@Composable
fun ProfileScreen(userId: Int) {
    val profile = remember { ProfileService.getProfileByUserId(userId) }

    if (profile != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔹 Espacio reservado para el PFP
            Box(
                modifier = Modifier
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Placeholder Image") // 👈 Aquí irá la imagen en el futuro
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = profile.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = "@${profile.username}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = profile.description ?: "Sin descripción",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Perfil no encontrado")
        }
    }
}