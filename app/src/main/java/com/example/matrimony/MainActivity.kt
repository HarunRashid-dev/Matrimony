package com.example.matrimony

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
import com.example.matrimony.ui.screens.CreateProfileScreen
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
            CreateProfileScreen(onStartRegistrationClicked = { selectedRelation ->
                // Handle the selected relation and potentially navigate further
                println("Selected relation: $selectedRelation")
                // Example of navigating to another screen with data:
                // navController.navigate("registration_details_screen/$selectedRelation")
            })
        }
        // You can define more routes here for other screens
    }
}