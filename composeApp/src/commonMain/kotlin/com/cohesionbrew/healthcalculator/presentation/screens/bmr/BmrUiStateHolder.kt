package com.cohesionbrew.healthcalculator.presentation.screens.bmr

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.domain.calculator.BmrCalculator
import com.cohesionbrew.healthcalculator.domain.calculator.TdeeCalculator
import com.cohesionbrew.healthcalculator.domain.model.history.BmrHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.CalculationType
import com.cohesionbrew.healthcalculator.domain.widget.WidgetUpdater
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class BmrUiStateHolder(
    private val historyRepository: HistoryRepository,
    private val userProfileRepository: UserProfileRepository,
    private val widgetUpdater: WidgetUpdater
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(BmrUiState())
    val uiState: StateFlow<BmrUiState> = _uiState.asStateFlow()

    init {
        loadDefaults()
    }

    private fun loadDefaults() = uiStateHolderScope.launch {
        val profile = userProfileRepository.getProfile() ?: return@launch
        _uiState.update {
            it.copy(
                gender = profile.gender,
                heightCm = profile.heightCm,
                weightKg = profile.weightKg,
                bodyFatPct = profile.bodyFatPct,
                activityLevelKey = profile.activityLevel,
                calorieGoalKey = profile.calorieGoal
            )
        }
    }

    fun onUiEvent(event: BmrUiEvent) {
        when (event) {
            is BmrUiEvent.OnGenderChanged -> _uiState.update { it.copy(gender = event.gender) }
            is BmrUiEvent.OnHeightChanged -> _uiState.update { it.copy(heightCm = event.height) }
            is BmrUiEvent.OnWeightChanged -> _uiState.update { it.copy(weightKg = event.weight) }
            is BmrUiEvent.OnAgeChanged -> _uiState.update { it.copy(age = event.age) }
            is BmrUiEvent.OnBodyFatChanged -> _uiState.update { it.copy(bodyFatPct = event.bodyFat) }
            is BmrUiEvent.OnActivityLevelChanged -> _uiState.update { it.copy(activityLevelKey = event.key) }
            is BmrUiEvent.OnCalorieGoalChanged -> _uiState.update { it.copy(calorieGoalKey = event.key) }
            BmrUiEvent.OnCalculate -> calculate()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun calculate() {
        val s = _uiState.value
        if (s.heightCm <= 0 || s.weightKg <= 0 || s.age <= 0) return
        _uiState.update { it.copy(isLoading = true) }

        val results = BmrCalculator.calculateAll(s.gender, s.weightKg, s.heightCm, s.age, s.bodyFatPct)
        val avgBmr = results.map { it.bmr }.average()
        val tdee = TdeeCalculator.calculateFromKey(avgBmr, s.activityLevelKey)
        val goalCal = TdeeCalculator.calculateGoalCaloriesFromKey(tdee, avgBmr, s.calorieGoalKey)

        _uiState.update {
            it.copy(formulaResults = results, bmr = avgBmr, tdee = tdee, goalCalories = goalCal, isCalculated = true, isLoading = false)
        }

        uiStateHolderScope.launch {
            historyRepository.addEntry(
                BmrHistoryEntry(
                    id = Uuid.random().toString(), primaryValue = ((avgBmr * 10).roundToInt() / 10.0),
                    category = null, createdAt = Clock.System.now().toEpochMilliseconds(),
                    formula = "average", tdee = ((tdee * 10).roundToInt() / 10.0),
                    activityLevel = s.activityLevelKey, gender = s.gender,
                    heightCm = s.heightCm, weightKg = s.weightKg, age = s.age
                )
            )
            widgetUpdater.updateWidget(CalculationType.BMR, avgBmr, null)
        }
    }
}
