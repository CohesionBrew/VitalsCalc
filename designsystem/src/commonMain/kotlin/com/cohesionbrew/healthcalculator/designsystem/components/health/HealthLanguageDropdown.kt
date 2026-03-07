package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cohesionbrew.healthcalculator.designsystem.util.isIos
import io.github.robinpcrd.cupertino.CupertinoWheelPicker
import io.github.robinpcrd.cupertino.ExperimentalCupertinoApi
import io.github.robinpcrd.cupertino.rememberCupertinoPickerState
import io.github.robinpcrd.cupertino.theme.CupertinoTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

/**
 * Data class representing a language option in the language dropdown.
 *
 * @param nativeName The display name of the language (e.g., "English", "Espanol / Spanish")
 * @param code The language code (e.g., "en", "es")
 * @param flag The drawable resource for the flag icon, or null if not available
 */
data class LanguageMenuItem(
    val nativeName: String,
    val code: String,
    val flag: DrawableResource? = null
)

/**
 * Adaptive language dropdown that renders as:
 * - iOS: Cupertino-style wheel picker with flag + language name
 * - Android: Material 3 ExposedDropdownMenuBox with flag icons
 *
 * @param modifier Modifier to be applied to the component
 * @param label The label for the dropdown
 * @param options List of available language options
 * @param selectedOption The currently selected language (null if none)
 * @param onOptionSelected Callback when user selects a language
 * @param cancelLabel Label for the cancel button (iOS picker dialog)
 * @param confirmLabel Label for the confirm button (iOS picker dialog)
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalCupertinoApi::class)
@Composable
fun HealthLanguageDropdown(
    modifier: Modifier = Modifier,
    label: String,
    options: List<LanguageMenuItem>,
    selectedOption: LanguageMenuItem?,
    onOptionSelected: (LanguageMenuItem) -> Unit,
    cancelLabel: String = "Cancel",
    confirmLabel: String = "OK"
) {
    if (isIos) {
        CupertinoLanguageSelector(
            modifier = modifier,
            label = label,
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected,
            cancelLabel = cancelLabel,
            confirmLabel = confirmLabel
        )
    } else {
        MaterialLanguageDropdown(
            modifier = modifier,
            label = label,
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = onOptionSelected
        )
    }
}

/**
 * iOS-style wheel picker for language selection.
 */
@OptIn(ExperimentalCupertinoApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CupertinoLanguageSelector(
    modifier: Modifier = Modifier,
    label: String,
    options: List<LanguageMenuItem>,
    selectedOption: LanguageMenuItem?,
    onOptionSelected: (LanguageMenuItem) -> Unit,
    cancelLabel: String,
    confirmLabel: String
) {
    var showPicker by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = selectedOption?.nativeName ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = selectedOption?.flag?.let { res ->
                {
                    Image(
                        painter = painterResource(res),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = showPicker)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    role = Role.DropdownList
                    contentDescription = "$label, ${selectedOption?.nativeName ?: ""}"
                }
        )

        // Transparent overlay to capture clicks
        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { showPicker = true }
        )
    }

    // Show wheel picker dialog when triggered
    if (showPicker && options.isNotEmpty()) {
        CupertinoLanguagePickerDialog(
            options = options,
            selectedOption = selectedOption,
            onOptionSelected = { option ->
                onOptionSelected(option)
                showPicker = false
            },
            onDismiss = { showPicker = false },
            cancelLabel = cancelLabel,
            confirmLabel = confirmLabel
        )
    }
}

/**
 * iOS-style wheel picker dialog for language selection with flag icons.
 */
@OptIn(ExperimentalCupertinoApi::class)
@Composable
private fun CupertinoLanguagePickerDialog(
    options: List<LanguageMenuItem>,
    selectedOption: LanguageMenuItem?,
    onOptionSelected: (LanguageMenuItem) -> Unit,
    onDismiss: () -> Unit,
    cancelLabel: String,
    confirmLabel: String
) {
    // Find initial index
    val initialIndex = selectedOption?.let { options.indexOf(it) }?.takeIf { it >= 0 } ?: 0

    val pickerState = rememberCupertinoPickerState(
        initiallySelectedItemIndex = initialIndex
    )

    // Track current selection
    var currentSelection by remember { mutableStateOf(selectedOption ?: options.firstOrNull()) }

    // Update selection when wheel scrolls
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
                    // Action buttons row at top (iOS style)
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
                            onClick = {
                                currentSelection?.let { onOptionSelected(it) }
                            }
                        ) {
                            Text(
                                text = confirmLabel,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Wheel picker with flag + language name
                    CupertinoWheelPicker(
                        state = pickerState,
                        items = options,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) { language ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            language.flag?.let { flagRes ->
                                Image(
                                    painter = painterResource(flagRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(12.dp))
                            }
                            Text(
                                text = language.nativeName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Standard Material 3 dropdown for language selection (Android).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaterialLanguageDropdown(
    modifier: Modifier = Modifier,
    label: String,
    options: List<LanguageMenuItem>,
    selectedOption: LanguageMenuItem?,
    onOptionSelected: (LanguageMenuItem) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedOption?.nativeName ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            leadingIcon = selectedOption?.flag?.let { res ->
                {
                    Image(
                        painter = painterResource(res),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            selectionOption.flag?.let { flagRes ->
                                Image(
                                    painter = painterResource(flagRes),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                            }
                            Text(selectionOption.nativeName)
                        }
                    },
                    onClick = {
                        onOptionSelected(selectionOption)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
