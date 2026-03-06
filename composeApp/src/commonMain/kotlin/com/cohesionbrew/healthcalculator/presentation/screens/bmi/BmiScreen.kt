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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthUnitSystemSwitch
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.generated.resources.*
import com.cohesionbrew.healthcalculator.presentation.components.health.BmiIndicatorBar
import com.cohesionbrew.healthcalculator.presentation.components.health.getBmiCategoryColor
import com.cohesionbrew.healthcalculator.presentation.screens.bmi.components.BmiDynamicGraphicsChart
import kotlin.math.abs
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
                Spacer(modifier = Modifier.height(8.dp))
                BmiDynamicGraphicsChart(currentBmi = uiState.bmi)
            }
        }
    }
}

@Composable
private fun BmiResultCard(uiState: BmiUiState) {
    val unknownCategory = stringResource(Res.string.unknown_category)
    val categoryName = BmiUiState.categoryNames.getOrElse(uiState.categoryIndex) { unknownCategory }
    val categoryColor = getBmiCategoryColor(uiState.categoryIndex)
    val unit = if (uiState.useMetric) stringResource(Res.string.unit_kg) else stringResource(Res.string.unit_lbs)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Two-column layout: BMI on left, Weight Status on right
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Left column: Your BMI + value
                Column {
                    Text(
                        text = stringResource(Res.string.your_bmi),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = ((uiState.bmi * 10).roundToInt() / 10.0).toString(),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Right column: Weight Status + colored category + weight diff
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.weight_status),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = categoryName,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = categoryColor,
                        textAlign = TextAlign.End
                    )

                    if (uiState.differenceFromHealthy != 0.0) {
                        val diffDisplay = ((abs(uiState.differenceFromHealthy) * 10).roundToInt() / 10.0)
                        val diffText = if (uiState.differenceFromHealthy > 0) {
                            stringResource(Res.string.bmi_diff_to_lose, diffDisplay.toString(), unit)
                        } else {
                            stringResource(Res.string.bmi_diff_to_gain, diffDisplay.toString(), unit)
                        }
                        Text(
                            text = diffText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End
                        )
                    } else {
                        Text(
                            text = stringResource(Res.string.at_or_below_healthy_weight),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.End
                        )
                    }
                }
            }

            // BmiIndicatorBar
            BmiIndicatorBar(bmiValue = uiState.bmi, categoryLabel = categoryName)

            // Healthy weight range centered below
            Text(
                text = stringResource(Res.string.healthy_weight_range),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            val minDisplay = ((uiState.healthyMinKg * 10).roundToInt() / 10.0)
            val maxDisplay = ((uiState.healthyMaxKg * 10).roundToInt() / 10.0)
            Text(
                text = "$minDisplay $unit - $maxDisplay $unit",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
