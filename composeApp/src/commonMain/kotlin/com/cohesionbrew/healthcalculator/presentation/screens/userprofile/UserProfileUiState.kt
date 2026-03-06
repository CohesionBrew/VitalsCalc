package com.cohesionbrew.healthcalculator.presentation.screens.userprofile

import androidx.compose.runtime.Immutable

@Immutable
data class UserProfileUiState(
    val gender: String = "male",
    val heightCm: Double? = null,
    val weightKg: Double? = null,
    val restingHr: Double? = null,
    val useMetric: Boolean = true,
    val isSaved: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface UserProfileUiEvent {
    data class OnGenderChanged(val isMale: Boolean) : UserProfileUiEvent
    data class OnHeightChanged(val value: Double?) : UserProfileUiEvent
    data class OnWeightChanged(val value: Double?) : UserProfileUiEvent
    data class OnRestingHrChanged(val value: Double?) : UserProfileUiEvent
    data class OnUseMetricChanged(val value: Boolean) : UserProfileUiEvent
    data object OnSave : UserProfileUiEvent
}
