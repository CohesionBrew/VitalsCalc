package com.measify.kappbuilder.presentation.screens.signin

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.measify.kappbuilder.util.ScreenRoute
import com.measify.kappbuilder.generated.resources.Res
import com.measify.kappbuilder.generated.resources.title_sign_in
import org.jetbrains.compose.resources.stringResource

class SignInScreenRoute : ScreenRoute {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        SignInScreen(onSuccessfulSignIn = {
            navigator.pop()
        })
    }

    override val title: String
        @Composable
        get() = stringResource(Res.string.title_sign_in)
}