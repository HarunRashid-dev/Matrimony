package com.example.matrimony.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Define the orange color (you might want to put this in your colors.kt file)
val Orange = Color(0xFFF27A35) // Replace with the exact hex code if needed

@Composable
fun CreateProfileScreen(onStartRegistrationClicked: (String) -> Unit) {
    var selectedRelation by remember { mutableStateOf("") }
    val relations = listOf("Myself", "Son", "Daughter", "Brother", "Sister", "Friend", "Relative")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "I am creating this profile for",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Options arranged in a grid-like fashion
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            relations.chunked(2).forEach { rowRelations ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    rowRelations.forEach { relation ->
                        OutlinedButton(
                            onClick = { selectedRelation = relation },
                            border = ButtonDefaults.outlinedButtonBorder,
                            shape = MaterialTheme.shapes.small,
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.weight(1f) // Distribute space equally in the row
                        ) {
                            Text(relation)
                        }
                    }
                    // Add an empty Spacer if there's only one item in the row to keep alignment
                    if (rowRelations.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Push the button to the bottom

        Button(
            onClick = {
                if (selectedRelation.isNotEmpty()) {
                    onStartRegistrationClicked(selectedRelation)
                    // You would typically navigate to the next step here
                    println("Selected relation: $selectedRelation")
                    // If you want to navigate from here, you'd need to pass the navController
                    // down to this screen as well.
                } else {
                    // Optionally show an error message if no option is selected
                    println("Please select a relation")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedRelation.isNotEmpty(), // Enable the button only if a relation is selected
            colors = ButtonDefaults.buttonColors(
                containerColor = Orange,
                contentColor = Color.White // Ensure the text color is readable on the orange background
            )
        ) {
            Text("Start Registration")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateProfileScreenPreview() {
    CreateProfileScreen(onStartRegistrationClicked = {})
}