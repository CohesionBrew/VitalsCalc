package com.cohesionbrew.healthcalculator.presentation.screens.heartrate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthUnitSystemSwitch
import com.cohesionbrew.healthcalculator.domain.calculator.HeartRateZoneCalculator
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun HeartRateScreen(uiStateHolder: HeartRateUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    HeartRateScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun HeartRateScreen(uiState: HeartRateUiState, onUiEvent: (HeartRateUiEvent) -> Unit) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_heart_rate),
        isScrollableContent = true,
        includeBottomInsets = false
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HealthScreenTitle(text = stringResource(Res.string.heart_rate))
            OutlinedTextField(
                value = if (uiState.age > 0) uiState.age.toString() else "",
                onValueChange = { onUiEvent(HeartRateUiEvent.OnAgeChanged(it.toIntOrNull() ?: 0)) },
                label = { Text(stringResource(Res.string.label_age)) }, suffix = { Text(stringResource(Res.string.unit_years)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = if (uiState.restingHr > 0) uiState.restingHr.toString() else "",
                onValueChange = { onUiEvent(HeartRateUiEvent.OnRestingHrChanged(it.toIntOrNull() ?: 0)) },
                label = { Text(stringResource(Res.string.resting_heart_rate_optional)) }, suffix = { Text(stringResource(Res.string.unit_bpm)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            HealthUnitSystemSwitch(
                label = stringResource(Res.string.tanaka_formula),
                description = if (uiState.useTanaka) stringResource(Res.string.tanaka_formula_desc) else stringResource(Res.string.standard_formula_desc),
                isMetric = uiState.useTanaka,
                onUnitSystemChange = { onUiEvent(HeartRateUiEvent.OnMethodChanged(it)) }
            )
            HealthActionButton(text = stringResource(Res.string.calculate_zones), isLoading = uiState.isLoading, onClick = { onUiEvent(HeartRateUiEvent.OnCalculate) })

            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Max Heart Rate: ${uiState.maxHr} BPM", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                uiState.zones.forEach { zone -> ZoneCard(zone) }
            }
        }
    }
}

private val zoneColors = listOf(
    Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFFFC107), Color(0xFFFF9800), Color(0xFFF44336)
)

@Composable
private fun ZoneCard(zone: HeartRateZoneCalculator.Zone) {
    val color = zoneColors.getOrElse(zone.number - 1) { Color.Gray }
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(modifier = Modifier.height(48.dp).padding(end = 4.dp).clip(RoundedCornerShape(4.dp)).background(color).padding(horizontal = 8.dp)) {
                Text("Z${zone.number}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 12.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(zone.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                Text("${zone.minBpm} - ${zone.maxBpm} BPM", style = MaterialTheme.typography.bodySmall)
                Text(zone.benefit, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text(zone.intensity, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
