package com.cohesionbrew.healthcalculator.presentation.screens.about

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class AboutScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<AboutUiStateHolder>()
        val navigator = LocalNavigator.current
        AboutScreen(uiStateHolder = uiStateHolder)
    }
}
