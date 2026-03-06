package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthDropdownSelector(
    options: List<String>,
    caption: String,
    selected: String,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val expandedState = if (expanded) "Expanded" else "Collapsed"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text(caption) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .semantics {
                    role = Role.DropdownList
                    contentDescription = "$caption, $selected"
                    stateDescription = expandedState
                }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onSelect(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

data class SelectableItem(
    val key: String,
    val displayName: String,
    val enabled: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthDropdownWithKeysSelector(
    label: String,
    options: List<SelectableItem>,
    selectedKey: String?,
    onItemSelected: (key: String, selectedItem: SelectableItem) -> Unit,
    modifier: Modifier = Modifier,
    fallbackText: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    val expandedState = if (expanded) "Expanded" else "Collapsed"

    val currentSelectedItem = options.find { it.key == selectedKey }
    val buttonText = currentSelectedItem?.displayName ?: fallbackText

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (currentSelectedItem?.enabled != false) expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = buttonText,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .semantics {
                    role = Role.DropdownList
                    contentDescription = "$label, $buttonText"
                    stateDescription = expandedState
                }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    enabled = option.enabled,
                    onClick = {
                        onItemSelected(option.key, option)
                        expanded = false
                    }
                )
            }
        }
    }
}
