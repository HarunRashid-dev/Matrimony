package com.example.matrimony.repository

import android.util.Log
import com.example.matrimony.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirestoreRepository {

    fun saveUserProfile(userProfile: UserProfile, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            onFailure(Exception("User not logged in"))
            return
        }

        val db = Firebase.firestore

        db.collection("users").document(uid)
            .set(userProfile)
            .addOnSuccessListener {
                Log.d("Firestore", "User data saved successfully")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error saving user data", e)
                onFailure(e)
            }
    }

    fun loadUserProfile(onSuccess: (UserProfile) -> Unit, onFailure: (Exception) -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid == null) {
            onFailure(Exception("User not logged in"))
            return
        }

        val db = Firebase.firestore

        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val profile = document.toObject(UserProfile::class.java)
                    if (profile != null) {
                        onSuccess(profile)
                    } else {
                        onFailure(Exception("User data not found"))
                    }
                } else {
                    onFailure(Exception("User data not found"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}
