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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import kotlin.math.roundToInt
import kotlin.time.Clock

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
        val dob = profile.dob?.let {
            try { LocalDate.parse(it) } catch (_: Exception) { null }
        } ?: LocalDate(1990, 1, 1)
        val age = calculateAge(dob)
        val totalInches = profile.heightCm / 2.54
        val feet = (totalInches / 12).toInt()
        val inches = (totalInches % 12).roundToInt()

        _uiState.update {
            it.copy(
                gender = profile.gender,
                dob = dob,
                age = age,
                heightCm = profile.heightCm,
                heightFeet = feet.coerceIn(3, 7).toString(),
                heightInches = inches.coerceIn(0, 11).toString(),
                useMetric = profile.useMetric,
                isLoading = false
            )
        }
    }

    fun onUiEvent(event: UserProfileUiEvent) {
        when (event) {
            is UserProfileUiEvent.OnGenderChanged -> _uiState.update {
                it.copy(gender = if (event.isMale) "male" else "female")
            }
            is UserProfileUiEvent.OnDobChanged -> {
                val age = calculateAge(event.dob)
                _uiState.update { it.copy(dob = event.dob, age = age) }
            }
            is UserProfileUiEvent.OnHeightCmChanged -> _uiState.update {
                it.copy(heightCm = event.value ?: 0.0)
            }
            is UserProfileUiEvent.OnHeightFeetChanged -> _uiState.update {
                it.copy(heightFeet = event.feet)
            }
            is UserProfileUiEvent.OnHeightInchesChanged -> _uiState.update {
                it.copy(heightInches = event.inches)
            }
            UserProfileUiEvent.OnSave -> saveProfile()
        }
    }

    private fun saveProfile() = uiStateHolderScope.launch {
        _uiState.update { it.copy(isSaving = true) }
        val state = _uiState.value
        val existing = userProfileRepository.getProfile() ?: UserProfile()

        val heightCm = if (state.useMetric) {
            state.heightCm
        } else {
            val feet = state.heightFeet.toIntOrNull() ?: 5
            val inches = state.heightInches.toIntOrNull() ?: 0
            ((feet * 12) + inches) * 2.54
        }

        userProfileRepository.saveProfile(
            existing.copy(
                gender = state.gender,
                dob = state.dob.toString(),
                heightCm = heightCm
            )
        )
        _uiState.update { it.copy(isSaving = false, isSaved = true) }
    }

    private fun calculateAge(dob: LocalDate): Int {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return dob.yearsUntil(now)
    }
}
