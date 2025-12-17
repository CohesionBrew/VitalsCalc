package com.measify.kappmaker.presentation.screens.home

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.measify.kappmaker.designsystem.components.AppButton
import com.measify.kappmaker.designsystem.components.ButtonStyle
import com.measify.kappmaker.designsystem.components.Chip
import com.measify.kappmaker.designsystem.components.ChipSize
import com.measify.kappmaker.designsystem.components.ChipStyle
import com.measify.kappmaker.designsystem.components.ScreenWithToolbar
import com.measify.kappmaker.designsystem.generated.resources.UiRes
import com.measify.kappmaker.designsystem.generated.resources.ic_coin_credits
import com.measify.kappmaker.designsystem.theme.AppTheme
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.title_screen_home
import com.measify.kappmaker.subscription.adapty.Adapty
import com.measify.kappmaker.subscription.api.SubscriptionProvider
import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory
import com.measify.kappmaker.subscription.api.SubscriptionProviderUi
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: HomeUiStateHolder,
    onMoreCreditsNeeded: () -> Unit,
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isMoreCreditsRequired) {
        if (uiState.isMoreCreditsRequired) {
            onMoreCreditsNeeded()
            uiStateHolder.onMoreCreditsRequiredHandled()
        }
    }

    HomeScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = uiStateHolder::onUiEvent
    )
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current

    ScreenWithToolbar(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onTap = { focusManager.clearFocus() })
        },
        isScrollableContent = false,
        title = stringResource(Res.string.title_screen_home),
        includeBottomInsets = false,// Set to true if bottom nav is not visible
        toolbarExtraContent = {
            Chip(
                text = "${uiState.creditBalance}",
                style = ChipStyle.FILLED_ALPHA,
                size = ChipSize.SMALL,
                startIconRes = UiRes.drawable.ic_coin_credits,
                onClick = {
                    onUiEvent(HomeUiEvent.OnClickToolbarCredits)
                }
            )
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)) {
            Text("HomeScreen")

            AppButton(text = "Purchase 5 credits") {

                onUiEvent(HomeUiEvent.OnClickBuyCredits)
            }

            AppButton(text = "Use 1 credit", style = ButtonStyle.ALTERNATIVE) {
                onUiEvent(HomeUiEvent.OnClickUseCredits)
            }
        }
    }
}