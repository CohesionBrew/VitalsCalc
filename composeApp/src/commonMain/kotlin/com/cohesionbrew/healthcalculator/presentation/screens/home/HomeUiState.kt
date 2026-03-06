package com.cohesionbrew.healthcalculator.presentation.screens.home

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationEntry

@Immutable
data class HomeUiState(
    val latestBmi: CalculationEntry? = null,
    val latestBmr: CalculationEntry? = null,
    val latestBodyFat: CalculationEntry? = null,
    val latestBp: CalculationEntry? = null,
    val historyCount: Int = 0,
    val isPro: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface HomeUiEvent {
    data object OnRefresh : HomeUiEvent
}
