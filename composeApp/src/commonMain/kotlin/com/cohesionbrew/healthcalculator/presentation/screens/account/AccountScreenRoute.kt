package com.cohesionbrew.healthcalculator.presentation.screens.account

import androidx.compose.runtime.Composable
import com.cohesionbrew.healthcalculator.presentation.screens.helpandsupport.HelpAndSupportScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.paywall.PaywallScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.profile.ProfileScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.signin.SignInScreenRoute
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.presentation.screens.subscriptions.SubscriptionsScreenRoute
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class AccountScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<AccountUiStateHolder>()
        val navigator = LocalNavigator.current
        AccountScreen(
            uiStateHolder = uiStateHolder,
            onNavigateHelpAndSupport = {
                navigator.navigate(HelpAndSupportScreenRoute())
            },
            onNavigatePaywall = {
                navigator.navigate(PaywallScreenRoute())
            },
            onNavigateSignIn = {
                navigator.navigate(SignInScreenRoute())
            },
            onNavigateProfile = {
                navigator.navigate(ProfileScreenRoute())
            },
            onNavigateSubscriptions = {
                navigator.navigate(SubscriptionsScreenRoute())
            }
        )
    }

}