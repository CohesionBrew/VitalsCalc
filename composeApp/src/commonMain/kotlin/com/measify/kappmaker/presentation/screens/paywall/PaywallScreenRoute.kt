package com.measify.kappmaker.presentation.screens.paywall

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.measify.kappmaker.data.source.featureflag.FeatureFlagManager
import com.measify.kappmaker.presentation.screens.paywall.remotepaywall.RemotePaywallScreen
import com.measify.kappmaker.presentation.screens.signin.SignInScreenRoute
import com.measify.kappmaker.root.LocalNavigator
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.uiStateHolder
import kotlinx.serialization.Serializable
import org.koin.compose.koinInject

@Serializable
class PaywallScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val featureFlagManager = koinInject<FeatureFlagManager>()
        val uiStateHolder = uiStateHolder<PaywallUiStateHolder>()
        if (featureFlagManager.getBoolean(FeatureFlagManager.Keys.SHOW_REMOTE_PAYWALL)) {
            Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
                RemotePaywallScreen(
                    uiStateHolder = uiStateHolder,
                    onDismiss = {
                        uiStateHolder.onPaywallDismissActionHandled()
                        navigator.popBackStack()
                    },
                    onSignInRequired = {
                        navigator.navigate(SignInScreenRoute())
                    }
                )
            }
        } else {
            PaywallScreen(
                uiStateHolder = uiStateHolder,
                onDismiss = {
                    uiStateHolder.onPaywallDismissActionHandled()
                    navigator.popBackStack()
                },
                onSignInRequired = {
                    navigator.navigate(SignInScreenRoute())
                }
            )
        }
    }

}