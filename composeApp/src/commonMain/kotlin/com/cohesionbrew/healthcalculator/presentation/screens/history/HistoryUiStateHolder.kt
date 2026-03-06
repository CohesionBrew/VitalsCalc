package com.cohesionbrew.healthcalculator.presentation.screens.history

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import com.cohesionbrew.healthcalculator.domain.premium.FeatureAccessManager
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HistoryUiStateHolder(
    private val historyRepository: HistoryRepository,
    private val featureAccessManager: FeatureAccessManager
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()
    private var historyJob: Job? = null

    init {
        observePro()
        observeHistory()
    }

    private fun observePro() = uiStateHolderScope.launch {
        featureAccessManager.isPro.collectLatest { pro ->
            _uiState.update { it.copy(isPro = pro) }
            observeHistory()
        }
    }

    private fun observeHistory() {
        historyJob?.cancel()
        historyJob = uiStateHolderScope.launch {
            val isPro = _uiState.value.isPro
            val daysLimit = featureAccessManager.getHistoryDaysLimit(isPro)
            val filter = _uiState.value.selectedFilter
            val flow = if (filter != null) historyRepository.getHistoryByType(filter)
            else historyRepository.getHistory(daysLimit)
            flow.collectLatest { entries ->
                _uiState.update { it.copy(entries = entries, isLoading = false) }
            }
        }
    }

    fun onUiEvent(event: HistoryUiEvent) {
        when (event) {
            is HistoryUiEvent.OnFilterSelected -> {
                _uiState.update { it.copy(selectedFilter = event.type, isLoading = true) }
                observeHistory()
            }
            is HistoryUiEvent.OnDeleteEntry -> uiStateHolderScope.launch { historyRepository.deleteEntry(event.id) }
            HistoryUiEvent.OnClearHistory -> _uiState.update { it.copy(showClearConfirmation = true) }
            HistoryUiEvent.OnConfirmClear -> {
                _uiState.update { it.copy(showClearConfirmation = false) }
                uiStateHolderScope.launch { historyRepository.clearHistory() }
            }
            HistoryUiEvent.OnDismissClear -> _uiState.update { it.copy(showClearConfirmation = false) }
        }
    }
}
