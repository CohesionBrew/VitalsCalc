package com.cohesionbrew.healthcalculator.presentation.screens.settings

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsUiStateHolder(
    private val userProfileRepository: UserProfileRepository,
    private val historyRepository: HistoryRepository
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() = uiStateHolderScope.launch {
        val profile = userProfileRepository.getProfile()
        _uiState.update { it.copy(useMetric = profile?.useMetric ?: true, isLoading = false) }
    }

    fun onUiEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnUnitSystemChanged -> {
                _uiState.update { it.copy(useMetric = event.useMetric) }
                uiStateHolderScope.launch {
                    val profile = userProfileRepository.getProfile()
                    if (profile != null) {
                        userProfileRepository.saveProfile(profile.copy(useMetric = event.useMetric))
                    }
                }
            }
            SettingsUiEvent.OnClearData -> _uiState.update { it.copy(showClearConfirmation = true) }
            SettingsUiEvent.OnConfirmClear -> {
                _uiState.update { it.copy(showClearConfirmation = false) }
                uiStateHolderScope.launch { historyRepository.clearHistory() }
            }
            SettingsUiEvent.OnDismissClear -> _uiState.update { it.copy(showClearConfirmation = false) }
        }
    }
}
