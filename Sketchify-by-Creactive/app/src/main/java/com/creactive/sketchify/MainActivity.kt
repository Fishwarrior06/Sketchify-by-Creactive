package com.creactive.sketchify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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

                    // HomeScreen
                    composable("home") {
                        HomeScreen(navController)
                    }

                    // PhotoTypeScreen con argumento 'modo'
                    composable(
                        route = "PhotoType?modo={modo}",
                        arguments = listOf(
                            navArgument("modo") {
                                type = NavType.StringType
                                defaultValue = "camara"
                                nullable = true
                            }
                        )
                    ) { backStackEntry ->
                        val modo = backStackEntry.arguments?.getString("modo") ?: "camara"
                        PhotoTypeScreen(navController, modo)
                    }
                }
            }
        }
    }
}