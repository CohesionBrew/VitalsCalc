package com.cohesionbrew.healthcalculator.presentation.screens.bmr

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.domain.calculator.BmrCalculator
import com.cohesionbrew.healthcalculator.domain.calculator.TdeeCalculator

@Immutable
data class BmrUiState(
    val gender: String = "male",
    val heightCm: Double = 0.0,
    val weightKg: Double = 0.0,
    val age: Int = 0,
    val bodyFatPct: Double? = null,
    val knowsBodyFat: Boolean = false,
    val activityLevelKey: String = "sedentary",
    val calorieGoalKey: String = "maintenance",
    val formulaResults: List<BmrCalculator.BmrResult> = emptyList(),
    val bmr: Double = 0.0,
    val tdee: Double = 0.0,
    val goalCalories: Int = 0,
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false
) {
    val activityLevels: List<TdeeCalculator.ActivityLevel> get() = TdeeCalculator.ActivityLevel.entries
    val calorieGoals: List<TdeeCalculator.CalorieGoal> get() = TdeeCalculator.CalorieGoal.entries
}

sealed interface BmrUiEvent {
    data class OnGenderChanged(val gender: String) : BmrUiEvent
    data class OnHeightChanged(val height: Double) : BmrUiEvent
    data class OnWeightChanged(val weight: Double) : BmrUiEvent
    data class OnAgeChanged(val age: Int) : BmrUiEvent
    data class OnBodyFatChanged(val bodyFat: Double?) : BmrUiEvent
    data object OnKnowsBodyFatToggled : BmrUiEvent
    data class OnActivityLevelChanged(val key: String) : BmrUiEvent
    data class OnCalorieGoalChanged(val key: String) : BmrUiEvent
    data object OnCalculate : BmrUiEvent
}
