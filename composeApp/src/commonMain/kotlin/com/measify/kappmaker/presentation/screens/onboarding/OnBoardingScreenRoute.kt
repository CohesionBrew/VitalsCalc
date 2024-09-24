package com.measify.kappmaker.presentation.screens.onboarding

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.measify.kappmaker.presentation.screens.main.MainScreenRoute
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.uiStateHolder

class OnBoardingScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val uiStateHolder = uiStateHolder<OnBoardingUiStateHolder>()
        val navigator = LocalNavigator.currentOrThrow
        OnBoardingScreen(
            uiStateHolder = uiStateHolder,
            onNavigateMain = { navigator.replace(MainScreenRoute()) }
        )
    }

}


