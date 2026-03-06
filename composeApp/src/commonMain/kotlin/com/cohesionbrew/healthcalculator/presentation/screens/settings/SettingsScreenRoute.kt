package com.cohesionbrew.healthcalculator.presentation.screens.settings

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class SettingsScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<SettingsUiStateHolder>()
        val navigator = LocalNavigator.current
        SettingsScreen(uiStateHolder = uiStateHolder)
    }
}
