package com.cohesionbrew.healthcalculator.presentation.screens.bodyfat

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.domain.model.BodyFatCategory
import com.cohesionbrew.healthcalculator.domain.model.BodyFatMethod

@Immutable
data class BodyFatUiState(
    val method: BodyFatMethod = BodyFatMethod.NAVY,
    val gender: String = "male",
    val heightCm: Double = 0.0,
    val waistCm: Double = 0.0,
    val neckCm: Double = 0.0,
    val hipCm: Double = 0.0,
    val weightKg: Double = 0.0,
    val age: Int = 0,
    val resultPercent: Double = 0.0,
    val category: BodyFatCategory? = null,
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface BodyFatUiEvent {
    data class OnMethodChanged(val method: BodyFatMethod) : BodyFatUiEvent
    data class OnGenderChanged(val gender: String) : BodyFatUiEvent
    data class OnHeightChanged(val v: Double) : BodyFatUiEvent
    data class OnWaistChanged(val v: Double) : BodyFatUiEvent
    data class OnNeckChanged(val v: Double) : BodyFatUiEvent
    data class OnHipChanged(val v: Double) : BodyFatUiEvent
    data class OnWeightChanged(val v: Double) : BodyFatUiEvent
    data class OnAgeChanged(val v: Int) : BodyFatUiEvent
    data object OnCalculate : BodyFatUiEvent
}
