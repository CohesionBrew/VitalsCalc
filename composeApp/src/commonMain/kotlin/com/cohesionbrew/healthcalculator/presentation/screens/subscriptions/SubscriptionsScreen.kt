package com.cohesionbrew.healthcalculator.presentation.screens.subscriptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.LoadingProgress
import com.cohesionbrew.healthcalculator.designsystem.components.LoadingProgressMode
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.premium.CurrentSubscriptionPlanAndFeatures
import com.cohesionbrew.healthcalculator.designsystem.components.premium.ManageSubscriptionText
import com.cohesionbrew.healthcalculator.designsystem.components.premium.UpgradePremiumBanner
import com.cohesionbrew.healthcalculator.designsystem.components.premium.UpgradePremiumBannerStyle
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.UiRes
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_back
import com.cohesionbrew.healthcalculator.designsystem.theme.AppTheme
import com.cohesionbrew.healthcalculator.domain.model.Subscription
import com.cohesionbrew.healthcalculator.generated.resources.Res
import com.cohesionbrew.healthcalculator.generated.resources.subscriptions
import com.cohesionbrew.healthcalculator.presentation.components.premium.PremiumFeatureFactory
import com.cohesionbrew.healthcalculator.util.Constants.subscriptionUrl
import com.cohesionbrew.healthcalculator.util.extensions.asFormattedDate
import org.jetbrains.compose.resources.stringResource

@Composable
fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: SubscriptionsUiStateHolder,
    onNavigatePaywall: () -> Unit,
    onClickBack: () -> Unit
) {

    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()

    if (uiState.isLoading) LoadingProgress(mode = LoadingProgressMode.FULLSCREEN)
    else {
        ScreenWithToolbar(
            modifier = modifier.fillMaxSize().background(AppTheme.colors.background),
            title = stringResource(Res.string.subscriptions),
            navigationIcon = UiRes.drawable.ic_back,
            includeBottomInsets = true,
            isScrollableContent = true,
            onNavigationIconClick = onClickBack,
        ) {
            SubscriptionsScreen(
                modifier = Modifier.fillMaxSize(),
                uiState = uiState,
                onUiEvent = uiStateHolder::onUiEvent,
                onClickUpgradePremium = { onNavigatePaywall() }
            )
        }
    }
}

@Composable
private fun SubscriptionsScreen(
    modifier: Modifier = Modifier,
    uiState: SubscriptionsUiState,
    onUiEvent: (SubscriptionsUiEvent) -> Unit,
    onClickUpgradePremium: () -> Unit = {},
) {
    val topPadding =
        if (uiState.showUpgradePremiumBanner) AppTheme.spacing.defaultSpacing
        else 0.dp
    Column(
        modifier = modifier.padding(top = topPadding),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)
    ){

        if (uiState.showUpgradePremiumBanner) {
            UpgradePremiumBanner(
                style = UpgradePremiumBannerStyle.LARGE,
                onClick = { onClickUpgradePremium() })
        }

        CurrentSubscriptionPlanAndFeatures(
            name = uiState.currentPlan?.name ?: "Free",
            features = PremiumFeatureFactory.ofSubscription(uiState.currentPlan),
            price = uiState.currentPlan?.formattedPrice,
            duration = when (uiState.currentPlan?.durationType) {
                Subscription.DurationType.MONTHLY -> "month"
                Subscription.DurationType.WEEKLY -> "week"
                Subscription.DurationType.YEARLY -> "year"
                Subscription.DurationType.LIFETIME -> "lifetime"
                else -> null
            },
        )

        if (uiState.currentPlan != null)
            ManageSubscriptionText(
                isLifetime = uiState.currentPlan.isLifetime,
                isRecurring = uiState.currentPlan.willRenew,
                expirationDate = uiState.currentPlan.expirationDateInMillis?.asFormattedDate(),
                subscriptionUrl = subscriptionUrl
            )

    }
}






