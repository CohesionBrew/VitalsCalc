package com.cohesionbrew.healthcalculator.presentation.screens.bodyfat

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class BodyFatScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<BodyFatUiStateHolder>()
        val navigator = LocalNavigator.current
        BodyFatScreen(uiStateHolder = uiStateHolder)
    }
}
