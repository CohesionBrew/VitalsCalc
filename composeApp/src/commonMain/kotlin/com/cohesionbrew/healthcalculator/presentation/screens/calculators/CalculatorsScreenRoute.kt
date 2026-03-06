package com.cohesionbrew.healthcalculator.presentation.screens.calculators

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class CalculatorsScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<CalculatorsUiStateHolder>()
        val navigator = LocalNavigator.current
        CalculatorsScreen(
            uiStateHolder = uiStateHolder,
            onNavigateToCalculator = { route -> navigator.navigate(route) }
        )
    }
}
