package com.example.matrimony.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterLoginScreen(
    onNavigateToCreateProfile: () -> Unit,
    onNavigateToHomeScreen: () -> Unit
) {
    var emailLogin by remember { mutableStateOf("") }
    var passwordLogin by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New user?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { onNavigateToCreateProfile() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Create profile", color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 16.dp)
        ) {
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
            Text(text = "  or  ", color = Color.Gray)
            Divider(modifier = Modifier.weight(1f), color = Color.Gray)
        }

        Text(text = "Already registered?", color = Color.Black, modifier = Modifier.padding(bottom = 16.dp))

        OutlinedTextField(
            value = emailLogin,
            onValueChange = { emailLogin = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = passwordLogin,
            onValueChange = { passwordLogin = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (emailLogin.isNotEmpty() && passwordLogin.isNotEmpty()) {
                    auth.signInWithEmailAndPassword(emailLogin, passwordLogin)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = task.result?.user
                                if (user?.isEmailVerified == true) {
                                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                    onNavigateToHomeScreen()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Please verify your email before logging in.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }

                            } else {
                                Log.e(
                                    "Firebase Auth",
                                    "Email/password sign-in failed",
                                    task.exception
                                )
                                Toast.makeText(
                                    context,
                                    "Login failed: ${task.exception?.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                } else {
                    Toast.makeText(context, "Please enter email and password", Toast.LENGTH_SHORT).show()
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(50)
        ) {
            Text(text = "Login", color = Color.White)
        }


    }
}
