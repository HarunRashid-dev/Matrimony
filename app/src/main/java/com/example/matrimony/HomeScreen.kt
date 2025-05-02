package com.example.matrimony

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.matrimony.model.UserProfile

@Composable
fun HomeScreen() {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val currentUser = auth.currentUser

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        if (currentUser != null) {
            val userId = currentUser.uid
            firestore.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userProfile = document.toObject(UserProfile::class.java)
                        Log.d("HomeScreen", "User data loaded")
                    } else {
                        errorMessage = "User data not found."
                        Log.e("HomeScreen", "No document for user")
                    }
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    errorMessage = exception.message
                    Log.e("HomeScreen", "Error: ${exception.message}")
                    isLoading = false
                }
        } else {
            errorMessage = "User not signed in"
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        errorMessage != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: $errorMessage", modifier = Modifier.padding(16.dp))
            }
        }
        userProfile != null -> {
            Column(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)) {
                Text("Welcome to Matrimony App!", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))

                Text("Personal Information", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Text("1. Age: ${userProfile?.age}")
                Text("2. Height: ${userProfile?.height}")
                Text("3. Body Type: ${userProfile?.bodyType}")
                Text("4. Mother Tongue: ${userProfile?.motherTongue}")
                Text("5. Profile Created By: ${userProfile?.profileCreatedBy}")
                Text("6. Marital Status: ${userProfile?.maritalStatus}")
                Text("7. Lives In: ${userProfile?.livesIn}")
                Text("8. Eating Habits: ${userProfile?.eatingHabits}")
                Text("9. Religion: ${userProfile?.religion}")
                Text("10. Caste: ${userProfile?.caste}")
                Text("11. Gothra(m): ${userProfile?.gothra}")
                Text("12. Horoscope: ${userProfile?.horoscope}")
                Text("13. Education: ${userProfile?.education}")
                Text("14. Studied at: ${userProfile?.studiedAt}")
                Text("15. Employment: ${userProfile?.employment}")
                Text("16. Works at: ${userProfile?.worksAt}")
                Text("17. Occupation: ${userProfile?.occupation}")
                Text("18. Income: ${userProfile?.income}")

                Spacer(modifier = Modifier.height(16.dp))
                Text("About Her Family", style = MaterialTheme.typography.titleMedium)
                Text("1. Family Type: ${userProfile?.familyType}")
                Text("2. Family Status: ${userProfile?.familyStatus}")
                Text("3. Brothers: ${userProfile?.brothers}")
                Text("4. Sisters: ${userProfile?.sisters}")

                Spacer(modifier = Modifier.height(16.dp))
                Text("Habits", style = MaterialTheme.typography.titleMedium)
                Text("Smoking Habits: ${userProfile?.smokingHabits}")
                Text("Drinking Habits: ${userProfile?.drinkingHabits}")
            }
        }
    }
}
