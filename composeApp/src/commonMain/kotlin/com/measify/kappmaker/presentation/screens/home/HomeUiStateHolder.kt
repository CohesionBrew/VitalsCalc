package com.measify.kappmaker.presentation.screens.home

import com.measify.kappmaker.data.repository.CreditRepository
import com.measify.kappmaker.domain.exceptions.CreditRequiredException
import com.measify.kappmaker.util.UiStateHolder
import com.measify.kappmaker.util.uiStateHolderScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeUiStateHolder(
    private val creditRepository: CreditRepository,
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        observeCreditBalance()
    }

    fun onMoreCreditsRequiredHandled() {
        _uiState.update { it.copy(isMoreCreditsRequired = false) }
    }

    fun onUiEvent(event: HomeUiEvent) {
        when (event) {
            HomeUiEvent.OnClickToolbarCredits -> {
                _uiState.update { it.copy(isMoreCreditsRequired = true) }
            }

            HomeUiEvent.OnClickBuyCredits -> {
                uiStateHolderScope.launch {
                    creditRepository.addCredits(5)
                }

            }

            HomeUiEvent.OnClickUseCredits -> {
                useCredit()
            }
        }
    }

    private fun observeCreditBalance() = uiStateHolderScope.launch {
        creditRepository.balance.collectLatest { creditBalance ->
            _uiState.update { it.copy(creditBalance = creditBalance) }
        }
    }

    private fun useCredit() = uiStateHolderScope.launch {
        try {
            creditRepository.useCredits(1, "Used for demo generation")
        } catch (e: Exception) {
            if (e is CreditRequiredException) {
                _uiState.update { it.copy(isMoreCreditsRequired = true) }
            }
            coroutineContext.ensureActive()
        }
    }
}