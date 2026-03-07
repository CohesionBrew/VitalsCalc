package com.cohesionbrew.healthcalculator.presentation.screens.bmr

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthDropdownWithKeysSelector
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthInfoTooltip
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.SelectableItem
import com.cohesionbrew.healthcalculator.designsystem.components.health.TooltipMode
import com.cohesionbrew.healthcalculator.domain.calculator.BmrCalculator
import com.cohesionbrew.healthcalculator.generated.resources.*
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
private fun BmrProfileSubtitle(uiState: BmrUiState) {
    if (uiState.age <= 0) return

    val genderDisplay = if (uiState.gender.lowercase() == "male") {
        stringResource(Res.string.male)
    } else {
        stringResource(Res.string.female)
    }

    val subtitle = if (uiState.useMetric) {
        stringResource(
            Res.string.age_w_kg_h_cm,
            genderDisplay,
            uiState.age,
            uiState.weightDisplayText,
            uiState.heightDisplayText
        )
    } else {
        stringResource(
            Res.string.age_w_lbs_h_in,
            genderDisplay,
            uiState.age,
            uiState.weightDisplayText,
            uiState.heightDisplayText
        )
    }

    Text(
        text = subtitle,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
fun BmrScreen(uiStateHolder: BmrUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BmrScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BmrScreen(uiState: BmrUiState, onUiEvent: (BmrUiEvent) -> Unit) {
    val title = stringResource(Res.string.bmr_calculator)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            LandscapeLayout(title = title, uiState = uiState, onUiEvent = onUiEvent)
        } else {
            PortraitLayout(title = title, uiState = uiState, onUiEvent = onUiEvent)
        }
    }
}

// --- Portrait Layout ---

@Composable
private fun PortraitLayout(
    title: String,
    uiState: BmrUiState,
    onUiEvent: (BmrUiEvent) -> Unit
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
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            HealthScreenTitle(text = title)

            BmrProfileSubtitle(uiState)

            Spacer(modifier = Modifier.height(8.dp))
            BmrDetailsCard(uiState, onUiEvent, modifier = Modifier.fillMaxWidth())
            TdeeDetailsCard(uiState, onUiEvent, modifier = Modifier.fillMaxWidth())
            CalorieGoalCard(uiState, onUiEvent, modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- Landscape Layout ---

@Composable
private fun LandscapeLayout(
    title: String,
    uiState: BmrUiState,
    onUiEvent: (BmrUiEvent) -> Unit
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
            // Left Column: title + subtitle + BmrDetailsCard
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HealthScreenTitle(text = title)

                BmrProfileSubtitle(uiState)

                Spacer(modifier = Modifier.height(8.dp))
                BmrDetailsCard(uiState, onUiEvent, modifier = Modifier.fillMaxWidth())
            }

            // Right Column: TdeeDetailsCard + CalorieGoalCard
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TdeeDetailsCard(uiState, onUiEvent, modifier = Modifier.fillMaxWidth())
                CalorieGoalCard(uiState, onUiEvent, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

// ==================== BMR Details Card (with body fat + formula selector, matches old app) ====================

@Composable
private fun BmrDetailsCard(
    uiState: BmrUiState,
    onUiEvent: (BmrUiEvent) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    var showChartPopup by remember { mutableStateOf(false) }

    val calculatorTypes = listOf(
        SelectableItem(key = "oxford_henry", displayName = stringResource(Res.string.oxford_henry)),
        SelectableItem(key = "mifflin_st_jeor", displayName = stringResource(Res.string.mifflin_st_jeor)),
        SelectableItem(key = "harris_benedict", displayName = stringResource(Res.string.harris_benedict)),
        SelectableItem(key = "katch_mcardle", displayName = stringResource(Res.string.katch_mcardle)),
        SelectableItem(key = "average_of_all", displayName = stringResource(Res.string.average_of_all))
    )

    Column(
        modifier = modifier
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // --- Title Row ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(Res.string.bmr_details),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(8.dp))
            HealthInfoTooltip(
                mode = TooltipMode.Dialog,
                tooltipText = stringResource(Res.string.tooltiptext_knowing_bmr),
                modifier = Modifier,
                tooltipTitle = stringResource(Res.string.bmr_info)
            )
        }

        // --- Body fat checkbox (matches old app's BmrDetailsCard) ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
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
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.i_know_my_body_fat),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(8.dp))
            HealthInfoTooltip(
                mode = TooltipMode.Dialog,
                tooltipText = stringResource(Res.string.tooltiptext_body_fat),
                modifier = Modifier,
                tooltipTitle = stringResource(Res.string.body_fat_info)
            )
        }

        AnimatedVisibility(visible = uiState.knowsBodyFat) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.body_fat_note),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }

        // --- BMR Calculation formula dropdown ---
        HealthDropdownWithKeysSelector(
            label = stringResource(Res.string.bmr_calculation),
            options = calculatorTypes,
            selectedKey = uiState.bmrFormula,
            onItemSelected = { key, _ ->
                onUiEvent(BmrUiEvent.OnBmrFormulaChanged(key))
            }
        )

        // --- BMR Result Row ---
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
                text = "${uiState.bmr.toInt()} ${stringResource(Res.string.unit_kcal_day)}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { showChartPopup = true }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(Res.string.show_bmr_chart)
                )
            }
        }

        // Chart popup dialog showing all formula results
        if (showChartPopup) {
            Dialog(onDismissRequest = { showChartPopup = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    BmrComparisonChart(uiState.formulaResults)
                }
            }
        }
    }
}

