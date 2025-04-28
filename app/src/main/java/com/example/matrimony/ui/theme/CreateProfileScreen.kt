package com.example.matrimony.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    onStartRegistrationClicked: (String, String) -> Unit,
    onNavigateBack: () -> Unit,
    navigateToOtpVerification: (String) -> Unit // You might not need this directly here anymore
) {
    var selectedRelation by remember { mutableStateOf("") }
    val relations = listOf("Myself", "Son", "Daughter", "Brother", "Sister", "Friend", "Relative")

    var selectedMotherTongue by remember { mutableStateOf("") }
    var expandedMotherTongue by remember { mutableStateOf(false) }
    val motherTongues = listOf("Tamil", "Telugu", "Malayalam", "Kannada", "Hindi", "English", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "I am creating this profile for",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Relationship Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            relations.take(3).forEach { relation ->
                OutlinedButton(
                    onClick = { selectedRelation = relation },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRelation == relation) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        contentColor = if (selectedRelation == relation) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(relation, fontSize = 14.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            relations.drop(3).take(3).forEach { relation ->
                OutlinedButton(
                    onClick = { selectedRelation = relation },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRelation == relation) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        contentColor = if (selectedRelation == relation) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(relation, fontSize = 14.sp)
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center // Center the last button
        ) {
            relations.drop(6).forEach { relation ->
                OutlinedButton(
                    onClick = { selectedRelation = relation },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedRelation == relation) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                        contentColor = if (selectedRelation == relation) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(relation, fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Mother Tongue Dropdown
        Text(
            text = "Mother tongue",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Start
        )
        ExposedDropdownMenuBox(
            expanded = expandedMotherTongue,
            onExpandedChange = { expandedMotherTongue = !expandedMotherTongue }
        ) {
            OutlinedTextField(
                readOnly = true,
                value = selectedMotherTongue,
                onValueChange = { },
                label = { Text("Friend's mother tongue") }, // Placeholder label
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMotherTongue) },
                modifier = Modifier.fillMaxWidth()
                    .menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expandedMotherTongue,
                onDismissRequest = { expandedMotherTongue = false }
            ) {
                motherTongues.forEach { tongue ->
                    DropdownMenuItem(
                        text = { Text(tongue) },
                        onClick = {
                            selectedMotherTongue = tongue
                            expandedMotherTongue = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f)) // Push the button to the bottom

        Button(
            onClick = {
                if (selectedRelation.isNotEmpty() && selectedMotherTongue.isNotEmpty()) {
                    onStartRegistrationClicked(selectedRelation, selectedMotherTongue)
                } else {
                    // Optionally show an error message if fields are not selected
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            enabled = selectedRelation.isNotEmpty() && selectedMotherTongue.isNotEmpty()
        ) {
            Text("Start Registration", style = MaterialTheme.typography.titleMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateProfileScreenPreview() {
    CreateProfileScreen(onStartRegistrationClicked = { _, _ -> }, onNavigateBack = {}, navigateToOtpVerification = {})
}