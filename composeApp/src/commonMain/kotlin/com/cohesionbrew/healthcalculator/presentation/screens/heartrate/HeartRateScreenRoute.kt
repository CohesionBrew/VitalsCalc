package com.cohesionbrew.healthcalculator.presentation.screens.heartrate

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class HeartRateScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<HeartRateUiStateHolder>()
        val navigator = LocalNavigator.current
        HeartRateScreen(uiStateHolder = uiStateHolder)
    }
}
