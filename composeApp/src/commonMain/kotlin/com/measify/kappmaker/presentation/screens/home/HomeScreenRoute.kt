package com.measify.kappmaker.presentation.screens.home

import androidx.compose.runtime.Composable
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class HomeScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<HomeUiStateHolder>()
        HomeScreen(uiStateHolder = uiStateHolder)
    }
}