package com.cohesionbrew.healthcalculator.presentation.screens.idealweight

import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.domain.calculator.IdealWeightCalculator
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class IdealWeightUiStateHolder(
    private val userProfileRepository: UserProfileRepository
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(IdealWeightUiState())
    val uiState: StateFlow<IdealWeightUiState> = _uiState.asStateFlow()

    init {
        loadDefaults()
    }

    private fun loadDefaults() = uiStateHolderScope.launch {
        val p = userProfileRepository.getProfile() ?: return@launch
        _uiState.update { it.copy(heightCm = p.heightCm, gender = p.gender, useMetric = p.useMetric) }
    }

    fun onUiEvent(event: IdealWeightUiEvent) {
        when (event) {
            is IdealWeightUiEvent.OnHeightChanged -> _uiState.update { it.copy(heightCm = event.height) }
            is IdealWeightUiEvent.OnGenderChanged -> _uiState.update { it.copy(gender = event.gender) }
            is IdealWeightUiEvent.OnUnitSystemChanged -> _uiState.update { it.copy(useMetric = event.useMetric) }
            IdealWeightUiEvent.OnCalculate -> calculate()
        }
    }

    private fun calculate() {
        val s = _uiState.value
        if (s.heightCm <= 0) return
        val results = IdealWeightCalculator.calculateAll(s.heightCm, s.gender == "male")
        _uiState.update { it.copy(results = results, isCalculated = true) }
    }
}
