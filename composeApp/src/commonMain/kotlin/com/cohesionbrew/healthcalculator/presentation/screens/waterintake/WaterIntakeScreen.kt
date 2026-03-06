package com.cohesionbrew.healthcalculator.presentation.screens.waterintake

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import io.github.robinpcrd.cupertino.adaptive.AdaptiveSwitch
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthDropdownSelector
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun WaterIntakeScreen(uiStateHolder: WaterIntakeUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    WaterIntakeScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun WaterIntakeScreen(uiState: WaterIntakeUiState, onUiEvent: (WaterIntakeUiEvent) -> Unit) {
    val activityLabels = listOf(stringResource(Res.string.sedentary), stringResource(Res.string.lightly_active), stringResource(Res.string.moderately_active), stringResource(Res.string.very_active), stringResource(Res.string.extra_active))

    ScreenWithToolbar(title = stringResource(Res.string.title_screen_water_intake), isScrollableContent = true, includeBottomInsets = false) {
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HealthScreenTitle(text = stringResource(Res.string.daily_water_intake))
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.weightKg),
                onValueChange = { onUiEvent(WaterIntakeUiEvent.OnWeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.weight)) },
                suffix = { Text(stringResource(Res.string.unit_kg)) },
                modifier = Modifier.fillMaxWidth()
            )
            HealthDropdownSelector(
                caption = stringResource(Res.string.activity_level),
                options = activityLabels,
                selected = activityLabels.getOrElse(uiState.activityLevel - 1) { activityLabels[0] },
                onSelect = { onUiEvent(WaterIntakeUiEvent.OnActivityLevelChanged(activityLabels.indexOf(it) + 1)) }
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(Res.string.hot_climate), style = MaterialTheme.typography.bodyLarge)
                    Text(stringResource(Res.string.hot_climate_description), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                @OptIn(ExperimentalAdaptiveApi::class)
                AdaptiveSwitch(checked = uiState.isHotClimate, onCheckedChange = { onUiEvent(WaterIntakeUiEvent.OnClimateChanged(it)) })
            }
            HealthActionButton(text = stringResource(Res.string.calculate), isLoading = uiState.isLoading, onClick = { onUiEvent(WaterIntakeUiEvent.OnCalculate) })
            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(Res.string.recommended_daily_intake), style = MaterialTheme.typography.titleMedium)
                        Text("${uiState.resultLiters} L", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text("≈ ${uiState.resultOunces} fl oz", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        val glasses = ((uiState.resultLiters * 1000) / 250).toInt()
                        Text("≈ $glasses glasses (250ml each)", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
