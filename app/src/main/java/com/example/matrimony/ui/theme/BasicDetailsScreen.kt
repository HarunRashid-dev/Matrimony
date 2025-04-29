package com.example.matrimony.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDetailsScreen(
    selectedRelation: String,
    selectedMotherTongue: String,
    onNavigateBack: () -> Unit,
    onRegistrationSuccess: () -> Unit // Callback for successful email registration
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { onNavigateBack() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Basic Details", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your ${selectedRelation.lowercase()}'s name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Create password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "Your password must be within 8-20 characters", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Registration successful
                                Log.d("Firebase Auth", "Email registration successful")
                                val user = auth.currentUser
                                user?.sendEmailVerification()
                                    ?.addOnCompleteListener { sendTask ->
                                        if (sendTask.isSuccessful) {
                                            Log.d("Firebase Auth", "Verification email sent.")
                                            // Inform user to check their email
                                            // For now, let's just navigate to a success screen
                                            onRegistrationSuccess()
                                        } else {
                                            Log.e("Firebase Auth", "Failed to send verification email.", sendTask.exception)
                                            // Handle error sending verification email
                                        }
                                    }
                            } else {
                                // Registration failed
                                Log.e("Firebase Auth", "Email registration failed.", task.exception)
                                // Handle registration failure (e.g., email already in use)
                            }
                        }
                } else {
                    // Optionally show an error message if email or password is empty
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Register with Email")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BasicDetailsScreenPreview() {
    BasicDetailsScreen(
        selectedRelation = "Relative",
        selectedMotherTongue = "Tamil",
        onNavigateBack = {},
        onRegistrationSuccess = {}
    )
}