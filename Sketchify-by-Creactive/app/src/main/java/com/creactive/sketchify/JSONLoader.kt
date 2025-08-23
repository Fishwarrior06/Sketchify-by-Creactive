package com.creactive.sketchify.data

import android.content.Context
import com.creactive.sketchify.models.User
import com.creactive.sketchify.models.Profile
import com.google.gson.Gson
import java.io.IOException

object JsonLoader {

    fun loadUsers(context: Context): List<User> {
        return try {
            val inputStream = context.assets.open("sample_users.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson(json, Array<User>::class.java).toList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            print(message = "Todo salio mien")
        }
    }


    fun loadProfile(context: Context): List<Profile> {
        return try {
            val inputStream = context.assets.open("sample_profile.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            Gson().fromJson(json, Array<Profile>::class.java).toList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        } finally {
            print(message = "Todo salio mien")
        }
    }
}