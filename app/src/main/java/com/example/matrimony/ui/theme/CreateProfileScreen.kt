import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api

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
fun CreateProfileScreen(onStartRegistrationClicked: (String, String) -> Unit) {
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

        // Mother tongue dropdown (visible only if a relation is selected)
        if (selectedRelation.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedMotherTongue,
                    onValueChange = { /* Do nothing */ },
                    label = { Text(motherTongueLabel) }, // Use the dynamic label
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
                } else {
                    println("Please select a relation and mother tongue")
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

@Preview(showBackground = true)
@Composable
fun CreateProfileScreenPreview() {
    CreateProfileScreen(onStartRegistrationClicked = { _, _ -> })
}