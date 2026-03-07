package com.cohesionbrew.healthcalculator.presentation.screens.home

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
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
    private val featureAccessManager: FeatureAccessManager,
    private val userProfileRepository: UserProfileRepository
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadLatest()
        loadProfile()
        observeHistoryCount()
        observePro()
    }

    fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnRefresh -> loadLatest()
            is HomeUiEvent.OnNavigateToCalculator -> Unit // handled by UI layer
        }
    }

    private fun loadLatest() = uiStateHolderScope.launch {
        val weight = historyRepository.getLatestByType(CalculationType.WEIGHT)
        val bmi = historyRepository.getLatestByType(CalculationType.BMI)
        val bodyFat = historyRepository.getLatestByType(CalculationType.BODY_FAT)
        val bp = historyRepository.getLatestByType(CalculationType.BLOOD_PRESSURE)
        val idealWeight = historyRepository.getLatestByType(CalculationType.IDEAL_WEIGHT)
        val bmr = historyRepository.getLatestByType(CalculationType.BMR)
        val waterIntake = historyRepository.getLatestByType(CalculationType.WATER_INTAKE)
        _uiState.update {
            it.copy(
                latestWeight = weight,
                latestBmi = bmi,
                latestBodyFat = bodyFat,
                latestBp = bp,
                latestIdealWeight = idealWeight,
                latestBmr = bmr,
                latestWaterIntake = waterIntake,
                isLoading = false
            )
        }
    }

    private fun loadProfile() = uiStateHolderScope.launch {
        val profile = userProfileRepository.getProfile()
        if (profile != null) {
            _uiState.update { it.copy(useMetric = profile.useMetric) }
        }
    }

    private fun observeHistoryCount() = uiStateHolderScope.launch {
        historyRepository.getHistoryCount().collectLatest { count ->
            _uiState.update { it.copy(historyCount = count) }
            // Re-fetch latest entries and profile whenever history changes
            loadLatest()
            loadProfile()
        }
    }

    private fun observePro() = uiStateHolderScope.launch {
        featureAccessManager.isPro.collectLatest { isPro ->
            _uiState.update { it.copy(isPro = isPro) }
        }
    }
}
