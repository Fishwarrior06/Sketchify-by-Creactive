package com.creactive.sketchify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.creactive.sketchify.ui.screens.HomeScreen
import com.creactive.sketchify.ui.screens.PhotoTypeScreen
import com.creactive.sketchify.ui.theme.SketchifyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SketchifyTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("Home") { HomeScreen(navController) }
                    composable("PhotoType") { PhotoTypeScreen(navController) }
                }
            }
        }
    }
}
