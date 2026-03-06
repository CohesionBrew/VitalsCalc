package com.cohesionbrew.healthcalculator.presentation.screens.heartrate

import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.domain.calculator.HeartRateZoneCalculator
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HeartRateUiStateHolder(
    private val userProfileRepository: UserProfileRepository
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(HeartRateUiState())
    val uiState: StateFlow<HeartRateUiState> = _uiState.asStateFlow()

    init {
        loadDefaults()
    }

    private fun loadDefaults() = uiStateHolderScope.launch {
        val profile = userProfileRepository.getProfile() ?: return@launch
        _uiState.update { it.copy(restingHr = profile.restingHr) }
    }

    fun onUiEvent(event: HeartRateUiEvent) {
        when (event) {
            is HeartRateUiEvent.OnAgeChanged -> _uiState.update { it.copy(age = event.age) }
            is HeartRateUiEvent.OnRestingHrChanged -> {
                val error = validateRestingHr(event.hr)
                _uiState.update { it.copy(restingHr = event.hr, restingHrError = error) }
                autoRecalculateIfNeeded()
            }
            is HeartRateUiEvent.OnMethodChanged -> _uiState.update { it.copy(useTanaka = event.useTanaka) }
            HeartRateUiEvent.OnKnowsRestingHrToggled -> {
                _uiState.update { state ->
                    val toggled = !state.knowsRestingHr
                    state.copy(
                        knowsRestingHr = toggled,
                        restingHr = if (!toggled) 0 else state.restingHr,
                        restingHrError = null
                    )
                }
                autoRecalculateIfNeeded()
            }
            HeartRateUiEvent.OnCalculate -> calculate()
        }
    }

    private fun validateRestingHr(hr: Int): String? {
        if (hr <= 0) return null
        return if (hr < 20 || hr > 100) "Please enter a value between 20 and 100" else null
    }

    private fun autoRecalculateIfNeeded() {
        val s = _uiState.value
        if (s.isCalculated && s.knowsRestingHr && s.restingHr in 20..100 && s.restingHrError == null) {
            calculate()
        } else if (s.isCalculated && !s.knowsRestingHr) {
            calculate()
        }
    }

    private fun calculate() {
        val s = _uiState.value
        if (s.age <= 0) return

        val maxHr = if (s.useTanaka) HeartRateZoneCalculator.calculateMaxHeartRateTanaka(s.age)
        else HeartRateZoneCalculator.calculateMaxHeartRate(s.age)

        val restingHr = if (s.knowsRestingHr && s.restingHr in 20..100) s.restingHr else null
        val zones = HeartRateZoneCalculator.calculateZones(s.age, restingHr, s.useTanaka)

        _uiState.update { it.copy(maxHr = maxHr, zones = zones, isCalculated = true) }
    }
}
