package com.cohesionbrew.healthcalculator.presentation.screens.account

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.cohesionbrew.healthcalculator.designsystem.components.AppCardContainer
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.SettingItemListContainer
import com.cohesionbrew.healthcalculator.designsystem.components.SmallTitle
import com.cohesionbrew.healthcalculator.designsystem.components.modals.AppModalBottomSheet
import com.cohesionbrew.healthcalculator.designsystem.components.premium.UpgradePremiumBanner
import com.cohesionbrew.healthcalculator.designsystem.components.premium.UpgradePremiumBannerStyle
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.UiRes
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.btn_cancel
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.btn_logout_confirm
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_arrow_right
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_profile_img_placeholder
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.logout
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.text_logout_confirmation
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_copy_content
import com.cohesionbrew.healthcalculator.designsystem.theme.AppTheme
import com.cohesionbrew.healthcalculator.domain.model.User
import com.cohesionbrew.healthcalculator.generated.resources.Res
import com.cohesionbrew.healthcalculator.generated.resources.help_and_support
import com.cohesionbrew.healthcalculator.generated.resources.subscriptions
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_account
import com.cohesionbrew.healthcalculator.generated.resources.title_sign_in
import com.cohesionbrew.healthcalculator.root.AppGlobalUiState
import com.cohesionbrew.healthcalculator.util.Constants
import com.cohesionbrew.healthcalculator.util.UiMessage
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: AccountUiStateHolder,
    onNavigateHelpAndSupport: () -> Unit,
    onNavigatePaywall: () -> Unit,
    onNavigateSignIn: () -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigateSubscriptions: () -> Unit,
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()

    if (uiState.isLogoutDialogVisible) {
        LogoutModalBottomSheet(
            onConfirm = { uiStateHolder.onUiEvent(AccountUiEvent.OnLogoutConfirmClick) },
            onDismiss = { uiStateHolder.onUiEvent(AccountUiEvent.OnLogoutDialogDismiss) }
        )
    }

    AccountScreen(
        modifier = modifier
            .fillMaxSize()
            .background(AppTheme.colors.background),
        uiState = uiState,
        onUiEvent = {
            when (it) {
                is AccountUiEvent.OnSettingsItemClick -> {
                    when (it.item.textRes) {
                        Res.string.help_and_support -> onNavigateHelpAndSupport()
                        Res.string.subscriptions -> onNavigateSubscriptions()
                        else -> uiStateHolder.onUiEvent(it)
                    }
                }

                is AccountUiEvent.OnClickUpgradePremium -> {
                    onNavigatePaywall()
                }

                AccountUiEvent.OnClickSignIn -> {
                    onNavigateSignIn()
                }

                AccountUiEvent.OnClickProfile -> {
                    onNavigateProfile()
                }

                else -> uiStateHolder.onUiEvent(it)
            }
        }
    )
}

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    uiState: AccountUiState,
    onUiEvent: (AccountUiEvent) -> Unit
) {
    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = true,
        title = stringResource(Res.string.title_screen_account)
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)) {

            if (uiState.showUpgradePremiumBanner) {
                UpgradePremiumBanner(
                    style = UpgradePremiumBannerStyle.SMALL,
                    onClick = { onUiEvent(AccountUiEvent.OnClickUpgradePremium) })
            }

            ProfileInfoBox(user = uiState.user, onClick = {
                if (uiState.user == null) onUiEvent(AccountUiEvent.OnClickSignIn)
                else onUiEvent(AccountUiEvent.OnClickProfile)

            })
            SettingItemListContainer(
                itemList = uiState.settingsItemList,
                onClick = { onUiEvent(AccountUiEvent.OnSettingsItemClick(it)) }
            )

        }
    }
}


@Composable
private fun ProfileInfoBox(user: User?, onClick: () -> Unit) {
    val clipboardManager = LocalClipboardManager.current
    AppCardContainer(
        modifier = Modifier.fillMaxWidth(),
        onClick = {
            if (Constants.AUTH_SOCIAL_LOGIN_ENABLED) {
                onClick()
            } else {
                user?.id?.let {
                    clipboardManager.setText(AnnotatedString(it))
                    AppGlobalUiState.showUiMessage(UiMessage.Message("User ID is copied to clipboard"))
                }
            }
        }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.horizontalItemSpacing)
        ) {
            if (Constants.AUTH_SOCIAL_LOGIN_ENABLED) {

                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(user?.photoUrl)
                        .crossfade(true)
                        .build(),
                    placeholder = painterResource(UiRes.drawable.ic_profile_img_placeholder),
                    error = painterResource(UiRes.drawable.ic_profile_img_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(60.dp).clip(CircleShape),
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.groupedVerticalElementSpacingSmall)
                ) {
                    val displayName =
                        if (user == null) stringResource(Res.string.title_sign_in) else user.displayName

                    SmallTitle(text = displayName ?: "User Name")
                    user?.email?.let { email ->
                        Text(
                            email,
                            style = AppTheme.typography.bodyMedium,
                            color = AppTheme.colors.text.secondary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                }

                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = vectorResource(UiRes.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = AppTheme.colors.text.primary
                )
            }
            else {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "User ID: ${user?.id ?: ""}",
                    style = AppTheme.typography.bodySmall,
                    color = AppTheme.colors.text.primary,
                    fontWeight = FontWeight.Medium
                )

                Icon(
                    modifier = Modifier.size(24.dp),
                    imageVector = vectorResource(UiRes.drawable.ic_copy_content),
                    contentDescription = null,
                    tint = AppTheme.colors.text.primary
                )
            }

        }
    }
}

@Composable
private fun LogoutModalBottomSheet(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AppModalBottomSheet(
        title = stringResource(UiRes.string.logout),
        titleColor = AppTheme.colors.status.error,
        btnDismissText = stringResource(UiRes.string.btn_cancel),
        btnConfirmText = stringResource(UiRes.string.btn_logout_confirm),
        onConfirm = { onConfirm() },
        onDismiss = { onDismiss() },
        reverseButtonsOrder = true
    ) {
        Text(
            text = stringResource(UiRes.string.text_logout_confirmation),
            textAlign = TextAlign.Center,
            color = AppTheme.colors.text.primary,
            style = AppTheme.typography.h5
        )

    }
}
