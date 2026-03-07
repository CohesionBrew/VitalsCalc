package com.cohesionbrew.healthcalculator.presentation.screens.bmi

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.domain.calculator.BmiCalculator
import com.cohesionbrew.healthcalculator.domain.model.history.BmiHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import com.cohesionbrew.healthcalculator.domain.widget.WidgetUpdater
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BmiUiStateHolder(
    private val historyRepository: HistoryRepository,
    private val userProfileRepository: UserProfileRepository,
    private val widgetUpdater: WidgetUpdater
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(BmiUiState())
    val uiState: StateFlow<BmiUiState> = _uiState.asStateFlow()

    init {
        loadDefaults()
    }

    private fun loadDefaults() = uiStateHolderScope.launch {
        val profile = userProfileRepository.getProfile() ?: return@launch

        // Calculate age from DOB
        val age = profile.dob?.let { dobStr ->
            try {
                val dob = LocalDate.parse(dobStr)
                val now = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                dob.yearsUntil(now)
            } catch (_: Exception) { null }
        }

        // Format height display
        val heightText = if (profile.useMetric) {
            "${profile.heightCm.roundToInt()} cm"
        } else {
            val totalInches = profile.heightCm / 2.54
            val feet = (totalInches / 12).toInt()
            val inches = (totalInches % 12).roundToInt()
            "$feet ft $inches in"
        }

        _uiState.update {
            it.copy(
                age = age,
                heightDisplayText = heightText,
                heightCm = profile.heightCm,
                weightKg = profile.weightKg,
                useMetric = profile.useMetric
            )
        }
    }

    fun onUiEvent(event: BmiUiEvent) {
        when (event) {
            is BmiUiEvent.OnHeightChanged -> _uiState.update { it.copy(heightCm = event.height) }
            is BmiUiEvent.OnWeightChanged -> _uiState.update { it.copy(weightKg = event.weight) }
            is BmiUiEvent.OnUnitSystemChanged -> _uiState.update { it.copy(useMetric = event.useMetric) }
            BmiUiEvent.OnCalculate -> calculate()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun calculate() {
        val state = _uiState.value
        if (state.heightCm <= 0 || state.weightKg <= 0) return

        _uiState.update { it.copy(isLoading = true) }

        val bmi = BmiCalculator.calculate(state.heightCm, state.weightKg)
        val categoryIndex = BmiCalculator.getCategory(bmi)
        val (minKg, maxKg) = BmiCalculator.getHealthyWeightRange(state.heightCm, useMetric = true)
        val diff = BmiCalculator.getDifferenceFromHealthy(state.heightCm, state.weightKg, state.useMetric)

        _uiState.update {
            it.copy(
                bmi = bmi,
                categoryIndex = categoryIndex,
                healthyMinKg = minKg,
                healthyMaxKg = maxKg,
                differenceFromHealthy = diff,
                isCalculated = true,
                isLoading = false
            )
        }

        uiStateHolderScope.launch {
            val categoryName = BmiUiState.categoryNames.getOrNull(categoryIndex)
            historyRepository.addEntry(
                BmiHistoryEntry(
                    id = Uuid.random().toString(),
                    primaryValue = ((bmi * 10).roundToInt() / 10.0),
                    category = categoryName,
                    createdAt = Clock.System.now().toEpochMilliseconds(),
                    heightCm = state.heightCm,
                    weightKg = state.weightKg,
                    healthyMinKg = minKg,
                    healthyMaxKg = maxKg
                )
            )
            widgetUpdater.updateWidget(CalculationType.BMI, bmi, categoryName)
        }
    }
}
