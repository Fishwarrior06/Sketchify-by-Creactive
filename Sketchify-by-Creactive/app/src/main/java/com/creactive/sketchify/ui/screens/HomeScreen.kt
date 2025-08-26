package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.creactive.sketchify.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    user: User
) {
    //Barra superior de la app.
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        //Te muestra tu nombre y el nombre de la app como titulo
                        Text(text = "Hola ${user.name}!", style = MaterialTheme.typography.bodyLarge)
                        Text(text = "Sketchify", style = MaterialTheme.typography.titleMedium)
                    }
                }
            )
        },
        bottomBar = {
            // Footer donde van los botones de navegacion
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .background(Color.LightGray),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Aquí pondremos los botones/tabs para navegar entre screens
                Text("Screen 1")
                Text("Screen 2")
                Text("Screen 3")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Titulo de bienvenida en la homescreen (todo este ultimo bloque de codigo es placeholder)
            Text(
                text = "Bienvenido a Sketchify 👋",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(20.dp))
            //La homescreen te muestra tu user y tu correo y tu rol para ver si el json loader funciona
            Text("Tu usuario: ${user.username}")
            Text("Tu correo: ${user.email}")
            Text("Rol: ${user.role}")
        }
    }
}