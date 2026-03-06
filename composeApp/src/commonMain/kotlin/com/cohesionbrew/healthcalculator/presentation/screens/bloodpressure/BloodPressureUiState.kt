package com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure

import androidx.compose.runtime.Immutable
import com.cohesionbrew.healthcalculator.domain.model.BpCategory

@Immutable
data class BloodPressureUiState(
    val systolic: Int = 0,
    val diastolic: Int = 0,
    val pulse: Int = 0,
    val category: BpCategory? = null,
    val validationError: String? = null,
    val isCalculated: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface BloodPressureUiEvent {
    data class OnSystolicChanged(val v: Int) : BloodPressureUiEvent
    data class OnDiastolicChanged(val v: Int) : BloodPressureUiEvent
    data class OnPulseChanged(val v: Int) : BloodPressureUiEvent
    data object OnClassify : BloodPressureUiEvent
}
