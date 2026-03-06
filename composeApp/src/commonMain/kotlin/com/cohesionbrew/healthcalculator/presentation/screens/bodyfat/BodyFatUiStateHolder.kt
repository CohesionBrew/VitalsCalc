package com.cohesionbrew.healthcalculator.presentation.screens.bodyfat

import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.domain.calculator.BodyFatCalculator
import com.cohesionbrew.healthcalculator.domain.model.BodyFatMethod
import com.cohesionbrew.healthcalculator.domain.model.history.BodyFatHistoryEntry
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

class BodyFatUiStateHolder(
    private val historyRepository: HistoryRepository,
    private val userProfileRepository: UserProfileRepository,
    private val widgetUpdater: WidgetUpdater
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(BodyFatUiState())
    val uiState: StateFlow<BodyFatUiState> = _uiState.asStateFlow()

    init {
        loadDefaults()
    }

    private fun loadDefaults() = uiStateHolderScope.launch {
        val p = userProfileRepository.getProfile() ?: return@launch
        _uiState.update { it.copy(gender = p.gender, heightCm = p.heightCm, waistCm = p.waistCm, neckCm = p.neckCm, hipCm = p.hipCm, weightKg = p.weightKg) }
    }

    fun onUiEvent(event: BodyFatUiEvent) {
        when (event) {
            is BodyFatUiEvent.OnMethodChanged -> _uiState.update { it.copy(method = event.method) }
            is BodyFatUiEvent.OnGenderChanged -> _uiState.update { it.copy(gender = event.gender) }
            is BodyFatUiEvent.OnHeightChanged -> _uiState.update { it.copy(heightCm = event.v) }
            is BodyFatUiEvent.OnWaistChanged -> _uiState.update { it.copy(waistCm = event.v) }
            is BodyFatUiEvent.OnNeckChanged -> _uiState.update { it.copy(neckCm = event.v) }
            is BodyFatUiEvent.OnHipChanged -> _uiState.update { it.copy(hipCm = event.v) }
            is BodyFatUiEvent.OnWeightChanged -> _uiState.update { it.copy(weightKg = event.v) }
            is BodyFatUiEvent.OnAgeChanged -> _uiState.update { it.copy(age = event.v) }
            BodyFatUiEvent.OnCalculate -> calculate()
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun calculate() {
        val s = _uiState.value
        val isMale = s.gender == "male"
        val result = when (s.method) {
            BodyFatMethod.NAVY -> BodyFatCalculator.calculateNavy(isMale, s.waistCm, s.neckCm, s.heightCm, s.hipCm)
            BodyFatMethod.RFM -> BodyFatCalculator.calculateRfm(isMale, s.heightCm, s.waistCm)
        }
        if (result <= 0) return
        val category = BodyFatCalculator.getCategory(result, isMale)
        _uiState.update { it.copy(resultPercent = result, category = category, isCalculated = true) }

        uiStateHolderScope.launch {
            historyRepository.addEntry(
                BodyFatHistoryEntry(
                    id = Uuid.random().toString(), primaryValue = ((result * 10).roundToInt() / 10.0),
                    category = category.name, createdAt = Clock.System.now().toEpochMilliseconds(),
                    method = s.method.name, isMale = isMale, waistCm = s.waistCm, neckCm = s.neckCm,
                    hipCm = s.hipCm, heightCm = s.heightCm, weightKg = s.weightKg, age = s.age
                )
            )
            widgetUpdater.updateWidget(CalculationType.BODY_FAT, result, category.name)
        }
    }
}
