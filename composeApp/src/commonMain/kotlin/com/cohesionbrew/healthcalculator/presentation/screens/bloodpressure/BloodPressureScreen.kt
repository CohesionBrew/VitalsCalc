package com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.domain.model.BpCategory
import com.cohesionbrew.healthcalculator.presentation.components.health.getBpCategoryColor
import com.cohesionbrew.healthcalculator.domain.model.history.BloodPressureHistoryEntry
import com.cohesionbrew.healthcalculator.generated.resources.*
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource

@Composable
fun BloodPressureScreen(uiStateHolder: BloodPressureUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BloodPressureScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BloodPressureScreen(uiState: BloodPressureUiState, onUiEvent: (BloodPressureUiEvent) -> Unit) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_blood_pressure),
        isScrollableContent = true,
        includeBottomInsets = false
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Input card with real-time classification
            BpInputCard(
                uiState = uiState,
                onUiEvent = onUiEvent,
                modifier = Modifier.fillMaxWidth()
            )

            // Save success feedback
            AnimatedVisibility(
                visible = uiState.showSaveSuccess,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.bp_reading_saved),
                        color = Color(0xFF4CAF50),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { onUiEvent(BloodPressureUiEvent.OnClearInputs) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(Res.string.bp_clear))
                }

                Button(
                    onClick = { onUiEvent(BloodPressureUiEvent.OnSaveReading) },
                    enabled = uiState.canSave,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(Res.string.bp_save_reading))
                }
            }

            // Guidance card (when classification is shown)
            uiState.category?.let { category ->
                BpGuidanceCard(
                    category = category,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Medical disclaimer
            MedicalDisclaimerCard(modifier = Modifier.fillMaxWidth())

            // Recent readings section
            if (uiState.recentReadings.isNotEmpty()) {
                RecentReadingsSection(
                    readings = uiState.recentReadings,
                    onDeleteReading = { id -> onUiEvent(BloodPressureUiEvent.OnDeleteReading(id)) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun BpInputCard(
    uiState: BloodPressureUiState,
    onUiEvent: (BloodPressureUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Systolic and Diastolic inputs side by side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = if (uiState.systolic > 0) uiState.systolic.toString() else "",
                    onValueChange = { onUiEvent(BloodPressureUiEvent.OnSystolicChanged(it.toIntOrNull() ?: 0)) },
                    label = { Text(stringResource(Res.string.bp_systolic)) },
                    placeholder = { Text("120") },
                    suffix = { Text(stringResource(Res.string.unit_mmhg)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    isError = uiState.systolic > 0 && uiState.systolic !in 40..300
                )

                OutlinedTextField(
                    value = if (uiState.diastolic > 0) uiState.diastolic.toString() else "",
                    onValueChange = { onUiEvent(BloodPressureUiEvent.OnDiastolicChanged(it.toIntOrNull() ?: 0)) },
                    label = { Text(stringResource(Res.string.bp_diastolic)) },
                    placeholder = { Text("80") },
                    suffix = { Text(stringResource(Res.string.unit_mmhg)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    isError = uiState.diastolic > 0 && uiState.diastolic !in 20..200
                )
            }

            // Pulse input (optional) - full width
            OutlinedTextField(
                value = if (uiState.pulse > 0) uiState.pulse.toString() else "",
                onValueChange = { onUiEvent(BloodPressureUiEvent.OnPulseChanged(it.toIntOrNull() ?: 0)) },
                label = { Text(stringResource(Res.string.bp_pulse_optional)) },
                placeholder = { Text("72") },
                suffix = { Text(stringResource(Res.string.unit_bpm)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Notes input (optional) - full width
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = { onUiEvent(BloodPressureUiEvent.OnNotesChanged(it)) },
                label = { Text(stringResource(Res.string.bp_notes_optional)) },
                placeholder = { Text(stringResource(Res.string.bp_notes_placeholder)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                ),
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            // Validation error
            uiState.validationError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
                )
            }

            // Real-time classification display
            uiState.category?.let { category ->
                BpCategoryDisplay(
                    systolic = uiState.systolic,
                    diastolic = uiState.diastolic,
                    category = category,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun BpCategoryDisplay(
    systolic: Int,
    diastolic: Int,
    category: BpCategory,
    modifier: Modifier = Modifier
) {
    val statusColor = getBpCategoryColor(category)

    Surface(
        modifier = modifier,
        color = statusColor.copy(alpha = 0.15f),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category color indicator with heart icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(statusColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "$systolic / $diastolic mmHg",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun BpGuidanceCard(
    category: BpCategory,
    modifier: Modifier = Modifier
) {
    val isCrisis = category == BpCategory.CRISIS
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = if (isCrisis)
                MaterialTheme.colorScheme.errorContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (isCrisis)
                    stringResource(Res.string.bp_health_alert)
                else
                    stringResource(Res.string.bp_health_guidance),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isCrisis)
                    MaterialTheme.colorScheme.onErrorContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.guidance,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isCrisis)
                    MaterialTheme.colorScheme.onErrorContainer
                else
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun MedicalDisclaimerCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Text(
            text = stringResource(Res.string.bp_medical_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun RecentReadingsSection(
    readings: List<BloodPressureHistoryEntry>,
    onDeleteReading: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(Res.string.bp_recent_readings),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(Res.string.bp_total_readings, readings.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Show last 5 readings
        readings.take(5).forEach { reading ->
            CompactReadingRow(
                reading = reading,
                onDelete = { onDeleteReading(reading.id) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CompactReadingRow(
    reading: BloodPressureHistoryEntry,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val category = reading.category?.let { categoryFromDisplayName(it) } ?: BpCategory.NORMAL
    val statusColor = getBpCategoryColor(category)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Color indicator dot
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(statusColor)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${reading.systolic}/${reading.diastolic} mmHg",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = formatDisplayDateTime(reading.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    reading.pulse?.let { pulse ->
                        Text(
                            text = stringResource(Res.string.bp_pulse_bpm, pulse),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(Res.string.bp_delete_reading_desc, reading.systolic, reading.diastolic),
                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

/**
 * Resolves a BpCategory from a stored displayName string.
 */
private fun categoryFromDisplayName(name: String): BpCategory {
    return BpCategory.entries.firstOrNull {
        it.displayName.equals(name, ignoreCase = true)
    } ?: BpCategory.NORMAL
}

/**
 * Formats an epoch-millis timestamp to a readable date-time string.
 */
private fun formatDisplayDateTime(epochMillis: Long): String {
    val instant = Instant.fromEpochMilliseconds(epochMillis)
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    val month = local.monthNumber.toString().padStart(2, '0')
    val day = local.dayOfMonth.toString().padStart(2, '0')
    val hour = local.hour.toString().padStart(2, '0')
    val minute = local.minute.toString().padStart(2, '0')
    return "${local.year}-$month-$day $hour:$minute"
}
