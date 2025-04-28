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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.tooling.preview.Preview
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicDetailsScreen(
    selectedRelation: String,
    selectedMotherTongue: String,
    onNavigateBack: () -> Unit,
    navigateToOtpVerification: (String) -> Unit,
    updateVerificationId: (String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }
    var selectedCountryCode by remember { mutableStateOf("+91") }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

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
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = selectedCountryCode,
                onValueChange = { /* Do nothing - it's uneditable */ },
                readOnly = true,
                label = { Text("Country Code") },
                modifier = Modifier.weight(0.3f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { mobileNumber = it },
                label = { Text("Enter mobile number") },
                modifier = Modifier.weight(0.7f)
            )
        }
        Text(text = "OTP will be sent to this number", style = MaterialTheme.typography.bodySmall)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val phoneNumber = selectedCountryCode + mobileNumber
                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(context as ComponentActivity)
                    .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                            Log.d("Firebase Auth", "onVerificationCompleted:$credential")
                            navigateToOtpVerification(phoneNumber)
                        }

                        override fun onVerificationFailed(e: FirebaseException) {
                            Log.w("Firebase Auth", "onVerificationFailed", e)
                            // TODO: Show an error message to the user
                        }

                        override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                            super.onCodeSent(verificationId, token)
                            Log.d("Firebase Auth", "onCodeSent:$verificationId")
                            updateVerificationId(verificationId)
                            navigateToOtpVerification(phoneNumber)
                            // TODO: Save token if needed for resending OTP
                        }
                    })
                    .build()
                PhoneAuthProvider.verifyPhoneNumber(options)
            },
            enabled = mobileNumber.isNotEmpty()
        ) {
            Text("Get OTP")
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
        navigateToOtpVerification = {},
        updateVerificationId = {}
    )
}