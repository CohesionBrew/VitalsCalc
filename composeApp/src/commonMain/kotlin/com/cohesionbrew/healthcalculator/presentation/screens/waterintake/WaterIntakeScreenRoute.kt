package com.cohesionbrew.healthcalculator.presentation.screens.waterintake

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class WaterIntakeScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<WaterIntakeUiStateHolder>()
        val navigator = LocalNavigator.current
        WaterIntakeScreen(uiStateHolder = uiStateHolder)
    }
}
