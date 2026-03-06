package com.cohesionbrew.healthcalculator.presentation.screens.subscriptions

import com.cohesionbrew.healthcalculator.domain.model.Subscription

data class SubscriptionsUiState(
    val isLoading: Boolean = false,
    val showUpgradePremiumBanner: Boolean = true,
    val currentPlan: Subscription? = null
)

sealed class SubscriptionsUiEvent {

}