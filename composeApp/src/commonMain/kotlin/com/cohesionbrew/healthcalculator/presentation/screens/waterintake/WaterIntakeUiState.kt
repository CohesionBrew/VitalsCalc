package com.cohesionbrew.healthcalculator.presentation.screens.waterintake

import androidx.compose.runtime.Immutable

@Immutable
data class WaterIntakeUiState(
    val weightKg: Double = 0.0,
    val activityLevel: Int = 3, // Default to moderate (matches old app)
    val isHotClimate: Boolean = false,
    val useMetric: Boolean = true,
    val resultLiters: Double = 0.0,
    val resultOunces: Double = 0.0,
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false
) {
    val activityLevelDescription: String
        get() = when (activityLevel) {
            1 -> "Sedentary"
            2 -> "Lightly Active"
            3 -> "Moderately Active"
            4 -> "Very Active"
            5 -> "Extremely Active"
            else -> "Moderate"
        }
}

sealed interface WaterIntakeUiEvent {
    data class OnWeightChanged(val weight: Double) : WaterIntakeUiEvent
    data class OnActivityLevelChanged(val level: Int) : WaterIntakeUiEvent
    data class OnClimateChanged(val isHot: Boolean) : WaterIntakeUiEvent
    data object OnCalculate : WaterIntakeUiEvent
}
