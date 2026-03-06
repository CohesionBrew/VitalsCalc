package com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class BloodPressureScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<BloodPressureUiStateHolder>()
        val navigator = LocalNavigator.current
        BloodPressureScreen(uiStateHolder = uiStateHolder)
    }
}
