package com.cohesionbrew.healthcalculator.presentation.screens.more

import com.cohesionbrew.healthcalculator.domain.premium.FeatureAccessManager
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MoreUiStateHolder(
    private val featureAccessManager: FeatureAccessManager
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(MoreUiState())
    val uiState: StateFlow<MoreUiState> = _uiState.asStateFlow()

    init {
        uiStateHolderScope.launch {
            featureAccessManager.isPro.collectLatest { pro ->
                _uiState.update { it.copy(isPro = pro) }
            }
        }
    }

    fun onUiEvent(event: MoreUiEvent) {}
}
