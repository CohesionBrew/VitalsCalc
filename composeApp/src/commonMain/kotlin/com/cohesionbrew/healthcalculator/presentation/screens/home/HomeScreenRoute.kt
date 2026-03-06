package com.cohesionbrew.healthcalculator.presentation.screens.home

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class HomeScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<HomeUiStateHolder>()
        HomeScreen(uiStateHolder = uiStateHolder)
    }
}
