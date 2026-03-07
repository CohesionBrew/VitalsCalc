package com.cohesionbrew.healthcalculator.presentation.screens.userprofile

import androidx.compose.runtime.Immutable
import kotlinx.datetime.LocalDate

@Immutable
data class UserProfileUiState(
    val gender: String = "male",
    val dob: LocalDate = LocalDate(1990, 1, 1),
    val age: Int = 0,
    val heightCm: Double = 0.0,
    val heightFeet: String = "5",
    val heightInches: String = "7",
    val useMetric: Boolean = true,
    val isSaving: Boolean = false,
    val isLoading: Boolean = true
)

sealed interface UserProfileUiEvent {
    data class OnGenderChanged(val isMale: Boolean) : UserProfileUiEvent
    data class OnDobChanged(val dob: LocalDate) : UserProfileUiEvent
    data class OnHeightCmChanged(val value: Double?) : UserProfileUiEvent
    data class OnHeightFeetChanged(val feet: String) : UserProfileUiEvent
    data class OnHeightInchesChanged(val inches: String) : UserProfileUiEvent
    data object OnSave : UserProfileUiEvent
}
