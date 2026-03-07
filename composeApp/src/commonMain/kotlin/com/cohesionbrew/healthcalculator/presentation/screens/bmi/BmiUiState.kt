package com.cohesionbrew.healthcalculator.presentation.screens.bmi

import androidx.compose.runtime.Immutable

@Immutable
data class BmiUiState(
    val heightCm: Double = 0.0,
    val weightKg: Double = 0.0,
    val useMetric: Boolean = true,
    val bmi: Double = 0.0,
    val categoryIndex: Int = -1,
    val healthyMinKg: Double = 0.0,
    val healthyMaxKg: Double = 0.0,
    val differenceFromHealthy: Double = 0.0,
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false,
    val age: Int? = null,
    val heightDisplayText: String = ""
) {
    companion object {
        val categoryNames = listOf("Underweight", "Normal", "Overweight", "Obese I", "Obese II", "Obese III")
    }
}

sealed interface BmiUiEvent {
    data class OnHeightChanged(val height: Double) : BmiUiEvent
    data class OnWeightChanged(val weight: Double) : BmiUiEvent
    data class OnUnitSystemChanged(val useMetric: Boolean) : BmiUiEvent
    data object OnCalculate : BmiUiEvent
}
