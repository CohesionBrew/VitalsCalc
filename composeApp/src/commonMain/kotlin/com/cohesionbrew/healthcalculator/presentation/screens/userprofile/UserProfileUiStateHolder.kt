package com.cohesionbrew.healthcalculator.presentation.screens.userprofile

import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.domain.model.UserProfile
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserProfileUiStateHolder(
    private val userProfileRepository: UserProfileRepository
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(UserProfileUiState())
    val uiState: StateFlow<UserProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() = uiStateHolderScope.launch {
        val profile = userProfileRepository.getProfile() ?: UserProfile()
        _uiState.update {
            it.copy(
                gender = profile.gender,
                heightCm = profile.heightCm.takeIf { h -> h > 0 },
                weightKg = profile.weightKg.takeIf { w -> w > 0 },
                restingHr = profile.restingHr.takeIf { r -> r > 0 }?.toDouble(),
                useMetric = profile.useMetric,
                isLoading = false
            )
        }
    }

    fun onUiEvent(event: UserProfileUiEvent) {
        when (event) {
            is UserProfileUiEvent.OnGenderChanged -> _uiState.update {
                it.copy(gender = if (event.isMale) "male" else "female", isSaved = false)
            }
            is UserProfileUiEvent.OnHeightChanged -> _uiState.update { it.copy(heightCm = event.value, isSaved = false) }
            is UserProfileUiEvent.OnWeightChanged -> _uiState.update { it.copy(weightKg = event.value, isSaved = false) }
            is UserProfileUiEvent.OnRestingHrChanged -> _uiState.update { it.copy(restingHr = event.value, isSaved = false) }
            is UserProfileUiEvent.OnUseMetricChanged -> _uiState.update { it.copy(useMetric = event.value, isSaved = false) }
            UserProfileUiEvent.OnSave -> saveProfile()
        }
    }

    private fun saveProfile() = uiStateHolderScope.launch {
        val state = _uiState.value
        val existing = userProfileRepository.getProfile() ?: UserProfile()
        val updated = existing.copy(
            gender = state.gender,
            heightCm = state.heightCm ?: 0.0,
            weightKg = state.weightKg ?: 0.0,
            restingHr = state.restingHr?.toInt() ?: 0,
            useMetric = state.useMetric
        )
        userProfileRepository.saveProfile(updated)
        _uiState.update { it.copy(isSaved = true) }
    }
}
