package com.example.matrimony

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun HomeScreen() {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to Matrimony App!",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You're one step closer to meeting your soulmate ❤️",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedImageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = selectedImageUri),
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        }

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = if (selectedImageUri != null) "Change Photo" else "Add Photo")
        }
    }
}
