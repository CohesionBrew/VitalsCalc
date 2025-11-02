package com.measify.kappmaker.presentation.screens.signin

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.measify.kappmaker.data.repository.UserRepository
import com.measify.kappmaker.designsystem.components.AgreePrivacyPolicyTermsConditionsText
import com.measify.kappmaker.designsystem.components.LogoImage
import com.measify.kappmaker.designsystem.components.ScreenWithToolbar
import com.measify.kappmaker.designsystem.generated.resources.UiRes
import com.measify.kappmaker.designsystem.generated.resources.ic_back
import com.measify.kappmaker.designsystem.theme.AppTheme
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.sign_in_to
import com.measify.kappmaker.generated.resources.title_sign_in
import com.measify.kappmaker.generated.resources.txt_main_action_to_sign_in
import com.measify.kappmaker.presentation.components.AuthUIHelperButtons
import com.measify.kappmaker.root.AppGlobalUiState
import com.measify.kappmaker.util.Constants
import com.measify.kappmaker.util.UiMessage
import com.measify.kappmaker.util.logging.AppLogger
import dev.gitlive.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@Composable
fun SignInScreen(
    isSignIn: Boolean,
    modifier: Modifier = Modifier,
    onSuccessfulSignIn: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateSignIn: () -> Unit = {},
) {

    val userRepository = koinInject<UserRepository>()

    val scrollState = rememberScrollState()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(500))
    }
    val coroutineScope = rememberCoroutineScope()
    ScreenWithToolbar(
        modifier = modifier.fillMaxSize().background(AppTheme.colors.background),
        title = stringResource(Res.string.title_sign_in),
        includeBottomInsets = true,
        isScrollableContent = true,
        navigationIcon = UiRes.drawable.ic_back,
        onNavigationIconClick = { onNavigateBack() }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            LogoImage(
                modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp)
                    .padding(AppTheme.spacing.defaultSpacing)
            )
            Spacer(modifier = Modifier.weight(1f))

            TitleText(modifier = Modifier.padding(top = AppTheme.spacing.largeSpacing))

            AuthUIHelperButtons(
                linkAccount = isSignIn.not(),
                modifier = Modifier.padding(top = AppTheme.spacing.largeSpacing).fillMaxWidth()
            ) { result ->
                result.onSuccess {
                    AppLogger.d("Successful sign in")
                    userRepository.onSuccessfulOauthSign()
                    onSuccessfulSignIn()
                }.onFailure { error ->

                    var errorMessage = error.message ?: ""
                    //TODO check if it is already existing or collision error. Checking with empty message is not reliable
                    val isUserAlreadyRegistered =
                        error is FirebaseAuthUserCollisionException || errorMessage.isEmpty()

                    if (isUserAlreadyRegistered) {
                        errorMessage =
                            "Looks like you already have an account with this email. Please, try again signing in"
                    }
                    coroutineScope.launch {
                        AppGlobalUiState.showUiMessage(
                            UiMessage.Message(errorMessage)
                        )
                    }
                    AppLogger.e("Error occurred while signing in, $error")
                    if (isUserAlreadyRegistered) onNavigateSignIn()
                }
            }


            AgreePrivacyPolicyTermsConditionsText(
                modifier = Modifier.padding(top = AppTheme.spacing.largeSpacing).fillMaxWidth(),
                privacyPolicyUrl = Constants.URL_PRIVACY_POLICY,
                termsConditionsUrl = Constants.URL_TERMS_CONDITIONS
            )
        }

    }
}


@Composable
private fun TitleText(modifier: Modifier) {
    val annotatedString = buildAnnotatedString {
        append(stringResource(Res.string.sign_in_to))
        appendLine()
        withStyle(
            style = SpanStyle(
                color = AppTheme.colors.primary,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append(stringResource(Res.string.txt_main_action_to_sign_in))
        }
    }
    Text(
        modifier = modifier,
        text = annotatedString,
        color = AppTheme.colors.text.primary,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        style = AppTheme.typography.h3
    )
}