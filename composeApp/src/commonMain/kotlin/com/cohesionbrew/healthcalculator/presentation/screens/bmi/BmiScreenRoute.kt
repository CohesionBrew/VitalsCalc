package com.cohesionbrew.healthcalculator.presentation.screens.bmi

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class BmiScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<BmiUiStateHolder>()
        val navigator = LocalNavigator.current
        BmiScreen(uiStateHolder = uiStateHolder)
    }
}
