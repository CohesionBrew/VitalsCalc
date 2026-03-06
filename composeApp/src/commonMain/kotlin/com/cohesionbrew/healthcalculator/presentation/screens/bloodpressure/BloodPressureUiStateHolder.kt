package com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.domain.calculator.BloodPressureClassifier
import com.cohesionbrew.healthcalculator.domain.calculator.BpValidator
import com.cohesionbrew.healthcalculator.domain.model.history.BloodPressureHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import com.cohesionbrew.healthcalculator.domain.widget.WidgetUpdater
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BloodPressureUiStateHolder(
    private val historyRepository: HistoryRepository,
    private val widgetUpdater: WidgetUpdater
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(BloodPressureUiState())
    val uiState: StateFlow<BloodPressureUiState> = _uiState.asStateFlow()

    fun onUiEvent(event: BloodPressureUiEvent) {
        when (event) {
            is BloodPressureUiEvent.OnSystolicChanged -> _uiState.update { it.copy(systolic = event.v, validationError = null) }
            is BloodPressureUiEvent.OnDiastolicChanged -> _uiState.update { it.copy(diastolic = event.v, validationError = null) }
            is BloodPressureUiEvent.OnPulseChanged -> _uiState.update { it.copy(pulse = event.v) }
            BloodPressureUiEvent.OnClassify -> classify()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun classify() {
        val s = _uiState.value
        val error = BpValidator.validate(s.systolic, s.diastolic, if (s.pulse > 0) s.pulse else null)
        if (error != null) {
            _uiState.update { it.copy(validationError = error) }
            return
        }
        val category = BloodPressureClassifier.classify(s.systolic, s.diastolic)
        _uiState.update { it.copy(category = category, isCalculated = true, validationError = null) }

        uiStateHolderScope.launch {
            historyRepository.addEntry(
                BloodPressureHistoryEntry(
                    id = Uuid.random().toString(), primaryValue = s.systolic.toDouble(),
                    category = category.displayName, createdAt = Clock.System.now().toEpochMilliseconds(),
                    systolic = s.systolic, diastolic = s.diastolic, pulse = if (s.pulse > 0) s.pulse else null
                )
            )
            widgetUpdater.updateWidget(CalculationType.BLOOD_PRESSURE, s.systolic.toDouble(), category.displayName)
        }
    }
}
