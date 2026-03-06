package com.cohesionbrew.healthcalculator.presentation.screens.userprofile

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class UserProfileScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<UserProfileUiStateHolder>()
        val navigator = LocalNavigator.current
        UserProfileScreen(uiStateHolder = uiStateHolder)
    }
}
