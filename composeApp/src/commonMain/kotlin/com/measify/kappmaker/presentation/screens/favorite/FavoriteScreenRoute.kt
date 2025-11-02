package com.measify.kappmaker.presentation.screens.favorite

import androidx.compose.runtime.Composable
import com.measify.kappmaker.presentation.screens.paywall.PaywallScreenRoute
import com.measify.kappmaker.root.LocalNavigator
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class FavoriteScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<FavoriteUiStateHolder>()
        val navigator = LocalNavigator.current
        FavoriteScreen(
            uiStateHolder = uiStateHolder,
            onPaymentRequired = {
                navigator.navigate(
                    PaywallScreenRoute()
                )
            }
        )
    }
}