package com.cohesionbrew.healthcalculator.presentation.screens.calculators

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.presentation.components.health.CalculatorTile

@Immutable
data class CalculatorsUiState(
    val tiles: List<CalculatorTile> = emptyList()
)

sealed interface CalculatorsUiEvent
