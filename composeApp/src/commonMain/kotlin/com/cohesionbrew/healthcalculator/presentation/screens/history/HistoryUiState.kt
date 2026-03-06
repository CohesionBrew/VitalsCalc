package com.cohesionbrew.healthcalculator.presentation.screens.history

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType

@Immutable
data class HistoryUiState(
    val entries: List<CalculationEntry> = emptyList(),
    val selectedFilter: CalculationType? = null,
    val isLoading: Boolean = true,
    val isPro: Boolean = false,
    val showClearConfirmation: Boolean = false
)

sealed interface HistoryUiEvent {
    data class OnFilterSelected(val type: CalculationType?) : HistoryUiEvent
    data class OnDeleteEntry(val id: String) : HistoryUiEvent
    data object OnClearHistory : HistoryUiEvent
    data object OnConfirmClear : HistoryUiEvent
    data object OnDismissClear : HistoryUiEvent
}
