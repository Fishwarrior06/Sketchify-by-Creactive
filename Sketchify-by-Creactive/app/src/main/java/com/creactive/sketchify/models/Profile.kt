package com.creactive.sketchify.models
data class Profile (
    val id : Int,
    val userId: Int,
    val name : String,
    val username: String,
    val profilePic: String? = null,
    val description: String? = null
)