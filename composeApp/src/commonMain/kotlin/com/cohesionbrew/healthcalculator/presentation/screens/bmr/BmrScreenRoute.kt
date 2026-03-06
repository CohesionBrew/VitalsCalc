package com.cohesionbrew.healthcalculator.presentation.screens.bmr

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class BmrScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<BmrUiStateHolder>()
        val navigator = LocalNavigator.current
        BmrScreen(uiStateHolder = uiStateHolder)
    }
}
