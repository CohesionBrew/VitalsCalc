package com.measify.kappmaker.presentation.screens.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.measify.kappmaker.designsystem.components.AppButton
import com.measify.kappmaker.designsystem.components.Chip
import com.measify.kappmaker.designsystem.components.ChipSize
import com.measify.kappmaker.designsystem.components.ChipStyle
import com.measify.kappmaker.designsystem.components.ScreenWithToolbar
import com.measify.kappmaker.designsystem.generated.resources.UiRes
import com.measify.kappmaker.designsystem.generated.resources.ic_coin_credits
import com.measify.kappmaker.designsystem.theme.AppTheme
import com.measify.kappmaker.root.AppGlobalUiState
import com.measify.kappmaker.util.UiMessage

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: FavoriteUiStateHolder,
    onPaymentRequired: () -> Unit
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isPaymentRequired) {
        if (uiState.isPaymentRequired) {
            onPaymentRequired()
            uiStateHolder.onPaymentRequiredHandled()
        }
    }

    FavoriteScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = uiStateHolder::onUiEvent
    )
}

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    uiState: FavoriteUiState,
    onUiEvent: (FavoriteUiEvent) -> Unit
) {
    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = true,
        title = "FavoriteScreen",
        includeBottomInsets = false, // Set to true if bottom nav is not visible
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)) {
            Text("FavoriteScreen")

            AppButton(text = "Get Premium Content", onClick = {
                onUiEvent(FavoriteUiEvent.OnClickGetPremium)
            })
        }
    }
}