package com.measify.kappbuilder.presentation.screens.signin

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.measify.kappbuilder.presentation.components.AgreePrivacyPolicyTermsConditionsText
import com.measify.kappbuilder.presentation.components.AuthUIHelperButtons
import com.measify.kappbuilder.util.logging.AppLogger
import com.measify.kappbuilder.generated.resources.Res
import com.measify.kappbuilder.generated.resources.ic_logo
import com.measify.kappbuilder.generated.resources.sign_in_to
import com.measify.kappbuilder.generated.resources.txt_main_action_to_sign_in
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SignInScreen(modifier: Modifier = Modifier, onSuccessfulSignIn: () -> Unit) {

    val scrollState = rememberScrollState()
    LaunchedEffect(true) {
        scrollState.animateScrollTo(scrollState.maxValue, tween(500))
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            Modifier.fillMaxSize().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            LogoImage(modifier = Modifier.fillMaxWidth().heightIn(max = 300.dp).padding(4.dp))
            Spacer(modifier = Modifier.weight(1f))
            TitleText(modifier = Modifier.padding(20.dp))
            AuthUIHelperButtons(modifier = Modifier.padding(top = 32.dp).fillMaxWidth()) { result ->
                result.onSuccess {
                    AppLogger.d("Successful sign in")
                    onSuccessfulSignIn()
                }.onFailure {
                    coroutineScope.launch { snackbarHostState.showSnackbar(it.message ?: "") }
                    AppLogger.e("Error occurred while signing in, $it")
                }
            }
            AgreePrivacyPolicyTermsConditionsText(
                modifier = Modifier.padding(top = 32.dp).fillMaxWidth(),
            )


        }
    }

}

@Composable
private fun LogoImage(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier.size(140.dp).align(Alignment.Center)
        )
    }

}

@Composable
private fun TitleText(modifier: Modifier) {
    val annotatedString = buildAnnotatedString {
        append(stringResource(Res.string.sign_in_to))
        appendLine()
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        ) {
            append(stringResource(Res.string.txt_main_action_to_sign_in))
        }
    }
    Text(
        modifier = modifier,
        text = annotatedString,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.headlineMedium
    )
}