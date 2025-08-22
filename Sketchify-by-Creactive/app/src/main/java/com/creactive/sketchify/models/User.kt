package com.creactive.sketchify.models

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val username: String,
    val password: String,
    val profilePic: String? = null,
    val registerDate: String,
    val role: String = "user",
)