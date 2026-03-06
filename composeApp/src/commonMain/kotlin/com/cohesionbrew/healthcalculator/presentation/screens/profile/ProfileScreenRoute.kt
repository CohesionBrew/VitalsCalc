package com.cohesionbrew.healthcalculator.presentation.screens.profile

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.presentation.screens.signin.SignInScreenRoute
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.navigatorUiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class ProfileScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        /*
            Use  as below for scoping uiStateHolder to the screen instead of navigation
            val uiStateHolder = uiStateHolder<ProfileUiStateHolder>()
         */
        val uiStateHolder = navigator.navigatorUiStateHolder<ProfileUiStateHolder>()
        ProfileScreen(
            uiStateHolder = uiStateHolder,
            onSignInRequired = {
                navigator.navigate(SignInScreenRoute()) {
                    popUpTo(ProfileScreenRoute()) {
                        inclusive = true
                    }
                }
            },
            onNavigateToBack = { navigator.popBackStack() }
        )
    }
}