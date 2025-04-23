package com.example.matrimony

import CreateProfileScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.matrimony.ui.screens.RegisterLoginScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier,
                color = Color.White
            ) {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "register_login_screen") {
        composable("register_login_screen") {
            RegisterLoginScreen(onNavigateToCreateProfile = {
                navController.navigate("create_profile_screen")
            })
        }
        composable("create_profile_screen") {
            CreateProfileScreen(
                onStartRegistrationClicked = { selectedRelation, selectedMotherTongue ->
                    println("Selected Relation: $selectedRelation, Mother Tongue: $selectedMotherTongue")
                    // Navigate to the next screen, passing this data if needed
                    // navController.navigate("next_screen/$selectedRelation/$selectedMotherTongue")
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        // You can define more routes here for other screens
    }
}