package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cohesionbrew.healthcalculator.designsystem.util.isIos
import io.github.robinpcrd.cupertino.CupertinoWheelPicker
import io.github.robinpcrd.cupertino.ExperimentalCupertinoApi
import io.github.robinpcrd.cupertino.rememberCupertinoPickerState
import io.github.robinpcrd.cupertino.theme.CupertinoTheme

@Composable
fun <T : Any> AdaptiveWheelSelector(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T) -> String,
    label: String,
    modifier: Modifier = Modifier,
    cancelLabel: String = "Cancel",
    confirmLabel: String = "OK",
    leadingIcon: (@Composable (T) -> Unit)? = null
) {
    if (isIos) {
        CupertinoWheelSelectorImpl(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
            optionLabel = optionLabel,
            label = label,
            modifier = modifier,
            cancelLabel = cancelLabel,
            confirmLabel = confirmLabel
        )
    } else {
        MaterialDropdownSelectorImpl(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
            optionLabel = optionLabel,
            label = label,
            modifier = modifier,
            leadingIcon = leadingIcon
        )
    }
}

@OptIn(ExperimentalCupertinoApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun <T : Any> CupertinoWheelSelectorImpl(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T) -> String,
    label: String,
    modifier: Modifier = Modifier,
    cancelLabel: String,
    confirmLabel: String
) {
    var showPicker by remember { mutableStateOf(false) }
    val currentLabel = selectedOption?.let { optionLabel(it) } ?: ""

    Box(modifier = modifier) {
        OutlinedTextField(
            value = currentLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPicker) },
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    role = Role.DropdownList
                    contentDescription = "$label, $currentLabel"
                }
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showPicker = true }
        )
    }

    if (showPicker && options.isNotEmpty()) {
        CupertinoWheelPickerDialog(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = { option ->
                onOptionSelected(option)
                showPicker = false
            },
            optionLabel = optionLabel,
            onDismiss = { showPicker = false },
            cancelLabel = cancelLabel,
            confirmLabel = confirmLabel
        )
    }
}

@OptIn(ExperimentalCupertinoApi::class)
@Composable
private fun <T : Any> CupertinoWheelPickerDialog(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T) -> String,
    onDismiss: () -> Unit,
    cancelLabel: String,
    confirmLabel: String
) {
    val initialIndex = selectedOption?.let { options.indexOf(it) }?.takeIf { it >= 0 } ?: 0
    val pickerState = rememberCupertinoPickerState(initiallySelectedItemIndex = initialIndex)
    var currentSelection by remember { mutableStateOf(selectedOption ?: options.firstOrNull()) }

    LaunchedEffect(pickerState.selectedItemIndex) {
        val actualIndex = pickerState.selectedItemIndex % options.size
        if (actualIndex in options.indices) {
            currentSelection = options[actualIndex]
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        CupertinoTheme {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = cancelLabel,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        TextButton(
                            onClick = { currentSelection?.let { onOptionSelected(it) } }
                        ) {
                            Text(
                                text = confirmLabel,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    CupertinoWheelPicker(
                        state = pickerState,
                        items = options,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) { option ->
                        Text(
                            text = optionLabel(option),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T : Any> MaterialDropdownSelectorImpl(
    options: List<T>,
    selectedOption: T?,
    onOptionSelected: (T) -> Unit,
    optionLabel: @Composable (T) -> String,
    label: String,
    modifier: Modifier = Modifier,
    leadingIcon: (@Composable (T) -> Unit)? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val expandedState = if (expanded) "Expanded" else "Collapsed"
    val currentLabel = selectedOption?.let { optionLabel(it) } ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = currentLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = if (leadingIcon != null && selectedOption != null) {
                { leadingIcon(selectedOption) }
            } else {
                null
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
                .semantics {
                    role = Role.DropdownList
                    contentDescription = "$label, $currentLabel"
                    stateDescription = expandedState
                }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            leadingIcon?.invoke(option)
                            Text(optionLabel(option))
                        }
                    },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
