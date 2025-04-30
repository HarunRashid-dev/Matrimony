package com.example.matrimony

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {
    var verificationId: String by mutableStateOf("")
        private set

    fun updateVerificationId(id: String) {
        verificationId = id
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = Firebase.auth

        setContent {
            Surface(
                modifier = Modifier,
                color = Color.White
            ) {
                AppNavigation(this)
            }
        }

        // Handle email link sign-in
        if (intent?.action == Intent.ACTION_VIEW && intent?.data?.scheme == "https") {
            val link = intent.data.toString()
            if (auth.isSignInWithEmailLink(link)) {
                // Get the email from intent data or request it from user.
                val email = intent.data?.getQueryParameter("email") //try to get email

                if (email != null) {
                    auth.signInWithEmailLink(email, link)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in successful.
                                val user = task.result?.user
                                Log.d("Firebase Auth", "Successfully signed in with email link! : ${user?.email}")
                                // Navigate to home screen.  Use a callback or shared state.
                                // navController.navigate("home_screen") // Can't access navController here directly.
                                //  You'll need a proper state management solution to trigger this.
                                //  For now, a basic approach (that might not be ideal for complex apps) is below:
                                navigateToHomeScreen = true
                            } else {
                                Log.e("Firebase Auth", "Error signing in with email link", task.exception)
                                // Handle error.
                            }
                        }
                } else {
                    Log.e("Firebase Auth", "Email is null in the link")
                    //email is null
                }
            }
        }
    }

    //  Use a property to track navigation.  Not ideal for complex apps.
    var navigateToHomeScreen by mutableStateOf(false)
        private set

    @Composable
    fun AppNavigation(mainActivity: MainActivity) {
        val navController = rememberNavController()

        //  Reset the navigation state after navigating.
        if (navigateToHomeScreen) {
            LaunchedEffect(key1 = Unit) {
                navController.navigate("home_screen") {
                    popUpTo("register_login_screen") { inclusive = true }
                }
                navigateToHomeScreen = false // Reset only after navigating
            }
        }

        NavHost(navController = navController, startDestination = "register_login_screen") {
            composable("register_login_screen") {
                RegisterLoginScreen(
                    onNavigateToCreateProfile = { navController.navigate("create_profile_screen") },
                    onNavigateToHomeScreen = {
                        navController.navigate("home_screen") {
                            popUpTo("register_login_screen") { inclusive = true }
                        }
                    }
                )
            }
            composable("create_profile_screen") {
                CreateProfileScreen(
                    onStartRegistrationClicked = { selectedRelation, selectedMotherTongue ->
                        println("Selected Relation: $selectedRelation, Mother Tongue: $selectedMotherTongue")
                        navController.navigate("basic_details_screen/$selectedRelation/$selectedMotherTongue")
                    },
                    onNavigateBack = { navController.popBackStack() },
                    navigateToOtpVerification = { /* No longer used */ }
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
                val selectedMotherTongue = backStackEntry.arguments?.getString("selectedMotherTongue")
                    ?: ""
                BasicDetailsScreen(
                    selectedRelation = selectedRelation,
                    selectedMotherTongue = selectedMotherTongue,
                    onNavigateBack = { navController.popBackStack() },
                    onRegistrationSuccess = {
                        navController.navigate("final_registration_screen") {
                            popUpTo("create_profile_screen") { inclusive = true }
                        }
                    }
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
                Text("Final Registration Screen") // Replace with your actual final registration screen
            }
            composable("home_screen") {
                Text("Home Screen") // Replace with your actual Home Screen composable
            }
        }
    }
}

@Composable
fun HomeScreen() {
    Text("Welcome to the Home Screen!")
}
