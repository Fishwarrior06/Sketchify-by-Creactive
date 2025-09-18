package com.creactive.sketchify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.creactive.sketchify.ui.screens.HomeScreen
import com.creactive.sketchify.ui.screens.PhotoTypeScreen
import com.creactive.sketchify.ui.screens.PastSessionsScreen
import com.creactive.sketchify.ui.theme.SketchifyTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SketchifyTheme {
                val navController = rememberNavController()

                // 👇 calcular window size una sola vez
                val windowSizeClass: WindowSizeClass = calculateWindowSizeClass(this)

                NavHost(navController = navController, startDestination = "home") {

                    composable("home") {
                        HomeScreen(navController, windowSizeClass)
                    }

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
                        PhotoTypeScreen(navController, modo, windowSizeClass)
                    }

                    composable("PastSessions") {
                        PastSessionsScreen(navController, windowSizeClass)
                    }
                }
            }
        }
    }
}