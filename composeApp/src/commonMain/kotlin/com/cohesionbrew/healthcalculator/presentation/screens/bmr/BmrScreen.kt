package com.cohesionbrew.healthcalculator.presentation.screens.bmr

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HealthScreenTitle(text = stringResource(Res.string.bmr_calculator))
            HealthGenderSelectorToggle(
                maleLabel = stringResource(Res.string.male),
                femaleLabel = stringResource(Res.string.female),
                isMaleSelected = uiState.gender == "male",
                onMaleSelected = { onUiEvent(BmrUiEvent.OnGenderChanged("male")) },
                onFemaleSelected = { onUiEvent(BmrUiEvent.OnGenderChanged("female")) }
            )
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.heightCm),
                onValueChange = { onUiEvent(BmrUiEvent.OnHeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.height)) },
                suffix = { Text(stringResource(Res.string.unit_cm)) },
                modifier = Modifier.fillMaxWidth()
            )
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.weightKg),
                onValueChange = { onUiEvent(BmrUiEvent.OnWeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.weight)) },
                suffix = { Text(stringResource(Res.string.unit_kg)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = if (uiState.age > 0) uiState.age.toString() else "",
                onValueChange = { onUiEvent(BmrUiEvent.OnAgeChanged(it.toIntOrNull() ?: 0)) },
                label = { Text(stringResource(Res.string.label_age)) }, suffix = { Text(stringResource(Res.string.unit_years)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true, modifier = Modifier.fillMaxWidth()
            )
            HealthDropdownWithKeysSelector(
                label = stringResource(Res.string.activity_level),
                options = uiState.activityLevels.map { SelectableItem(it.key, it.description) },
                selectedKey = uiState.activityLevelKey,
                onItemSelected = { key, _ -> onUiEvent(BmrUiEvent.OnActivityLevelChanged(key)) }
            )
            HealthDropdownWithKeysSelector(
                label = stringResource(Res.string.calorie_goal),
                options = uiState.calorieGoals.map { SelectableItem(it.key, it.description) },
                selectedKey = uiState.calorieGoalKey,
                onItemSelected = { key, _ -> onUiEvent(BmrUiEvent.OnCalorieGoalChanged(key)) }
            )
            HealthActionButton(text = stringResource(Res.string.calculate), isLoading = uiState.isLoading, onClick = { onUiEvent(BmrUiEvent.OnCalculate) })

            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(stringResource(Res.string.results), style = MaterialTheme.typography.titleMedium)
                        ResultRow(stringResource(Res.string.average_bmr), "${((uiState.bmr * 10).roundToInt() / 10.0)} kcal/day")
                        ResultRow(stringResource(Res.string.tdee), "${((uiState.tdee * 10).roundToInt() / 10.0)} kcal/day", MaterialTheme.colorScheme.primary)
                        ResultRow(stringResource(Res.string.goal_calories), "${uiState.goalCalories} kcal/day")
                        HorizontalDivider()
                        Text(stringResource(Res.string.formula_breakdown), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        uiState.formulaResults.forEach { r ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(r.formula, style = MaterialTheme.typography.bodySmall)
                                Text("${((r.bmr * 10).roundToInt() / 10.0)} kcal", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, valueColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Unspecified) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = valueColor)
    }
}
