package com.creactive.sketchify.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.creactive.sketchify.data.UserService
import com.creactive.sketchify.models.User
import androidx.compose.ui.Alignment

@Composable
fun RegisterScreen(
    onRegisterSuccess: (User) -> Unit,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var profilePic by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrar usuario", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") }, singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = username, onValueChange = { username = it }, label = { Text("Username") }, singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, singleLine = true)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = password, onValueChange = {password = it}, label = { Text("password") }, singleLine = true)
        OutlinedTextField(value = profilePic, onValueChange = { profilePic = it }, label = { Text("Foto (URL)") })
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if (name.isBlank() || username.isBlank() || email.isBlank()) {
                errorMessage = "Completa todos los campos"
            } else {
                val newUser = UserService.registerUser(name, username, email, profilePic)
                onRegisterSuccess(newUser)
            }
        }) {
            Text("Registrar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = { onNavigateBack() }) {
            Text("Volver al login")
        }

        errorMessage?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it, color = MaterialTheme.colorScheme.error)
        }
    }
}