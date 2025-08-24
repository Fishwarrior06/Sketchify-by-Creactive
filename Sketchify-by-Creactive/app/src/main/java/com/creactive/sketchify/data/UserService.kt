package com.creactive.sketchify.data
import android.content.Context
import com.creactive.sketchify.models.User

object UserService {
    private val users: MutableList<User> = mutableListOf()

    fun loadUsersFromJson(context: Context) {
        try {
            val loadedUsers = JsonLoader.loadUsers(context)
            users.addAll(loadedUsers)
        } catch (e: Exception) {
            println("Error al cargar usuarios desde JSON: ${e.message}")
        } finally {
            println("Intento de carga de usuarios finalizado")
        }
    }

    fun login(username: String, password: String): User? {
        return users.find { it.username == username && it.password == password }
    }

    fun getUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    fun registerUser(name: String, username: String, email: String, password: String, profilePic: String? = null): User {
        val newUser = User(
            id = users.size + 1,
            name = name,
            username = username,
            email = email,
            password = password,
            profilePic = profilePic,
            registerDate = java.time.LocalDate.now().toString(),
            role = "user"
        )
        users.add(newUser)
        return newUser
    }
}
