package com.cohesionbrew.healthcalculator.presentation.screens.calculators

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import com.cohesionbrew.healthcalculator.generated.resources.Res
import com.cohesionbrew.healthcalculator.generated.resources.calc_tile_blood_pressure
import com.cohesionbrew.healthcalculator.generated.resources.calc_tile_bmi
import com.cohesionbrew.healthcalculator.generated.resources.calc_tile_bmr
import com.cohesionbrew.healthcalculator.generated.resources.calc_tile_body_fat
import com.cohesionbrew.healthcalculator.generated.resources.calc_tile_heart_rate
import com.cohesionbrew.healthcalculator.generated.resources.calc_tile_ideal_weight
import com.cohesionbrew.healthcalculator.generated.resources.calc_tile_water_intake
import com.cohesionbrew.healthcalculator.presentation.components.health.CalculatorTile
import com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure.BloodPressureScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bmi.BmiScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bmr.BmrScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bodyfat.BodyFatScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.heartrate.HeartRateScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.idealweight.IdealWeightScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.waterintake.WaterIntakeScreenRoute
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CalculatorsUiStateHolder : UiStateHolder() {
    private val _uiState = MutableStateFlow(
        CalculatorsUiState(
            tiles = listOf(
                // Order matches old VitalsCalc: measurements first, then calculations/goals
                // Row 1: BMI, Body Fat, Blood Pressure (measurement-type)
                CalculatorTile(
                    titleRes = Res.string.calc_tile_bmi,
                    icon = Icons.Filled.Home,
                    route = BmiScreenRoute()
                ),
                CalculatorTile(
                    titleRes = Res.string.calc_tile_body_fat,
                    icon = Icons.Filled.Person,
                    route = BodyFatScreenRoute()
                ),
                CalculatorTile(
                    titleRes = Res.string.calc_tile_blood_pressure,
                    icon = Icons.Filled.Favorite,
                    route = BloodPressureScreenRoute()
                ),
                // Row 2: Ideal Weight, BMR, Heart Rate (calculation/goal-type)
                CalculatorTile(
                    titleRes = Res.string.calc_tile_ideal_weight,
                    icon = Icons.Filled.Person,
                    route = IdealWeightScreenRoute()
                ),
                CalculatorTile(
                    titleRes = Res.string.calc_tile_bmr,
                    icon = Icons.Filled.Star,
                    route = BmrScreenRoute()
                ),
                CalculatorTile(
                    titleRes = Res.string.calc_tile_heart_rate,
                    icon = Icons.Filled.Favorite,
                    route = HeartRateScreenRoute()
                ),
                // Row 3: Water Intake
                CalculatorTile(
                    titleRes = Res.string.calc_tile_water_intake,
                    icon = Icons.Filled.Star,
                    route = WaterIntakeScreenRoute()
                )
            )
        )
    )
    val uiState: StateFlow<CalculatorsUiState> = _uiState.asStateFlow()

    fun onUiEvent(event: CalculatorsUiEvent) {}
}
