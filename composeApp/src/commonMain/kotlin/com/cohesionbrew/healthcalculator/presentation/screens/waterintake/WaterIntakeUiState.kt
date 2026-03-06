package com.cohesionbrew.healthcalculator.presentation.screens.waterintake

import androidx.compose.runtime.Immutable

@Immutable
data class WaterIntakeUiState(
    val weightKg: Double = 0.0,
    val activityLevel: Int = 1,
    val isHotClimate: Boolean = false,
    val resultLiters: Double = 0.0,
    val resultOunces: Double = 0.0,
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface WaterIntakeUiEvent {
    data class OnWeightChanged(val weight: Double) : WaterIntakeUiEvent
    data class OnActivityLevelChanged(val level: Int) : WaterIntakeUiEvent
    data class OnClimateChanged(val isHot: Boolean) : WaterIntakeUiEvent
    data object OnCalculate : WaterIntakeUiEvent
}
