package com.measify.kappbuilder.presentation.screens.paywall

import com.measify.kappbuilder.generated.resources.Res
import com.measify.kappbuilder.generated.resources.msg_successful_payment
import com.measify.kappbuilder.generated.resources.msg_successful_restore_payment
import com.measify.kappbuilder.util.UiMessage
import com.measify.kappbuilder.util.UiStateHolder
import com.measify.kappbuilder.util.extensions.buttonText
import com.measify.kappbuilder.util.logging.AppLogger
import com.measify.kappbuilder.util.uiStateHolderScope
import com.revenuecat.purchases.kmp.Purchases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class PaywallUiStateHolder() : UiStateHolder {
    private val _uiState = MutableStateFlow(PaywallUiState())
    val uiState: StateFlow<PaywallUiState> = _uiState.asStateFlow()


    init {
        getOfferings()
    }

    fun onMessageShown() {
        _uiState.update { it.copy(message = null) }
    }

    private fun getOfferings() {
        _uiState.update { it.copy(isLoading = true) }
        Purchases.sharedInstance.getOfferings(
            onError = { error ->
                AppLogger.e("RevenueCat: Error getting offerings: $error")
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        message = UiMessage.Message(error.message),
                        buyButtonEnabled = false
                    )
                }

            },
            onSuccess = { offerings ->
                offerings.current?.availablePackages?.takeUnless { it.isEmpty() }?.let { packages ->
                    val filteredPackages = packages.sortedBy { it.storeProduct.price.amountMicros }
                    val defaultSelectedPackageIndex = 0
                    val selectedPackage = filteredPackages.getOrNull(defaultSelectedPackageIndex)
                    // You can change features dynamically based on selected package
                    val features =
                        listOf(
                            "Amazing Feature 1",
                            "Amazing Feature 2",
                            "Amazing Feature 3"
                        )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            features = features,
                            packages = filteredPackages,
                            selectedPackage = selectedPackage,
                            buyButtonEnabled = selectedPackage != null
                        )
                    }
                }
            })
    }

    fun onUiEvent(event: PaywallUiEvent) {
        when (event) {
            PaywallUiEvent.OnClickBuy -> {
                buyPackage()
            }

            PaywallUiEvent.OnClickRestore -> {
                restorePayment()
            }

            is PaywallUiEvent.OnSelectPackage -> {
                _uiState.update {
                    it.copy(
                        selectedPackage = event.rcPackage,
                        buyButtonEnabled = true,
                        buyButtonText = event.rcPackage.buttonText
                    )
                }
            }
        }
    }

    private fun restorePayment() = uiStateHolderScope.launch {

        if (userCanDoPaymentAction().not()) {
            _uiState.update { it.copy(message = UiMessage.Message("You can not do payment action")) }
            return@launch
        }

        _uiState.update { it.copy(isLoading = true) }
        Purchases.sharedInstance.restorePurchases(
            onError = {
                AppLogger.e("RevenueCat: Error restoring purchases: $it")
                _uiState.update { state ->
                    state.copy(
                        message = UiMessage.Message(it.message),
                        isLoading = false
                    )
                }
            }, onSuccess = {
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        isSuccessfulPurchase = true,
                        message = UiMessage.Resource(Res.string.msg_successful_restore_payment)
                    )
                }
                AppLogger.e("RevenueCat: Successful restoring purchases: $it")
            })
    }

    private fun buyPackage() = uiStateHolderScope.launch {
        if (userCanDoPaymentAction().not()) {
            _uiState.update { it.copy(message = UiMessage.Message("You can not do payment action")) }
            return@launch
        }
        val selectedPackage = uiState.value.selectedPackage
        if (selectedPackage != null) {
            _uiState.update { it.copy(buyButtonEnabled = false) }
            Purchases.sharedInstance.purchase(
                selectedPackage,
                onSuccess = { transaction, customer ->
                    _uiState.update {
                        it.copy(
                            buyButtonEnabled = true,
                            isSuccessfulPurchase = true,
                            message = UiMessage.Resource(Res.string.msg_successful_payment)
                        )
                    }
                },
                onError = { error, userCancelled ->
                    _uiState.update {
                        it.copy(
                            buyButtonEnabled = true,
                            message = UiMessage.Message(error.message)
                        )
                    }
                }
            )
        }
    }

    private suspend fun userCanDoPaymentAction(): Boolean {
        //You can for example check if user is authenticated here
        return true
    }

}

