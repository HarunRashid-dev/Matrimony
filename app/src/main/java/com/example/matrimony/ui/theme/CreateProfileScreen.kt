import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.text.style.TextAlign

// Define the orange color
val Orange = Color(0xFFF27A35)
val SelectedButtonColor = Color(0xFFDDEEFF) // A light blue color for selection, adjust as needed

// List of Indian languages
val indianLanguages = listOf(
    "Assamese", "Bengali", "Bodo", "Dogri", "Gujarati", "Hindi", "Kannada",
    "Kashmiri", "Konkani", "Maithili", "Malayalam", "Marathi", "Meitei (Manipuri)",
    "Nepali", "Odia (Oriya)", "Punjabi", "Sanskrit", "Santali", "Sindhi",
    "Tamil", "Telugu", "Urdu"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    onStartRegistrationClicked: (String, String) -> Unit,
    onNavigateBack: () -> Unit // Add a callback for back navigation
) {
    var selectedRelation by remember { mutableStateOf("") }
    var selectedMotherTongue by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val relations = listOf("Myself", "Son", "Daughter", "Brother", "Sister", "Friend", "Relative")

    // Dynamically determine the label for Mother tongue
    val motherTongueLabel = remember(selectedRelation) {
        when (selectedRelation) {
            "Myself" -> "Mother tongue"
            "Son" -> "Son's mother tongue"
            "Daughter" -> "Daughter's mother tongue"
            "Brother" -> "Brother's mother tongue"
            "Sister" -> "Sister's mother tongue"
            "Friend" -> "Friend's mother tongue"
            "Relative" -> "Relative's mother tongue"
            else -> "Mother tongue" // Default label
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Bar with Back Button and Title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { onNavigateBack() } // Handle back navigation on click
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Create Profile",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Start
            )
            // You can add the "@A/அ" symbol here if you have an ImageVector or a Text representation
            Spacer(modifier = Modifier.weight(1f)) // Push the symbol to the right
        }

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "I am creating this profile for",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Relation selection buttons
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                relations.chunked(2).forEach { rowRelations ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        rowRelations.forEach { relation ->
                            val isSelected = selectedRelation == relation
                            OutlinedButton(
                                onClick = { selectedRelation = relation },
                                border = ButtonDefaults.outlinedButtonBorder,
                                shape = MaterialTheme.shapes.small,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.onSurface,
                                    containerColor = if (isSelected) SelectedButtonColor else Color.Transparent
                                ),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(relation)
                            }
                        }
                        if (rowRelations.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Mother tongue dropdown
            if (selectedRelation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedMotherTongue,
                        onValueChange = { /* Do nothing */ },
                        label = { Text(motherTongueLabel) },
                        readOnly = true,
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier.fillMaxWidth()
                            .menuAnchor(),
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        indianLanguages.forEach { language ->
                            DropdownMenuItem(
                                text = { Text(language) },
                                onClick = {
                                    selectedMotherTongue = language
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (selectedRelation.isNotEmpty() && selectedMotherTongue.isNotEmpty()) {
                        onStartRegistrationClicked(selectedRelation, selectedMotherTongue)
                        println("Selected relation: $selectedRelation, Mother tongue: $selectedMotherTongue")
                        // Navigation to the next screen would typically happen here
                    } else {
                        println("Please select a relation and mother tongue")
                        // Optionally show an error message
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = selectedRelation.isNotEmpty() && selectedMotherTongue.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                )
            ) {
                Text("Start Registration")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateProfileScreenPreview() {
    CreateProfileScreen(onStartRegistrationClicked = { _, _ -> }, onNavigateBack = {})
}