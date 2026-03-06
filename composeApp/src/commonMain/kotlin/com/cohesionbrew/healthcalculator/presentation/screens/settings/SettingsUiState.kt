package com.cohesionbrew.healthcalculator.presentation.screens.settings

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsUiState(
    val useMetric: Boolean = true,
    val showClearConfirmation: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface SettingsUiEvent {
    data class OnUnitSystemChanged(val useMetric: Boolean) : SettingsUiEvent
    data object OnClearData : SettingsUiEvent
    data object OnConfirmClear : SettingsUiEvent
    data object OnDismissClear : SettingsUiEvent
}
