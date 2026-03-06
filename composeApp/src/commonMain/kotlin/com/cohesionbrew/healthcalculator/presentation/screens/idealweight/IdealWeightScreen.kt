package com.cohesionbrew.healthcalculator.presentation.screens.idealweight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthGenderSelectorToggle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthUnitSystemSwitch
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.domain.model.IdealWeightResults
import com.cohesionbrew.healthcalculator.generated.resources.*
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun IdealWeightScreen(uiStateHolder: IdealWeightUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    IdealWeightScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun IdealWeightScreen(uiState: IdealWeightUiState, onUiEvent: (IdealWeightUiEvent) -> Unit) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_ideal_weight),
        isScrollableContent = true,
        includeBottomInsets = false
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HealthScreenTitle(text = stringResource(Res.string.idealweight_title))

            HealthGenderSelectorToggle(
                maleLabel = stringResource(Res.string.male),
                femaleLabel = stringResource(Res.string.female),
                isMaleSelected = uiState.gender == "male",
                onMaleSelected = { onUiEvent(IdealWeightUiEvent.OnGenderChanged("male")) },
                onFemaleSelected = { onUiEvent(IdealWeightUiEvent.OnGenderChanged("female")) }
            )

            HealthUnitSystemSwitch(
                label = stringResource(Res.string.unit_system),
                description = if (uiState.useMetric) stringResource(Res.string.metric_cm_kg) else stringResource(Res.string.imperial_in_lbs),
                isMetric = uiState.useMetric,
                onUnitSystemChange = { onUiEvent(IdealWeightUiEvent.OnUnitSystemChanged(it)) }
            )

            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.heightCm),
                onValueChange = { onUiEvent(IdealWeightUiEvent.OnHeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.height)) },
                suffix = { Text(if (uiState.useMetric) stringResource(Res.string.unit_cm) else stringResource(Res.string.unit_in)) },
                modifier = Modifier.fillMaxWidth()
            )

            HealthActionButton(
                text = stringResource(Res.string.calculate),
                isLoading = uiState.isLoading,
                onClick = { onUiEvent(IdealWeightUiEvent.OnCalculate) }
            )

            uiState.results?.let { results ->
                Spacer(modifier = Modifier.height(8.dp))
                FormulaComparisonCard(
                    results = results,
                    useMetric = uiState.useMetric
                )
            }
        }
    }
}

@Composable
private fun FormulaComparisonCard(
    results: IdealWeightResults,
    useMetric: Boolean,
    modifier: Modifier = Modifier
) {
    val unit = if (useMetric) "kg" else "lbs"
    val conversionFactor = if (useMetric) 1.0 else 2.20462

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.idealweight_result_title),
                style = MaterialTheme.typography.titleMedium
            )

            // Average result - highlighted
            Text(
                text = "${formatWeight(results.averageKg * conversionFactor)} $unit",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            // Range display
            val rangeMin = formatWeight(results.minKg * conversionFactor)
            val rangeMax = formatWeight(results.maxKg * conversionFactor)
            Text(
                text = "Range: $rangeMin - $rangeMax $unit",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Individual formula results
            FormulaRow(
                label = stringResource(Res.string.idealweight_robinson),
                value = formatWeight(results.robinsonKg * conversionFactor),
                unit = unit
            )
            FormulaRow(
                label = stringResource(Res.string.idealweight_miller),
                value = formatWeight(results.millerKg * conversionFactor),
                unit = unit
            )
            FormulaRow(
                label = stringResource(Res.string.idealweight_devine),
                value = formatWeight(results.devineKg * conversionFactor),
                unit = unit
            )
            FormulaRow(
                label = stringResource(Res.string.idealweight_hamwi),
                value = formatWeight(results.hamwiKg * conversionFactor),
                unit = unit
            )
        }
    }
}

@Composable
private fun FormulaRow(
    label: String,
    value: String,
    unit: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = "$value $unit",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatWeight(weight: Double): String {
    val rounded = (weight * 10).roundToInt() / 10.0
    return if (rounded == rounded.toLong().toDouble()) {
        "${rounded.toLong()}.0"
    } else {
        rounded.toString()
    }
}
