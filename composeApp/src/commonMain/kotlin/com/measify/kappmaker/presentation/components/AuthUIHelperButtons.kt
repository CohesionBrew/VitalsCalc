package com.measify.kappmaker.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.measify.kappmaker.domain.model.AuthProvider
import com.measify.kappmaker.presentation.theme.AppTheme
import com.measify.kappmaker.presentation.theme.LocalThemeIsDark
import com.measify.kappmaker.util.logging.AppLogger
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.apple.AppleButtonMode
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import dev.gitlive.firebase.auth.FirebaseUser
import org.jetbrains.compose.ui.tooling.preview.Preview


@Composable
fun AuthUIHelperButtons(
    modifier: Modifier = Modifier,
    authProviders: List<AuthProvider> = AuthProvider.entries,
    shape: Shape = CircleShape,
    height: Dp = 56.dp,
    spaceBetweenButtons: Dp = AppTheme.spacing.groupedVerticalElementSpacing,
    textFontSize: TextUnit = 24.sp,
    autoClickEnabledIfOneProviderExists: Boolean = true,
    linkAccount: Boolean = false,
    onFirebaseResult: (Result<FirebaseUser?>) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spaceBetweenButtons)) {
        val isExistOnlyOneAuthProvider by remember { mutableStateOf(authProviders.size == 1) }
        val updatedOnFirebaseResult by rememberUpdatedState(onFirebaseResult)
        val isDarkMode = LocalThemeIsDark.current
        if (authProviders.contains(AuthProvider.GOOGLE)) {
            //Google Sign-In Button and authentication with Firebase
            GoogleButtonUiContainerFirebase(linkAccount = linkAccount, onResult = {
                AppLogger.d("GoogleSignIn Result: $it")
                updatedOnFirebaseResult(it)
            }) {
                LaunchedEffect(Unit) { if (isExistOnlyOneAuthProvider && autoClickEnabledIfOneProviderExists) this@GoogleButtonUiContainerFirebase.onClick() }
                GoogleSignInButton(
                    modifier = Modifier.fillMaxWidth().height(height),
                    fontSize = textFontSize,
                    text = if (linkAccount) "Continue with Google" else "Sign in with Google",
                    mode = if (isDarkMode) GoogleButtonMode.Dark else GoogleButtonMode.Light,
                    shape = shape
                ) { this.onClick() }
            }
        }

        if (authProviders.contains(AuthProvider.APPLE)) {
            //Apple Sign-In Button and authentication with Firebase
            AppleButtonUiContainer(linkAccount = linkAccount, onResult = {
                AppLogger.d("AppleSignIn Result: $it")
                updatedOnFirebaseResult(it)
            }) {
                LaunchedEffect(Unit) { if (isExistOnlyOneAuthProvider && autoClickEnabledIfOneProviderExists) this@AppleButtonUiContainer.onClick() }
                AppleSignInButton(
                    modifier = Modifier.fillMaxWidth().height(height),
                    text = if (linkAccount) "Continue with Apple" else "Sign in with Apple",
                    mode = if (isDarkMode) AppleButtonMode.White else AppleButtonMode.Black,
                    shape = shape,
                ) { this.onClick() }
            }
        }
    }

}

@Preview
@Composable
fun AuthUiHelperButtonsPreview() {
    PreviewHelper {
        AuthUIHelperButtons { }
    }
}