package com.creactive.sketchify.data

import android.content.Context
import com.creactive.sketchify.models.User
import com.creactive.sketchify.models.Profile
import com.google.gson.Gson
import java.io.IOException

object JsonLoader {

    //Agarra el contexto del usuario
    fun loadUsers(context: Context): List<User> {
        //Intenta conseguir el assets de "sample_users.json" e intenta leer los contenidos del JSON
        return try {
            val inputStream = context.assets.open("sample_users.json")
            val json = inputStream.bufferedReader().use { it.readText() }
            //Como los datos del JSON estan en formatos de Array y nesecita convertirlo en una lista
            Gson().fromJson(json, Array<User>::class.java).toList()
            //Si hay una excepcion y te printea una lista sin nada porque no lo pudo leer
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
            //Finalmente si si jalo el try de manda un mensaje en consola que dice todo salio mien
        } finally {
            print(message = "Todo salio mien")
        }
    }

    //Aqui es mas de lo mismo de arriba solo que aqui es para los perfiles de los usuarios
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