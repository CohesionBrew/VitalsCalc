package com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.domain.model.BpCategory
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun BloodPressureScreen(uiStateHolder: BloodPressureUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BloodPressureScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BloodPressureScreen(uiState: BloodPressureUiState, onUiEvent: (BloodPressureUiEvent) -> Unit) {
    ScreenWithToolbar(title = stringResource(Res.string.title_screen_blood_pressure), isScrollableContent = true, includeBottomInsets = false) {
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HealthScreenTitle(text = stringResource(Res.string.bp_title))
            IntInputField(stringResource(Res.string.bp_systolic), stringResource(Res.string.unit_mmhg), uiState.systolic) { onUiEvent(BloodPressureUiEvent.OnSystolicChanged(it)) }
            IntInputField(stringResource(Res.string.bp_diastolic), stringResource(Res.string.unit_mmhg), uiState.diastolic) { onUiEvent(BloodPressureUiEvent.OnDiastolicChanged(it)) }
            IntInputField(stringResource(Res.string.bp_pulse_optional), stringResource(Res.string.unit_bpm), uiState.pulse) { onUiEvent(BloodPressureUiEvent.OnPulseChanged(it)) }

            uiState.validationError?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            HealthActionButton(text = stringResource(Res.string.bp_classify), isLoading = uiState.isLoading, onClick = { onUiEvent(BloodPressureUiEvent.OnClassify) })

            if (uiState.isCalculated && uiState.category != null) {
                Spacer(modifier = Modifier.height(8.dp))
                BpResultCard(uiState.systolic, uiState.diastolic, uiState.category)
            }
        }
    }
}

@Composable
private fun IntInputField(label: String, suffix: String, value: Int, onValueChange: (Int) -> Unit) {
    OutlinedTextField(
        value = if (value > 0) value.toString() else "",
        onValueChange = { onValueChange(it.toIntOrNull() ?: 0) },
        label = { Text(label) }, suffix = { Text(suffix) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true, modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun BpResultCard(systolic: Int, diastolic: Int, category: BpCategory) {
    val statusColor = when (category) {
        BpCategory.NORMAL -> Color(0xFF4CAF50)
        BpCategory.ELEVATED -> Color(0xFFFFC107)
        BpCategory.HYPERTENSION_1 -> Color(0xFFFF9800)
        BpCategory.HYPERTENSION_2 -> Color(0xFFF44336)
        BpCategory.CRISIS -> Color(0xFFB71C1C)
    }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("$systolic / $diastolic mmHg", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(category.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = statusColor)
            Text(category.guidance, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
