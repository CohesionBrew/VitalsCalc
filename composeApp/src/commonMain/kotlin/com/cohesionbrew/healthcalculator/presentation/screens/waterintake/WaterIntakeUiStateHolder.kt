package com.cohesionbrew.healthcalculator.presentation.screens.waterintake

import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.domain.calculator.WaterIntakeCalculator
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WaterIntakeUiStateHolder(
    private val userProfileRepository: UserProfileRepository
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(WaterIntakeUiState())
    val uiState: StateFlow<WaterIntakeUiState> = _uiState.asStateFlow()

    init {
        loadDefaults()
    }

    private fun loadDefaults() = uiStateHolderScope.launch {
        val p = userProfileRepository.getProfile() ?: return@launch
        _uiState.update {
            it.copy(
                weightKg = p.weightKg,
                useMetric = p.useMetric
            )
        }
        // Auto-calculate if we have valid weight
        if (p.weightKg > 0) {
            calculateInternal()
        }
    }

    fun onUiEvent(event: WaterIntakeUiEvent) {
        when (event) {
            is WaterIntakeUiEvent.OnWeightChanged -> {
                _uiState.update { it.copy(weightKg = event.weight) }
                if (event.weight > 0) calculateInternal()
            }
            is WaterIntakeUiEvent.OnActivityLevelChanged -> {
                _uiState.update { it.copy(activityLevel = event.level.coerceIn(1, 5)) }
                calculateInternal()
            }
            is WaterIntakeUiEvent.OnClimateChanged -> {
                _uiState.update { it.copy(isHotClimate = event.isHot) }
                calculateInternal()
            }
            WaterIntakeUiEvent.OnCalculate -> calculateInternal()
        }
    }

    private fun calculateInternal() {
        val s = _uiState.value
        if (s.weightKg <= 0) return
        val result = WaterIntakeCalculator.calculate(s.weightKg, s.activityLevel, s.isHotClimate)
        _uiState.update {
            it.copy(
                resultLiters = result.liters,
                resultOunces = result.ounces,
                isCalculated = true
            )
        }
    }
}
