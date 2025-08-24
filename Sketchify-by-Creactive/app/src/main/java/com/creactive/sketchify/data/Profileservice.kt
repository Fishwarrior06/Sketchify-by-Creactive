package com.creactive.sketchify.data

import com.creactive.sketchify.models.Profile

object ProfileService {
    private val profiles = mutableListOf(
        Profile(
            id = 1,
            userId = 1,
            name = "Héctor Ortega",
            username = "hector123",
            profilePic = null, // Puedes poner una URL si quieres
            description = "Soy desarrollador apasionado por Kotlin y React."
        )
    )

    fun getProfileByUserId(userId: Int): Profile? {
        return profiles.find { it.userId == userId }
    }

    fun updateProfile(userId: Int, newDescription: String?, newProfilePic: String?): Profile? {
        val profile = profiles.find { it.userId == userId }
        profile?.let {
            val updated = it.copy(
                description = newDescription ?: it.description,
                profilePic = newProfilePic ?: it.profilePic
            )
            profiles[profiles.indexOf(it)] = updated
            return updated
        }
        return null
    }
}