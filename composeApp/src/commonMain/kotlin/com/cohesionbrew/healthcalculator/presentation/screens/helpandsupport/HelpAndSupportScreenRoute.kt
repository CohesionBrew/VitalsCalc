package com.cohesionbrew.healthcalculator.presentation.screens.helpandsupport

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import kotlinx.serialization.Serializable

@Serializable
class HelpAndSupportScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        HelpAndSupportScreen(onNavigateBack = { navigator.popBackStack() })
    }

}