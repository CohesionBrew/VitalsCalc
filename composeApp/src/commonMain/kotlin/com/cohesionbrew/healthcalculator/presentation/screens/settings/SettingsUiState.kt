package com.cohesionbrew.healthcalculator.presentation.screens.settings

import androidx.compose.runtime.Immutable

@Immutable
data class SettingsUiState(
    val useMetric: Boolean = true,
    val advancedMode: Boolean = false,
    val language: String = "en",
    val isSaving: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface SettingsUiEvent {
    data class OnUseMetricChanged(val useMetric: Boolean) : SettingsUiEvent
    data class OnAdvancedModeChanged(val enabled: Boolean) : SettingsUiEvent
    data class OnLanguageChanged(val code: String) : SettingsUiEvent
    data object OnSave : SettingsUiEvent
}
