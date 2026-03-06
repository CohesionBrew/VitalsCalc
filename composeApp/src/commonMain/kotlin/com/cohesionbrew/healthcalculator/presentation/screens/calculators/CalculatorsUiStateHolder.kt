package com.cohesionbrew.healthcalculator.presentation.screens.calculators

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
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
                CalculatorTile("BMI Calculator", Icons.Filled.Favorite, BmiScreenRoute()),
                CalculatorTile("BMR / TDEE", Icons.Filled.FavoriteBorder, BmrScreenRoute()),
                CalculatorTile("Heart Rate Zones", Icons.Filled.Favorite, HeartRateScreenRoute()),
                CalculatorTile("Body Fat", Icons.Filled.FavoriteBorder, BodyFatScreenRoute()),
                CalculatorTile("Ideal Weight", Icons.Filled.Favorite, IdealWeightScreenRoute()),
                CalculatorTile("Water Intake", Icons.Filled.FavoriteBorder, WaterIntakeScreenRoute()),
                CalculatorTile("Blood Pressure", Icons.Filled.Favorite, BloodPressureScreenRoute())
            )
        )
    )
    val uiState: StateFlow<CalculatorsUiState> = _uiState.asStateFlow()

    fun onUiEvent(event: CalculatorsUiEvent) {}
}
