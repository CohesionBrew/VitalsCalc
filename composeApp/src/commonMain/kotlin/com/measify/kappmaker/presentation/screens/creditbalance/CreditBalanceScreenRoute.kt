package com.measify.kappmaker.presentation.screens.creditbalance

import androidx.compose.runtime.Composable
import com.measify.kappmaker.presentation.screens.paywall.PaywallScreenRoute
import com.measify.kappmaker.root.LocalNavigator
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class CreditBalanceScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<CreditBalanceUiStateHolder>()
        val navigator = LocalNavigator.current
        CreditBalanceScreen(
            uiStateHolder = uiStateHolder,
            onPurchaseRequired = {
                navigator.navigate(PaywallScreenRoute())
            },
            onClickBack = {
                navigator.popBackStack()
            }
        )
    }
}