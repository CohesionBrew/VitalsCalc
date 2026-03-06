package com.cohesionbrew.healthcalculator.presentation.screens.onboarding

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.presentation.screens.main.MainScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.paywall.PaywallScreenRoute
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class OnBoardingScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<OnBoardingUiStateHolder>()
        val navigator = LocalNavigator.current
        OnBoardingScreen(
            style = OnBoardingScreenStyle.STYLE1,
            uiStateHolder = uiStateHolder,
            onNavigateMain = {
                navigator.navigate(MainScreenRoute()) {
                    popUpTo(OnBoardingScreenRoute()) {
                        inclusive = true
                    }
                }
            },
            onNavigatePaywall = {
                navigator.navigate(PaywallScreenRoute())
            }
        )
    }

}


