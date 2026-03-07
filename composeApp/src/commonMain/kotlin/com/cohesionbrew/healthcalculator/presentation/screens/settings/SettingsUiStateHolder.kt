package com.cohesionbrew.healthcalculator.presentation.screens.settings

import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.domain.model.UserProfile
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsUiStateHolder(
    private val userProfileRepository: UserProfileRepository
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() = uiStateHolderScope.launch {
        val profile = userProfileRepository.getProfile() ?: UserProfile()
        _uiState.update {
            it.copy(
                useMetric = profile.useMetric,
                advancedMode = profile.advancedMode,
                language = profile.language,
                isLoading = false
            )
        }
    }

    fun onUiEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnUseMetricChanged -> _uiState.update { it.copy(useMetric = event.useMetric) }
            is SettingsUiEvent.OnAdvancedModeChanged -> _uiState.update { it.copy(advancedMode = event.enabled) }
            is SettingsUiEvent.OnLanguageChanged -> _uiState.update { it.copy(language = event.code) }
            SettingsUiEvent.OnSave -> saveSettings()
        }
    }

    private fun saveSettings() = uiStateHolderScope.launch {
        _uiState.update { it.copy(isSaving = true) }
        val state = _uiState.value
        val existing = userProfileRepository.getProfile() ?: UserProfile()
        userProfileRepository.saveProfile(
            existing.copy(
                useMetric = state.useMetric,
                advancedMode = state.advancedMode,
                language = state.language
            )
        )
        _uiState.update { it.copy(isSaving = false) }
    }
}
