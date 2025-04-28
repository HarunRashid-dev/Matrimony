package com.example.matrimony

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.matrimony.ui.theme.BasicDetailsScreen
import com.example.matrimony.ui.theme.CreateProfileScreen
import com.example.matrimony.ui.theme.OtpVerificationScreen
import com.example.matrimony.ui.screens.RegisterLoginScreen

class MainActivity : ComponentActivity() {
    var verificationId: String by mutableStateOf("")
        private set

    fun updateVerificationId(id: String) {
        verificationId = id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier,
                color = Color.White
            ) {
                AppNavigation(this)
            }
        }
    }
}

@Composable
fun AppNavigation(mainActivity: MainActivity) {
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
                    navController.navigate("basic_details_screen/$selectedRelation/$selectedMotherTongue")
                },
                onNavigateBack = { navController.popBackStack() },
                navigateToOtpVerification = { phoneNumber ->
                    navController.navigate("otp_verification_screen/$phoneNumber")
                }
            )
        }
        composable(
            route = "basic_details_screen/{selectedRelation}/{selectedMotherTongue}",
            arguments = listOf(
                navArgument("selectedRelation") { type = NavType.StringType },
                navArgument("selectedMotherTongue") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val selectedRelation = backStackEntry.arguments?.getString("selectedRelation") ?: ""
            val selectedMotherTongue = backStackEntry.arguments?.getString("selectedMotherTongue") ?: ""
            BasicDetailsScreen(
                selectedRelation = selectedRelation,
                selectedMotherTongue = selectedMotherTongue,
                onNavigateBack = { navController.popBackStack() },
                navigateToOtpVerification = { phoneNumber ->
                    navController.navigate("otp_verification_screen/$phoneNumber")
                },
                updateVerificationId = mainActivity::updateVerificationId
            )
        }
        composable(
            route = "otp_verification_screen/{mobileNumber}",
            arguments = listOf(navArgument("mobileNumber") { type = NavType.StringType })
        ) { backStackEntry ->
            val mobileNumber = backStackEntry.arguments?.getString("mobileNumber") ?: ""
            OtpVerificationScreen(
                mobileNumber = mobileNumber,
                onVerificationSuccess = {
                    navController.navigate("final_registration_screen") {
                        popUpTo("basic_details_screen") { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() },
                verificationId = mainActivity.verificationId // Pass the verificationId
            )
        }
        composable("final_registration_screen") {
            Text("Final Registration Screen")
        }
    }
}