package com.cohesionbrew.healthcalculator.presentation.screens.references

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class ReferencesScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<ReferencesUiStateHolder>()
        val navigator = LocalNavigator.current
        ReferencesScreen(uiStateHolder = uiStateHolder)
    }
}
