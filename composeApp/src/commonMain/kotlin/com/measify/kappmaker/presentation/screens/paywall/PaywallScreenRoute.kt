package com.measify.kappmaker.presentation.screens.paywall

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.measify.kappmaker.data.source.featureflag.FeatureFlagManager
import com.measify.kappmaker.presentation.screens.paywall.remotepaywall.RemotePaywallScreen
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.uiStateHolder
import org.koin.compose.koinInject

class PaywallScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val featureFlagManager = koinInject<FeatureFlagManager>()
        if (featureFlagManager.getBoolean(FeatureFlagManager.Keys.SHOW_REMOTE_PAYWALL)) {
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


