package com.measify.kappmaker.presentation.screens.profile

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.measify.kappmaker.presentation.screens.signin.SignInScreenRoute
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.navigatorUiStateHolder

class ProfileScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        /*
            Use  as below for scoping uiStateHolder to the screen instead of navigation
            val uiStateHolder = uiStateHolder<ProfileUiStateHolder>()
         */
        val uiStateHolder = navigator.navigatorUiStateHolder<ProfileUiStateHolder>()
        ProfileScreen(uiStateHolder = uiStateHolder, onSignInRequired = {
            navigator.replace(SignInScreenRoute())
        })
    }
}