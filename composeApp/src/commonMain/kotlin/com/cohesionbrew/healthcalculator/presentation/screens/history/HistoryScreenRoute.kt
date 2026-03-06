package com.cohesionbrew.healthcalculator.presentation.screens.history

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class HistoryScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<HistoryUiStateHolder>()
        val navigator = LocalNavigator.current
        HistoryScreen(uiStateHolder = uiStateHolder)
    }
}
