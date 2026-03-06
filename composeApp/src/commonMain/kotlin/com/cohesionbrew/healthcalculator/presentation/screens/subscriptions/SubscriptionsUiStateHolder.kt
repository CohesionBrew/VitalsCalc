package com.cohesionbrew.healthcalculator.presentation.screens.subscriptions

import com.cohesionbrew.healthcalculator.data.repository.SubscriptionRepository
import com.cohesionbrew.healthcalculator.domain.model.isFree
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SubscriptionsUiStateHolder(private val subscriptionRepository: SubscriptionRepository) :
    UiStateHolder() {
    private val _uiState = MutableStateFlow(SubscriptionsUiState(isLoading = true))
    val uiState: StateFlow<SubscriptionsUiState> = _uiState.asStateFlow()

    init {
        uiStateHolderScope.launch {
            subscriptionRepository.currentSubscriptionFlow.collect { currentSubscription ->
                _uiState.update {
                    it.copy(
                        currentPlan = currentSubscription,
                        isLoading = false,
                        showUpgradePremiumBanner = currentSubscription.isFree
                    )
                }
            }
        }
    }



    fun onUiEvent(event: SubscriptionsUiEvent) {

    }

}