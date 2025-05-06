package com.measify.kappmaker.presentation.screens.home

import androidx.compose.runtime.Composable
import com.measify.kappmaker.util.ScreenRoute
import kotlinx.serialization.Serializable

@Serializable
class HomeScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        HomeScreen()
    }
}