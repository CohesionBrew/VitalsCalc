package com.measify.kappmaker.presentation.screens.signin

import androidx.compose.runtime.Composable
import com.measify.kappmaker.root.LocalNavigator
import com.measify.kappmaker.util.ScreenRoute
import kotlinx.serialization.Serializable

@Serializable
class SignInScreenRoute(private val isSignIn: Boolean = false) : ScreenRoute {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        SignInScreen(
            isSignIn = isSignIn,
            onSuccessfulSignIn = {
                navigator.popBackStack()
            },
            onNavigateBack = {
                navigator.popBackStack()
            },
            onNavigateSignIn = {
                navigator.navigate(SignInScreenRoute(isSignIn = true)) {
                    popUpTo(SignInScreenRoute()) {
                        inclusive = true
                    }
                }
            }
        )
    }
}