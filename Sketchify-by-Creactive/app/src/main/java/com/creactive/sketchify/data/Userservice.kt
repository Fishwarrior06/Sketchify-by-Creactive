package com.creactive.sketchify.data
import com.creactive.sketchify.models.User

object Userservice {
    private val Users = mutableListOf(
        User(1, "Héctor Ortega", "hector@email.com", "Fishwarrior06", "Splashote",null, registerDate = "2025-08-21", role = "user")
    )

    fun login(username: String, password: String): User? {
        return Users.find { it.username == username }
    }

    fun getUserById(id: Int): User? {
        return Users.find { it.id == id }
    }

    fun registerUser(name: String, username: String, email: String, profilePic: String? = null): User {
        val newUser = User(
            id = Users.size + 1,
            name = name,
            username = username,
            email = email,
            password = "Splashote",
            profilePic = profilePic,
            registerDate = java.time.LocalDate.now().toString(),
            role = "user"
        )
        Users.add(newUser)
        return newUser
    }
}
