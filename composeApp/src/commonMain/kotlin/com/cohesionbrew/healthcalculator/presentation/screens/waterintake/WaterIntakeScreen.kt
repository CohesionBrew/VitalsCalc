package com.cohesionbrew.healthcalculator.presentation.screens.waterintake

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.generated.resources.*
import io.github.robinpcrd.cupertino.adaptive.AdaptiveSwitch
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun WaterIntakeScreen(uiStateHolder: WaterIntakeUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    WaterIntakeScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun WaterIntakeScreen(uiState: WaterIntakeUiState, onUiEvent: (WaterIntakeUiEvent) -> Unit) {
    val activityLabels = listOf(
        stringResource(Res.string.sedentary),
        stringResource(Res.string.lightly_active),
        stringResource(Res.string.moderately_active),
        stringResource(Res.string.very_active),
        stringResource(Res.string.extra_active)
    )

    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_water_intake),
        isScrollableContent = true,
        includeBottomInsets = false
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HealthScreenTitle(text = stringResource(Res.string.daily_water_intake))

            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.weightKg),
                onValueChange = { onUiEvent(WaterIntakeUiEvent.OnWeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.weight)) },
                suffix = { Text(stringResource(Res.string.unit_kg)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Activity level slider with description
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "${stringResource(Res.string.activity_level)}: ${activityLabels.getOrElse(uiState.activityLevel - 1) { activityLabels[0] }}",
                    style = MaterialTheme.typography.titleMedium
                )

                Slider(
                    value = uiState.activityLevel.toFloat(),
                    onValueChange = { onUiEvent(WaterIntakeUiEvent.OnActivityLevelChanged(it.roundToInt())) },
                    valueRange = 1f..5f,
                    steps = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        stringResource(Res.string.sedentary),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        stringResource(Res.string.extra_active),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Hot climate toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(Res.string.hot_climate),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        stringResource(Res.string.hot_climate_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                AdaptiveSwitch(
                    checked = uiState.isHotClimate,
                    onCheckedChange = { onUiEvent(WaterIntakeUiEvent.OnClimateChanged(it)) }
                )
            }

            HealthActionButton(
                text = stringResource(Res.string.calculate),
                isLoading = uiState.isLoading,
                onClick = { onUiEvent(WaterIntakeUiEvent.OnCalculate) }
            )

            // Result card
            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))
                WaterResultCard(
                    liters = uiState.resultLiters,
                    ounces = uiState.resultOunces
                )
            }
        }
    }
}

@Composable
private fun WaterResultCard(
    liters: Double,
    ounces: Double,
    modifier: Modifier = Modifier
) {
    val glasses = (liters * 1000 / 250).toInt()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF2196F3)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(Res.string.recommended_daily_intake),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Primary value - liters
            Text(
                text = "${formatWaterValue(liters)} L",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Secondary value - ounces
            Text(
                text = "(${formatWaterValue(ounces)} fl oz)",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Glasses count
            Text(
                text = stringResource(Res.string.water_result_glasses, glasses),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun formatWaterValue(value: Double): String {
    val rounded = (value * 10).roundToInt() / 10.0
    return if (rounded == rounded.toLong().toDouble()) {
        "${rounded.toLong()}.0"
    } else {
        rounded.toString()
    }
}
