package com.cohesionbrew.healthcalculator.presentation.screens.heartrate

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.domain.calculator.HeartRateZoneCalculator

@Immutable
data class HeartRateUiState(
    val age: Int = 0,
    val restingHr: Int = 0,
    val useTanaka: Boolean = false,
    val maxHr: Int = 0,
    val zones: List<HeartRateZoneCalculator.Zone> = emptyList(),
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface HeartRateUiEvent {
    data class OnAgeChanged(val age: Int) : HeartRateUiEvent
    data class OnRestingHrChanged(val hr: Int) : HeartRateUiEvent
    data class OnMethodChanged(val useTanaka: Boolean) : HeartRateUiEvent
    data object OnCalculate : HeartRateUiEvent
}
