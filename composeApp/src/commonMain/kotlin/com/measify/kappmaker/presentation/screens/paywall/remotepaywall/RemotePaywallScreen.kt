package com.measify.kappmaker.presentation.screens.paywall.remotepaywall

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.msg_successful_payment
import com.measify.kappmaker.generated.resources.msg_successful_restore_payment
import com.measify.kappmaker.presentation.components.MessageDialog
import com.measify.kappmaker.util.UiMessage
import com.measify.kappmaker.util.logging.AppLogger
import com.revenuecat.purchases.kmp.CustomerInfo
import com.revenuecat.purchases.kmp.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreTransaction
import com.revenuecat.purchases.kmp.ui.revenuecatui.Paywall
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallListener
import com.revenuecat.purchases.kmp.ui.revenuecatui.PaywallOptions



@Composable
fun RemotePaywallScreen(onDismiss: () -> Unit) {
    var message by remember { mutableStateOf<UiMessage?>(null) }
    message?.let {
        MessageDialog(
            text = it.value,
            onConfirm = {
                message = null
                onDismiss()
            }
        )
    }
    val paywallOptions = remember {
        PaywallOptions(dismissRequest = { onDismiss() }) {
            this.shouldDisplayDismissButton = true
            this.listener = object : PaywallListener {
                override fun onPurchaseCompleted(
                    customerInfo: CustomerInfo,
                    storeTransaction: StoreTransaction
                ) {
                    //Successful payment
                    super.onPurchaseCompleted(customerInfo, storeTransaction)
                    message = UiMessage.Resource(Res.string.msg_successful_payment)

                }

                override fun onPurchaseError(error: PurchasesError) {
                    super.onPurchaseError(error)
                    AppLogger.e("There was an error with purchase: $error")
                }

                override fun onRestoreCompleted(customerInfo: CustomerInfo) {
                    super.onRestoreCompleted(customerInfo)
                    message = UiMessage.Resource(Res.string.msg_successful_restore_payment)
                }

                override fun onRestoreError(error: PurchasesError) {
                    super.onRestoreError(error)
                    AppLogger.e("There was an error with restore purchase: $error")
                }
            }
        }
    }
    Paywall(options = paywallOptions)
}