// ==================== TDEE Details Card ====================

@Composable
private fun TdeeDetailsCard(
    uiState: BmrUiState,
    onUiEvent: (BmrUiEvent) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    var showTdeeGridPopup by remember { mutableStateOf(false) }

    val activityLevels = listOf(
        SelectableItem(key = "sedentary", displayName = stringResource(Res.string.sedentary)),
        SelectableItem(key = "lightly_active", displayName = stringResource(Res.string.lightly_active)),
        SelectableItem(key = "moderately_active", displayName = stringResource(Res.string.moderately_active)),
        SelectableItem(key = "very_active", displayName = stringResource(Res.string.very_active)),
        SelectableItem(key = "extra_active", displayName = stringResource(Res.string.extra_active))
    )

    Column(
        modifier = modifier
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // --- Title Row ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.tdee_details),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(8.dp))
            HealthInfoTooltip(
                mode = TooltipMode.Dialog,
                tooltipText = stringResource(Res.string.tooltiptext_tdee),
                modifier = Modifier,
                tooltipTitle = stringResource(Res.string.tdee_info)
            )
        }

        // --- Activity Level Dropdown ---
        HealthDropdownWithKeysSelector(
            label = stringResource(Res.string.activity_level),
            options = activityLevels,
            selectedKey = uiState.activityLevelKey,
            onItemSelected = { key, _ ->
                onUiEvent(BmrUiEvent.OnActivityLevelChanged(key))
            }
        )

        // --- TDEE Result Row ---
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
                text = "${uiState.tdee.toInt()} ${stringResource(Res.string.unit_kcal_day)}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { showTdeeGridPopup = true }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(Res.string.show_tdee_grid)
                )
            }
        }

        // TDEE grid popup
        if (showTdeeGridPopup) {
            Dialog(onDismissRequest = { showTdeeGridPopup = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    TdeeCalorieGrid(
                        bmr = uiState.bmr,
                        activityLevelKey = uiState.activityLevelKey,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ==================== Calorie Goal Card ====================

@Composable
private fun CalorieGoalCard(
    uiState: BmrUiState,
    onUiEvent: (BmrUiEvent) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    var showGoalGridPopup by remember { mutableStateOf(false) }
    var showMacroGridPopup by remember { mutableStateOf(false) }

    val calorieGoals = listOf(
        SelectableItem(key = "faster_weight_gain", displayName = stringResource(Res.string.faster_weight_gain)),
        SelectableItem(key = "weight_gain", displayName = stringResource(Res.string.weight_gain)),
        SelectableItem(key = "maintenance", displayName = stringResource(Res.string.maintenance)),
        SelectableItem(key = "weight_loss", displayName = stringResource(Res.string.weight_loss)),
        SelectableItem(key = "faster_weight_loss", displayName = stringResource(Res.string.faster_weight_loss))
    )

    Column(
        modifier = modifier
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        // --- Title Row ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(Res.string.calorie_goals),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(8.dp))
            HealthInfoTooltip(
                mode = TooltipMode.Dialog,
                tooltipText = stringResource(Res.string.tooltiptext_calorie_goal),
                modifier = Modifier,
                tooltipTitle = stringResource(Res.string.calorie_goal_info)
            )
        }

        // --- Goal Dropdown ---
        HealthDropdownWithKeysSelector(
            label = stringResource(Res.string.calorie_goal),
            options = calorieGoals,
            selectedKey = uiState.calorieGoalKey,
            onItemSelected = { key, _ ->
                onUiEvent(BmrUiEvent.OnCalorieGoalChanged(key))
            }
        )

        // --- Calories Result Row ---
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
                text = "${uiState.goalCalories} ${stringResource(Res.string.unit_kcal_day)}",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { showGoalGridPopup = true }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(Res.string.show_calorie_grid)
                )
            }
            Spacer(Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.show_macro_splits),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(Modifier.width(8.dp))
            IconButton(onClick = { showMacroGridPopup = true }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    tint = MaterialTheme.colorScheme.onSurface,
                    contentDescription = stringResource(Res.string.show_macro_splits)
                )
            }
        }

        // Calorie guidance grid popup
        if (showGoalGridPopup) {
            Dialog(onDismissRequest = { showGoalGridPopup = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    CalorieGuidanceGrid(
                        tdee = uiState.tdee.toInt(),
                        bmr = uiState.bmr.toInt(),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        // Macro split grid popup
        if (showMacroGridPopup) {
            Dialog(onDismissRequest = { showMacroGridPopup = false }) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    tonalElevation = 8.dp,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    MacroSplitGrid(
                        totalCalories = uiState.goalCalories,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

// ==================== BMR Comparison Chart (popup content) ====================

@Composable
private fun BmrComparisonChart(
    bmrResults: List<BmrCalculator.BmrResult>,
    containerColor: Color = MaterialTheme.colorScheme.surface
) {
    Column(
        modifier = Modifier
            .background(containerColor)
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.bmr_comparison),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))

        bmrResults.forEach { result ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = result.formula,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${result.bmr.toInt()} ${stringResource(Res.string.unit_kcal)}",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                )
            }
        }

        val average = bmrResults.map { it.bmr }.average()
        HorizontalDivider()

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.average),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${average.toInt()} ${stringResource(Res.string.unit_kcal)}",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(Res.string.note_oxford_henry_accurate),
                fontWeight = FontWeight.Light,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

// ==================== TDEE Calorie Grid (popup content) ====================

@Composable
private fun TdeeCalorieGrid(
    bmr: Double,
    activityLevelKey: String,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(containerColor)
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.calorie_recommendations),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        // Header Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                stringResource(Res.string.activity_level_desc),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                stringResource(Res.string.calories_day),
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        HorizontalDivider()

        data class ActivityLevelRow(val label: String, val key: String, val multiplier: Double)

        val activityLevels = listOf(
            ActivityLevelRow(stringResource(Res.string.bmr), "bmr", 1.0),
            ActivityLevelRow(stringResource(Res.string.sedentary), "sedentary", 1.2),
            ActivityLevelRow(stringResource(Res.string.lightly_active), "lightly_active", 1.375),
            ActivityLevelRow(stringResource(Res.string.moderately_active), "moderately_active", 1.55),
            ActivityLevelRow(stringResource(Res.string.very_active), "very_active", 1.725),
            ActivityLevelRow(stringResource(Res.string.extra_active), "extra_active", 1.9)
        )

        activityLevels.forEach { activity ->
            val calories = (bmr * activity.multiplier).toInt()
            val isSelected = activity.key == activityLevelKey

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        else Color.Transparent
                    )
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = activity.label,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "$calories ${stringResource(Res.string.unit_kcal)}",
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

// ==================== Calorie Guidance Grid (popup content) ====================

@Composable
private fun CalorieGuidanceGrid(
    tdee: Int,
    bmr: Int,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    val calorieMultipliers = listOf(
        Pair(Res.string.faster_weight_loss, 0.75),
        Pair(Res.string.weight_loss, 0.85),
        Pair(Res.string.maintenance, 1.0),
        Pair(Res.string.weight_gain, 1.10),
        Pair(Res.string.faster_weight_gain, 1.15)
    )

    val calorieTargets = calorieMultipliers.map { (labelResId, multiplier) ->
        val label = stringResource(labelResId)
        val calculatedCalories = (tdee * multiplier).toInt()
        val finalCalories = if (multiplier < 1.0) {
            maxOf(calculatedCalories, bmr)
        } else {
            calculatedCalories
        }
        Pair(label, finalCalories)
    }

    Column(
        modifier = modifier
            .background(containerColor)
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.calorie_recommendations),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(16.dp))

        Column(modifier = Modifier.padding(8.dp)) {
            calorieTargets.forEachIndexed { index, (label, calories) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        text = "$calories",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = stringResource(Res.string.unit_kcal),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                if (index < calorieTargets.size - 1) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

// ==================== Macro Split Grid (popup content) ====================

private data class MacroSplit(
    val label: String,
    val proteinPercent: Int,
    val fatPercent: Int,
    val carbPercent: Int
)

@Composable
private fun MacroSplitGrid(
    totalCalories: Int,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(containerColor)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.daily_macros),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(Res.string.protein_fats_carbs),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(16.dp))

        val macroSplits = listOf(
            MacroSplit(stringResource(Res.string.moderate_carb), 30, 35, 35),
            MacroSplit(stringResource(Res.string.lower_carb), 40, 40, 20),
            MacroSplit(stringResource(Res.string.higher_carb), 30, 20, 50)
        )

        macroSplits.forEach { split ->
            val proteinGrams = (totalCalories * split.proteinPercent / 100.0 / 4).roundToInt()
            val fatGrams = (totalCalories * split.fatPercent / 100.0 / 9).roundToInt()
            val carbGrams = (totalCalories * split.carbPercent / 100.0 / 4).roundToInt()

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = containerColor,
                tonalElevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = split.label,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                stringResource(Res.string.protein),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text("$proteinGrams g")
                        }
                        Column {
                            Text(
                                stringResource(Res.string.fats),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text("$fatGrams g")
                        }
                        Column {
                            Text(
                                stringResource(Res.string.carbs),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text("$carbGrams g")
                        }
                    }
                }
            }
        }
    }
}
