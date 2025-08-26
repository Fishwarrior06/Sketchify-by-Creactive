package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.creactive.sketchify.data.UserService
import com.creactive.sketchify.models.User

@Composable
fun LoginScreen(
    onLoginSuccess: (User) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    //Las variables de la clase user que debemos de llenar para hacer login
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            //Aqui estan los textboxes que me permiten registrarme
            Text("Inicia sesión", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Usuario") }, singleLine = true)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") }, visualTransformation = PasswordVisualTransformation(), singleLine = true)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                //Si el user no existe osea null te va a decir usuario no encontrado en caso de existir te manda al homescreen
                val user = UserService.login(username, password)
                if (user != null) {
                    errorMessage = null
                    onLoginSuccess(user)
                } else {
                    errorMessage = "Usuario no encontrado"
                }
            }) {
                Text("Entrar")
            }
            //Boton de registrarse
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { onNavigateToRegister() }) {
                Text("Registrarse")
            }

            errorMessage?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}