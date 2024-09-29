package com.measify.kappmaker.presentation.screens.paywall

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.measify.kappmaker.presentation.screens.paywall.remotepaywall.RemotePaywallScreen
import com.measify.kappmaker.util.Constants
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.uiStateHolder

class PaywallScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        if (Constants.SHOW_REMOTE_PAYWALL) {
            RemotePaywallScreen(onDismiss = { navigator.pop() })
        } else {
            val uiStateHolder = uiStateHolder<PaywallUiStateHolder>()
            PaywallScreen(
                uiStateHolder = uiStateHolder,
                onDismiss = { navigator.pop() }
            )
        }
    }

}


