package com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.domain.model.BpCategory
import com.cohesionbrew.healthcalculator.domain.model.history.BloodPressureHistoryEntry

@Immutable
data class BloodPressureUiState(
    val systolic: Int = 0,
    val diastolic: Int = 0,
    val pulse: Int = 0,
    val notes: String = "",
    val category: BpCategory? = null,
    val validationError: String? = null,
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false,
    val recentReadings: List<BloodPressureHistoryEntry> = emptyList(),
    val showSaveSuccess: Boolean = false
) {
    val canSave: Boolean
        get() = systolic in 40..300 && diastolic in 20..200 && systolic > diastolic
}

sealed interface BloodPressureUiEvent {
    data class OnSystolicChanged(val v: Int) : BloodPressureUiEvent
    data class OnDiastolicChanged(val v: Int) : BloodPressureUiEvent
    data class OnPulseChanged(val v: Int) : BloodPressureUiEvent
    data class OnNotesChanged(val v: String) : BloodPressureUiEvent
    data object OnSaveReading : BloodPressureUiEvent
    data object OnClearInputs : BloodPressureUiEvent
    data class OnDeleteReading(val id: String) : BloodPressureUiEvent
    data object OnDismissError : BloodPressureUiEvent
}
