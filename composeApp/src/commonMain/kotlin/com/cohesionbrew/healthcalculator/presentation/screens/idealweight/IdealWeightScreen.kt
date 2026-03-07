package com.cohesionbrew.healthcalculator.presentation.screens.idealweight

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
    val title = stringResource(Res.string.idealweight_title)

    // Build subtitle showing profile data: "Male, 175 cm" (matches old app)
    val genderText = if (uiState.gender == "male") stringResource(Res.string.male) else stringResource(Res.string.female)
    val heightText = if (uiState.useMetric) {
        "${uiState.heightCm.toInt()} ${stringResource(Res.string.unit_cm)}"
    } else {
        val totalInches = uiState.heightCm / 2.54
        val feet = (totalInches / 12).toInt()
        val inches = (totalInches % 12).toInt()
        "$feet' $inches\""
    }
    val subtitle = "$genderText, $heightText"

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            LandscapeLayout(
                title = title,
                subtitle = subtitle,
                uiState = uiState,
                onUiEvent = onUiEvent
            )
        } else {
            PortraitLayout(
                title = title,
                subtitle = subtitle,
                uiState = uiState,
                onUiEvent = onUiEvent
            )
        }
    }
}

// --- Portrait Layout (matches old StandardHealthScaffold PortraitLayout) ---

@Composable
private fun PortraitLayout(
    title: String,
    subtitle: String,
    uiState: IdealWeightUiState,
    onUiEvent: (IdealWeightUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HealthScreenTitle(text = title)

            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onBackground
            )

            // --- Input Section ---
            IdealWeightInputSection(uiState = uiState, onUiEvent = onUiEvent)

            // --- Results Section ---
            uiState.results?.let { results ->
                FormulaComparisonCard(
                    results = results,
                    useMetric = uiState.useMetric,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        // Ad banner slot would go here if needed
    }
}

// --- Landscape Layout (matches old StandardHealthScaffold LandscapeLayout) ---

@Composable
private fun LandscapeLayout(
    title: String,
    subtitle: String,
    uiState: IdealWeightUiState,
    onUiEvent: (IdealWeightUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column: title + subtitle + inputs + button
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HealthScreenTitle(text = title)

                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onBackground
                )

                IdealWeightInputSection(uiState = uiState, onUiEvent = onUiEvent)
            }

            // Right Column: results
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                uiState.results?.let { results ->
                    FormulaComparisonCard(
                        results = results,
                        useMetric = uiState.useMetric,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        // Ad banner slot would go here if needed
    }
}

// ==================== Input Section ====================

@Composable
private fun IdealWeightInputSection(
    uiState: IdealWeightUiState,
    onUiEvent: (IdealWeightUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // --- Gender selector ---
        HealthGenderSelectorToggle(
            maleLabel = stringResource(Res.string.male),
            femaleLabel = stringResource(Res.string.female),
            isMaleSelected = uiState.gender == "male",
            onMaleSelected = { onUiEvent(IdealWeightUiEvent.OnGenderChanged("male")) },
            onFemaleSelected = { onUiEvent(IdealWeightUiEvent.OnGenderChanged("female")) }
        )

        // --- Unit system switch ---
        HealthUnitSystemSwitch(
            label = stringResource(Res.string.unit_system),
            description = if (uiState.useMetric) stringResource(Res.string.metric_cm_kg) else stringResource(Res.string.imperial_in_lbs),
            isMetric = uiState.useMetric,
            onUnitSystemChange = { onUiEvent(IdealWeightUiEvent.OnUnitSystemChanged(it)) }
        )

        // --- Height ---
        FormattedDoubleTextField(
            value = formatDoubleDisplay(uiState.heightCm),
            onValueChange = { onUiEvent(IdealWeightUiEvent.OnHeightChanged(it ?: 0.0)) },
            label = { Text(stringResource(Res.string.height)) },
            suffix = { Text(if (uiState.useMetric) stringResource(Res.string.unit_cm) else stringResource(Res.string.unit_in)) },
            modifier = Modifier.fillMaxWidth()
        )

        // --- Calculate button ---
        HealthActionButton(
            text = stringResource(Res.string.calculate),
            isLoading = uiState.isLoading,
            onClick = { onUiEvent(IdealWeightUiEvent.OnCalculate) }
        )
    }
}

// ==================== FormulaComparisonCard (matches old app) ====================

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
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(Res.string.idealweight_result_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Average result - highlighted
            val averageWeight = formatWeight(results.averageKg * conversionFactor)
            Text(
                text = "$averageWeight $unit",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Range display (always shown, matches old app)
            val rangeMin = formatWeight(results.minKg * conversionFactor)
            val rangeMax = formatWeight(results.maxKg * conversionFactor)
            Text(
                text = "${stringResource(Res.string.idealweight_average)} ($rangeMin - $rangeMax $unit)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(8.dp))

            // Individual formula results (always shown, matches old app advanced mode)
            Text(
                text = "Individual Formula Results",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Formula comparison rows
            FormulaRow(
                label = stringResource(Res.string.idealweight_robinson),
                value = formatWeight(results.robinsonKg * conversionFactor),
                unit = unit,
                year = "1983"
            )
            FormulaRow(
                label = stringResource(Res.string.idealweight_miller),
                value = formatWeight(results.millerKg * conversionFactor),
                unit = unit,
                year = "1983"
            )
            FormulaRow(
                label = stringResource(Res.string.idealweight_devine),
                value = formatWeight(results.devineKg * conversionFactor),
                unit = unit,
                year = "1974"
            )
            FormulaRow(
                label = stringResource(Res.string.idealweight_hamwi),
                value = formatWeight(results.hamwiKg * conversionFactor),
                unit = unit,
                year = "1964"
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Educational note about formula limitations (matches old app)
            Text(
                text = "Note: These formulas were developed in 1964-1983 and don't account for body composition, muscle mass, or ethnic variations.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun FormulaRow(
    label: String,
    value: String,
    unit: String,
    year: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = year,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
        Text(
            text = "$value $unit",
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
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
