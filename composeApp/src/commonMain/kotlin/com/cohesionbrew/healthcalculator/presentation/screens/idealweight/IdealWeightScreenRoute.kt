package com.cohesionbrew.healthcalculator.presentation.screens.idealweight

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class IdealWeightScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<IdealWeightUiStateHolder>()
        val navigator = LocalNavigator.current
        IdealWeightScreen(uiStateHolder = uiStateHolder)
    }
}
