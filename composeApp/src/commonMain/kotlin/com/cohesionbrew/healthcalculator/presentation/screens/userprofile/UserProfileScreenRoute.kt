package com.cohesionbrew.healthcalculator.presentation.screens.userprofile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()

        LaunchedEffect(uiState.isSaved) {
            if (uiState.isSaved) {
                navigator.popBackStack()
            }
        }

        UserProfileScreen(uiStateHolder = uiStateHolder)
    }
}
