package com.cohesionbrew.healthcalculator.presentation.screens.idealweight

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.domain.model.IdealWeightResults

@Immutable
data class IdealWeightUiState(
    val heightCm: Double = 0.0,
    val gender: String = "male",
    val useMetric: Boolean = true,
    val results: IdealWeightResults? = null,
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface IdealWeightUiEvent {
    data class OnHeightChanged(val height: Double) : IdealWeightUiEvent
    data class OnGenderChanged(val gender: String) : IdealWeightUiEvent
    data class OnUnitSystemChanged(val useMetric: Boolean) : IdealWeightUiEvent
    data object OnCalculate : IdealWeightUiEvent
}
