package com.cohesionbrew.healthcalculator.presentation.screens.bmi

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthUnitSystemSwitch
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.generated.resources.*
import com.cohesionbrew.healthcalculator.presentation.components.health.BmiIndicatorBar
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun BmiScreen(uiStateHolder: BmiUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BmiScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BmiScreen(
    uiState: BmiUiState,
    onUiEvent: (BmiUiEvent) -> Unit
) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_bmi),
        isScrollableContent = true,
        includeBottomInsets = false
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HealthScreenTitle(text = stringResource(Res.string.body_mass_index))

            HealthUnitSystemSwitch(
                label = stringResource(Res.string.unit_system),
                description = if (uiState.useMetric) stringResource(Res.string.metric_cm_kg) else stringResource(Res.string.imperial_in_lbs),
                isMetric = uiState.useMetric,
                onUnitSystemChange = { onUiEvent(BmiUiEvent.OnUnitSystemChanged(it)) }
            )

            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.heightCm),
                onValueChange = { onUiEvent(BmiUiEvent.OnHeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.height)) },
                suffix = { Text(if (uiState.useMetric) stringResource(Res.string.unit_cm) else stringResource(Res.string.unit_in)) },
                modifier = Modifier.fillMaxWidth()
            )

            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.weightKg),
                onValueChange = { onUiEvent(BmiUiEvent.OnWeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.weight)) },
                suffix = { Text(if (uiState.useMetric) stringResource(Res.string.unit_kg) else stringResource(Res.string.unit_lbs)) },
                modifier = Modifier.fillMaxWidth()
            )

            HealthActionButton(
                text = stringResource(Res.string.calculate_bmi),
                isLoading = uiState.isLoading,
                onClick = { onUiEvent(BmiUiEvent.OnCalculate) }
            )

            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))
                BmiResultCard(uiState)
            }
        }
    }
}

@Composable
private fun BmiResultCard(uiState: BmiUiState) {
    val unknownCategory = stringResource(Res.string.unknown_category)
    val categoryName = BmiUiState.categoryNames.getOrElse(uiState.categoryIndex) { unknownCategory }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(stringResource(Res.string.your_bmi), style = MaterialTheme.typography.titleMedium)
            Text(
                ((uiState.bmi * 10).roundToInt() / 10.0).toString(),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(categoryName, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            BmiIndicatorBar(bmiValue = uiState.bmi, categoryLabel = categoryName)

            Spacer(modifier = Modifier.height(4.dp))
            Text(stringResource(Res.string.healthy_weight_range), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            val unit = if (uiState.useMetric) stringResource(Res.string.unit_kg) else stringResource(Res.string.unit_lbs)
            val minDisplay = ((uiState.healthyMinKg * 10).roundToInt() / 10.0)
            val maxDisplay = ((uiState.healthyMaxKg * 10).roundToInt() / 10.0)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("$minDisplay $unit", style = MaterialTheme.typography.bodyMedium)
                Text("$maxDisplay $unit", style = MaterialTheme.typography.bodyMedium)
            }

            if (uiState.differenceFromHealthy != 0.0) {
                val diffDisplay = ((kotlin.math.abs(uiState.differenceFromHealthy) * 10).roundToInt() / 10.0)
                val direction = if (uiState.differenceFromHealthy > 0) "to lose" else "to gain"
                Text(
                    "$diffDisplay $unit $direction to reach healthy range",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
