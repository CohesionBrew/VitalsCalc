package com.cohesionbrew.healthcalculator.presentation.screens.bmr

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthDropdownWithKeysSelector
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthGenderSelectorToggle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.SelectableItem
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.domain.calculator.TdeeCalculator
import com.cohesionbrew.healthcalculator.generated.resources.*
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun BmrScreen(uiStateHolder: BmrUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BmrScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BmrScreen(uiState: BmrUiState, onUiEvent: (BmrUiEvent) -> Unit) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_bmr),
        isScrollableContent = true,
        includeBottomInsets = false
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- Title ---
            HealthScreenTitle(text = stringResource(Res.string.bmr_calculator))

            // --- Gender selector ---
            HealthGenderSelectorToggle(
                maleLabel = stringResource(Res.string.male),
                femaleLabel = stringResource(Res.string.female),
                isMaleSelected = uiState.gender == "male",
                onMaleSelected = { onUiEvent(BmrUiEvent.OnGenderChanged("male")) },
                onFemaleSelected = { onUiEvent(BmrUiEvent.OnGenderChanged("female")) }
            )

            // --- Height ---
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.heightCm),
                onValueChange = { onUiEvent(BmrUiEvent.OnHeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.height)) },
                suffix = { Text(stringResource(Res.string.unit_cm)) },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Weight ---
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.weightKg),
                onValueChange = { onUiEvent(BmrUiEvent.OnWeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.weight)) },
                suffix = { Text(stringResource(Res.string.unit_kg)) },
                modifier = Modifier.fillMaxWidth()
            )

            // --- Age ---
            OutlinedTextField(
                value = if (uiState.age > 0) uiState.age.toString() else "",
                onValueChange = { onUiEvent(BmrUiEvent.OnAgeChanged(it.toIntOrNull() ?: 0)) },
                label = { Text(stringResource(Res.string.label_age)) },
                suffix = { Text(stringResource(Res.string.unit_years)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // --- Body fat checkbox + input ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onUiEvent(BmrUiEvent.OnKnowsBodyFatToggled) }
                    )
            ) {
                Checkbox(
                    checked = uiState.knowsBodyFat,
                    onCheckedChange = { onUiEvent(BmrUiEvent.OnKnowsBodyFatToggled) }
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(Res.string.i_know_my_body_fat),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            AnimatedVisibility(visible = uiState.knowsBodyFat) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormattedDoubleTextField(
                        value = formatDoubleDisplay(uiState.bodyFatPct),
                        onValueChange = { onUiEvent(BmrUiEvent.OnBodyFatChanged(it)) },
                        label = { Text(stringResource(Res.string.body_fat_percent)) },
                        suffix = { Text(stringResource(Res.string.unit_percent)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(Res.string.body_fat_note),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // --- Calculate button ---
            HealthActionButton(
                text = stringResource(Res.string.calculate),
                isLoading = uiState.isLoading,
                onClick = { onUiEvent(BmrUiEvent.OnCalculate) }
            )

            // ===== RESULTS SECTION =====
            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))

                // --- BMR Details Card ---
                BmrDetailsCard(uiState)

                // --- TDEE Details Card ---
                TdeeDetailsCard(uiState, onUiEvent)

                // --- Calorie Goal Card ---
                CalorieGoalCard(uiState, onUiEvent)

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ==================== BMR Details Card ====================

@Composable
private fun BmrDetailsCard(uiState: BmrUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = stringResource(Res.string.bmr_details),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            // BMR value
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.bmr),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${((uiState.bmr * 10).roundToInt() / 10.0)} kcal/day",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Formula Breakdown
            Text(
                text = stringResource(Res.string.formula_breakdown),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            uiState.formulaResults.forEach { result ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = result.formula,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${result.bmr.toInt()} kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End
                    )
                }
            }

            // Average
            if (uiState.formulaResults.size > 1) {
                HorizontalDivider()
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(Res.string.average),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${uiState.bmr.toInt()} kcal",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Text(
                text = stringResource(Res.string.note_oxford_henry_accurate),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Light
            )
        }
    }
}

// ==================== TDEE Details Card ====================

@Composable
private fun TdeeDetailsCard(uiState: BmrUiState, onUiEvent: (BmrUiEvent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = stringResource(Res.string.tdee_details),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            // Activity level dropdown
            HealthDropdownWithKeysSelector(
                label = stringResource(Res.string.activity_level),
                options = uiState.activityLevels.map { SelectableItem(it.key, it.description) },
                selectedKey = uiState.activityLevelKey,
                onItemSelected = { key, _ -> onUiEvent(BmrUiEvent.OnActivityLevelChanged(key)) },
                modifier = Modifier.fillMaxWidth()
            )

            // TDEE value
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.tdee),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${((uiState.tdee * 10).roundToInt() / 10.0)} kcal/day",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// ==================== Calorie Goal Card ====================

@Composable
private fun CalorieGoalCard(uiState: BmrUiState, onUiEvent: (BmrUiEvent) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Title
            Text(
                text = stringResource(Res.string.calorie_goals),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            // Goal dropdown
            HealthDropdownWithKeysSelector(
                label = stringResource(Res.string.calorie_goal),
                options = uiState.calorieGoals.map { SelectableItem(it.key, it.description) },
                selectedKey = uiState.calorieGoalKey,
                onItemSelected = { key, _ -> onUiEvent(BmrUiEvent.OnCalorieGoalChanged(key)) },
                modifier = Modifier.fillMaxWidth()
            )

            // Goal calories value
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.calories),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "${uiState.goalCalories} kcal/day",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

            // Macro split section
            MacroSplitSection(totalCalories = uiState.goalCalories)
        }
    }
}

// ==================== Macro Split Section ====================

private data class MacroSplit(
    val label: String,
    val proteinPercent: Int,
    val fatPercent: Int,
    val carbPercent: Int
)

@Composable
private fun MacroSplitSection(totalCalories: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(Res.string.daily_macros),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.protein_fats_carbs),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        val macroSplits = listOf(
            MacroSplit(stringResource(Res.string.moderate_carb), 30, 35, 35),
            MacroSplit(stringResource(Res.string.lower_carb), 40, 40, 20),
            MacroSplit(stringResource(Res.string.higher_carb), 30, 20, 50)
        )

        macroSplits.forEach { split ->
            val proteinGrams = (totalCalories * split.proteinPercent / 100.0 / 4).roundToInt()
            val fatGrams = (totalCalories * split.fatPercent / 100.0 / 9).roundToInt()
            val carbGrams = (totalCalories * split.carbPercent / 100.0 / 4).roundToInt()

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = split.label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            stringResource(Res.string.protein),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text("${proteinGrams}g", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            stringResource(Res.string.fats),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text("${fatGrams}g", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            stringResource(Res.string.carbs),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text("${carbGrams}g", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
