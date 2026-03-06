package com.cohesionbrew.healthcalculator.presentation.screens.subscriptions

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.presentation.screens.paywall.PaywallScreenRoute
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class SubscriptionsScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<SubscriptionsUiStateHolder>()
        val navigator = LocalNavigator.current
        SubscriptionsScreen(
            uiStateHolder = uiStateHolder,
            onClickBack = { navigator.popBackStack() },
            onNavigatePaywall = {
                navigator.navigate(PaywallScreenRoute())
            })
    }
}