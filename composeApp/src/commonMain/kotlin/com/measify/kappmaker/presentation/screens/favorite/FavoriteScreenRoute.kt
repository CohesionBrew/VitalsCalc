package com.measify.kappmaker.presentation.screens.favorite

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.measify.kappmaker.presentation.screens.paywall.PaywallScreenRoute
import com.measify.kappmaker.util.ScreenRoute

class FavoriteScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        FavoriteScreen(onPaymentRequired = { navigator.push(PaywallScreenRoute()) })
    }
}