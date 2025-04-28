package com.example.matrimony.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.FirebaseTooManyRequestsException
import com.example.matrimony.MainActivity // Assuming MainActivity is in the root package
import com.google.firebase.auth.PhoneAuthProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtpVerificationScreen(
    mobileNumber: String,
    onVerificationSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
    verificationId: String
) {
    var otp by remember { mutableStateOf("") }
    val auth = FirebaseAuth.getInstance()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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
            Text(text = "Verify OTP", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Enter the verification code sent to $mobileNumber",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("OTP") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (otp.isNotEmpty()) {
                    val credential = PhoneAuthProvider.getCredential(verificationId, otp) // 'credential' is declared here
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // OTP verification successful
                                Log.d("Firebase Auth", "OTP verification successful")
                                onVerificationSuccess() // Navigate to the next screen
                            } else {
                                // OTP verification failed
                                Log.e("Firebase Auth", "OTP verification failed: ${task.exception?.message}")
                                if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                    // Incorrect OTP
                                    // TODO: Show an error message to the user about incorrect OTP
                                } else if (task.exception is FirebaseTooManyRequestsException) {
                                    // SMS quota exceeded or app not whitelisted
                                    // TODO: Show an error message about SMS limits
                                } else {
                                    // Other verification errors
                                    // TODO: Show a generic error message
                                }
                            }
                        }
                } else {
                    // TODO: Show an error message that OTP is required
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verify OTP")
        }
    }
}
