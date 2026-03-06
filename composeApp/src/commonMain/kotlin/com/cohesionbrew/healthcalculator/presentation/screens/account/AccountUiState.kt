package com.cohesionbrew.healthcalculator.presentation.screens.account

import com.cohesionbrew.healthcalculator.designsystem.components.SettingsItemUiState
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_settings_item_logout
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_settings_item_subscriptions
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_settings_item_support_legal
import com.cohesionbrew.healthcalculator.domain.model.User
import com.cohesionbrew.healthcalculator.generated.resources.Res
import com.cohesionbrew.healthcalculator.generated.resources.help_and_support
import com.cohesionbrew.healthcalculator.generated.resources.logout
import com.cohesionbrew.healthcalculator.generated.resources.subscriptions
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.UiRes

data class AccountUiState(
    val settingsItemList: List<SettingsItemUiState> = listOf(
        SettingsItemUiState(
            startIcon = UiRes.drawable.ic_settings_item_subscriptions,
            textRes = Res.string.subscriptions
        ),

        SettingsItemUiState(
            startIcon = UiRes.drawable.ic_settings_item_support_legal,
            textRes = Res.string.help_and_support
        ),

        SettingsItemUiState(
            startIcon = UiRes.drawable.ic_settings_item_logout,
            textRes = Res.string.logout,
            showEndIcon = false,
        ),
    ),
    val user: User? = null,
    val isLogoutDialogVisible: Boolean = false,
    val showUpgradePremiumBanner: Boolean = false,
)

sealed class AccountUiEvent {
    data class OnSettingsItemClick(val item: SettingsItemUiState) : AccountUiEvent()
    data object OnLogoutConfirmClick : AccountUiEvent()
    data object OnLogoutDialogDismiss : AccountUiEvent()
    data object OnClickUpgradePremium : AccountUiEvent()
    data object OnClickSignIn : AccountUiEvent()
    data object OnClickProfile : AccountUiEvent()
}