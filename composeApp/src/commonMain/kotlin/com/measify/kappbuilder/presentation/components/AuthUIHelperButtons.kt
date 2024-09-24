package com.measify.kappbuilder.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.measify.kappbuilder.domain.model.AuthProvider
import com.measify.kappbuilder.presentation.theme.LocalThemeIsDark
import com.measify.kappbuilder.util.logging.AppLogger
import com.mmk.kmpauth.firebase.apple.AppleButtonUiContainer
import com.mmk.kmpauth.firebase.google.GoogleButtonUiContainerFirebase
import com.mmk.kmpauth.uihelper.apple.AppleButtonMode
import com.mmk.kmpauth.uihelper.apple.AppleSignInButton
import com.mmk.kmpauth.uihelper.google.GoogleButtonMode
import com.mmk.kmpauth.uihelper.google.GoogleSignInButton
import dev.gitlive.firebase.auth.FirebaseUser


@Composable
fun AuthUIHelperButtons(
    modifier: Modifier = Modifier,
    authProviders: List<AuthProvider> = AuthProvider.entries,
    shape: Shape = RoundedCornerShape(16.dp),
    height: Dp = 56.dp,
    spaceBetweenButtons: Dp = 14.dp,
    textFontSize: TextUnit = 24.sp,
    autoClickEnabledIfOneProviderExists: Boolean = true,
    onFirebaseResult: (Result<FirebaseUser?>) -> Unit,
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(spaceBetweenButtons)) {
        val isExistOnlyOneAuthProvider by remember { mutableStateOf(authProviders.size == 1) }
        val updatedOnFirebaseResult by rememberUpdatedState(onFirebaseResult)
        val isDarkMode by LocalThemeIsDark.current
        if (authProviders.contains(AuthProvider.GOOGLE)) {
            //Google Sign-In Button and authentication with Firebase
            GoogleButtonUiContainerFirebase(onResult = {
                AppLogger.d("GoogleSignIn Result: $it")
                updatedOnFirebaseResult(it)
            }) {
                LaunchedEffect(Unit) { if (isExistOnlyOneAuthProvider && autoClickEnabledIfOneProviderExists) this@GoogleButtonUiContainerFirebase.onClick() }
                GoogleSignInButton(
                    modifier = Modifier.fillMaxWidth().height(height),
                    fontSize = textFontSize,
                    mode = if (isDarkMode) GoogleButtonMode.Dark else GoogleButtonMode.Light,
                    shape = shape
                ) { this.onClick() }
            }
        }

        if (authProviders.contains(AuthProvider.APPLE)) {
            //Apple Sign-In Button and authentication with Firebase
            AppleButtonUiContainer(onResult = {
                AppLogger.d("AppleSignIn Result: $it")
                updatedOnFirebaseResult(it)
            }) {
                LaunchedEffect(Unit) { if (isExistOnlyOneAuthProvider && autoClickEnabledIfOneProviderExists) this@AppleButtonUiContainer.onClick() }
                AppleSignInButton(
                    modifier = Modifier.fillMaxWidth().height(height),
                    mode = if (isDarkMode) AppleButtonMode.White else AppleButtonMode.Black,
                    shape = shape,
                ) { this.onClick() }
            }
        }
    }

}
