package com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.domain.calculator.BloodPressureClassifier
import com.cohesionbrew.healthcalculator.domain.calculator.BpValidator
import com.cohesionbrew.healthcalculator.domain.model.history.BloodPressureHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import com.cohesionbrew.healthcalculator.domain.widget.WidgetUpdater
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.delay
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

    init {
        loadRecentReadings()
    }

    fun onUiEvent(event: BloodPressureUiEvent) {
        when (event) {
            is BloodPressureUiEvent.OnSystolicChanged -> onSystolicChanged(event.v)
            is BloodPressureUiEvent.OnDiastolicChanged -> onDiastolicChanged(event.v)
            is BloodPressureUiEvent.OnPulseChanged -> _uiState.update { it.copy(pulse = event.v) }
            is BloodPressureUiEvent.OnNotesChanged -> _uiState.update { it.copy(notes = event.v) }
            BloodPressureUiEvent.OnSaveReading -> saveReading()
            BloodPressureUiEvent.OnClearInputs -> clearInputs()
            is BloodPressureUiEvent.OnDeleteReading -> deleteReading(event.id)
            BloodPressureUiEvent.OnDismissError -> _uiState.update { it.copy(validationError = null) }
        }
    }

    private fun onSystolicChanged(value: Int) {
        _uiState.update { it.copy(systolic = value, validationError = null) }
        classifyRealTime()
    }

    private fun onDiastolicChanged(value: Int) {
        _uiState.update { it.copy(diastolic = value, validationError = null) }
        classifyRealTime()
    }

    private fun classifyRealTime() {
        val s = _uiState.value
        if (s.systolic in 40..300 && s.diastolic in 20..200 && s.systolic > s.diastolic) {
            val category = BloodPressureClassifier.classify(s.systolic, s.diastolic)
            _uiState.update { it.copy(category = category, isCalculated = true) }
        } else {
            _uiState.update { it.copy(category = null, isCalculated = false) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun saveReading() {
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
                    id = Uuid.random().toString(),
                    primaryValue = s.systolic.toDouble(),
                    category = category.displayName,
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    systolic = s.systolic,
                    diastolic = s.diastolic,
                    pulse = if (s.pulse > 0) s.pulse else null,
                    notes = s.notes.ifBlank { null }
                )
            )
            widgetUpdater.updateWidget(CalculationType.BLOOD_PRESSURE, s.systolic.toDouble(), category.displayName)
            _uiState.update { it.copy(showSaveSuccess = true) }
            delay(2000L)
            _uiState.update { it.copy(showSaveSuccess = false) }
        }
    }

    private fun clearInputs() {
        _uiState.update {
            it.copy(
                systolic = 0,
                diastolic = 0,
                pulse = 0,
                notes = "",
                category = null,
                isCalculated = false,
                validationError = null
            )
        }
    }

    private fun deleteReading(id: String) {
        uiStateHolderScope.launch {
            historyRepository.deleteEntry(id)
        }
    }

    private fun loadRecentReadings() {
        uiStateHolderScope.launch {
            historyRepository.getHistoryByType(CalculationType.BLOOD_PRESSURE).collect { entries ->
                val bpEntries = entries.filterIsInstance<BloodPressureHistoryEntry>()
                _uiState.update { it.copy(recentReadings = bpEntries) }
            }
        }
    }
}
