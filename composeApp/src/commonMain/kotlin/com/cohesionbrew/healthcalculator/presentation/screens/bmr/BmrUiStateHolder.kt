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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil
import kotlin.math.roundToInt
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

private const val KG_TO_LB = 2.20462

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

        // Calculate age from DOB
        val age = profile.dob?.let { dobStr ->
            try {
                val dob = LocalDate.parse(dobStr)
                val now = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()).date
                dob.yearsUntil(now)
            } catch (_: Exception) { null }
        } ?: 0

        // Format weight and height for subtitle display
        val weightText = if (profile.useMetric) {
            (kotlin.math.round(profile.weightKg * 10) / 10.0).toString()
        } else {
            (kotlin.math.round(profile.weightKg * KG_TO_LB * 10) / 10.0).toString()
        }

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
                gender = profile.gender,
                heightCm = profile.heightCm,
                weightKg = profile.weightKg,
                age = age,
                bodyFatPct = profile.bodyFatPct,
                knowsBodyFat = profile.bodyFatPct != null && (profile.bodyFatPct ?: 0.0) > 0,
                activityLevelKey = profile.activityLevel,
                calorieGoalKey = profile.calorieGoal,
                bmrFormula = profile.bmrFormula.let { mapProfileFormula(it) },
                useMetric = profile.useMetric,
                weightDisplayText = weightText,
                heightDisplayText = heightText
            )
        }

        // Auto-calculate if profile has valid data
        if (profile.heightCm > 0 && profile.weightKg > 0 && age > 0) {
            calculate()
        }
    }

    /** Map profile's short formula key to the full key used in dropdowns */
    private fun mapProfileFormula(profileFormula: String): String {
        return when (profileFormula) {
            "mifflin" -> "mifflin_st_jeor"
            "harris" -> "harris_benedict"
            "katch" -> "katch_mcardle"
            "oxford" -> "oxford_henry"
            "average" -> "average_of_all"
            else -> profileFormula // already a full key
        }
    }

    fun onUiEvent(event: BmrUiEvent) {
        when (event) {
            is BmrUiEvent.OnGenderChanged -> _uiState.update { it.copy(gender = event.gender) }
            is BmrUiEvent.OnHeightChanged -> _uiState.update { it.copy(heightCm = event.height) }
            is BmrUiEvent.OnWeightChanged -> _uiState.update { it.copy(weightKg = event.weight) }
            is BmrUiEvent.OnAgeChanged -> _uiState.update { it.copy(age = event.age) }
            is BmrUiEvent.OnBodyFatChanged -> {
                _uiState.update { it.copy(bodyFatPct = event.bodyFat) }
                // Re-calculate when body fat changes (affects Katch-McArdle and average)
                if (_uiState.value.isCalculated) calculate()
            }
            BmrUiEvent.OnKnowsBodyFatToggled -> {
                _uiState.update {
                    val toggled = !it.knowsBodyFat
                    it.copy(
                        knowsBodyFat = toggled,
                        bodyFatPct = if (toggled) it.bodyFatPct else null,
                        // Reset to average if Katch-McArdle was selected but body fat is unchecked
                        bmrFormula = if (!toggled && it.bmrFormula == "katch_mcardle") "average_of_all" else it.bmrFormula
                    )
                }
                // Re-calculate since body fat availability changed
                if (_uiState.value.isCalculated) calculate()
            }
            is BmrUiEvent.OnBmrFormulaChanged -> {
                _uiState.update { it.copy(bmrFormula = event.formula) }
                // Re-calculate with new formula
                if (_uiState.value.isCalculated) calculate()
            }
            is BmrUiEvent.OnActivityLevelChanged -> {
                _uiState.update { it.copy(activityLevelKey = event.key) }
                recalculateTdeeAndGoal()
            }
            is BmrUiEvent.OnCalorieGoalChanged -> {
                _uiState.update { it.copy(calorieGoalKey = event.key) }
                recalculateTdeeAndGoal()
            }
            BmrUiEvent.OnCalculate -> calculate()
        }
    }

    private fun recalculateTdeeAndGoal() {
        val s = _uiState.value
        if (!s.isCalculated || s.bmr <= 0) return
        val tdee = TdeeCalculator.calculateFromKey(s.bmr, s.activityLevelKey)
        val goalCal = TdeeCalculator.calculateGoalCaloriesFromKey(tdee, s.bmr, s.calorieGoalKey)
        _uiState.update { it.copy(tdee = tdee, goalCalories = goalCal) }
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun calculate() {
        val s = _uiState.value
        if (s.heightCm <= 0 || s.weightKg <= 0 || s.age <= 0) return
        _uiState.update { it.copy(isLoading = true) }

        val results = BmrCalculator.calculateAll(s.gender, s.weightKg, s.heightCm, s.age, s.bodyFatPct)

        // Calculate BMR based on selected formula
        val selectedBmr = when (s.bmrFormula) {
            "oxford_henry" -> BmrCalculator.calculateOxfordHenry(s.gender, s.weightKg, s.age)
            "mifflin_st_jeor" -> BmrCalculator.calculateMifflinStJeor(s.gender, s.weightKg, s.heightCm, s.age)
            "harris_benedict" -> BmrCalculator.calculateHarrisBenedict(s.gender, s.weightKg, s.heightCm, s.age)
            "katch_mcardle" -> {
                if (s.knowsBodyFat && (s.bodyFatPct ?: 0.0) > 0) {
                    BmrCalculator.calculateKatchMcArdle(s.weightKg, s.bodyFatPct!!)
                } else {
                    results.map { it.bmr }.average()
                }
            }
            else -> results.map { it.bmr }.average() // "average_of_all" or default
        }

        val tdee = TdeeCalculator.calculateFromKey(selectedBmr, s.activityLevelKey)
        val goalCal = TdeeCalculator.calculateGoalCaloriesFromKey(tdee, selectedBmr, s.calorieGoalKey)

        _uiState.update {
            it.copy(formulaResults = results, bmr = selectedBmr, tdee = tdee, goalCalories = goalCal, isCalculated = true, isLoading = false)
        }

        uiStateHolderScope.launch {
            historyRepository.addEntry(
                BmrHistoryEntry(
                    id = Uuid.random().toString(), primaryValue = ((selectedBmr * 10).roundToInt() / 10.0),
                    category = null, createdAt = Clock.System.now().toEpochMilliseconds(),
                    formula = s.bmrFormula, tdee = ((tdee * 10).roundToInt() / 10.0),
                    activityLevel = s.activityLevelKey, gender = s.gender,
                    heightCm = s.heightCm, weightKg = s.weightKg, age = s.age
                )
            )
            widgetUpdater.updateWidget(CalculationType.BMR, selectedBmr, null)
        }
    }
}
