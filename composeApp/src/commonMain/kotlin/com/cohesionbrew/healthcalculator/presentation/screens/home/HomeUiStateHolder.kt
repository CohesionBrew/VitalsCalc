package com.cohesionbrew.healthcalculator.presentation.screens.home

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import com.cohesionbrew.healthcalculator.domain.premium.FeatureAccessManager
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeUiStateHolder(
    private val historyRepository: HistoryRepository,
    private val featureAccessManager: FeatureAccessManager
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadLatest()
        observeHistoryCount()
        observePro()
    }

    fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnRefresh -> loadLatest()
        }
    }

    private fun loadLatest() = uiStateHolderScope.launch {
        val bmi = historyRepository.getLatestByType(CalculationType.BMI)
        val bmr = historyRepository.getLatestByType(CalculationType.BMR)
        val bodyFat = historyRepository.getLatestByType(CalculationType.BODY_FAT)
        val bp = historyRepository.getLatestByType(CalculationType.BLOOD_PRESSURE)
        _uiState.update {
            it.copy(latestBmi = bmi, latestBmr = bmr, latestBodyFat = bodyFat, latestBp = bp, isLoading = false)
        }
    }

    private fun observeHistoryCount() = uiStateHolderScope.launch {
        historyRepository.getHistoryCount().collectLatest { count ->
            _uiState.update { it.copy(historyCount = count) }
        }
    }

    private fun observePro() = uiStateHolderScope.launch {
        featureAccessManager.isPro.collectLatest { isPro ->
            _uiState.update { it.copy(isPro = isPro) }
        }
    }
}
