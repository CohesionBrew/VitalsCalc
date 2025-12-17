package com.measify.kappmaker.subscription.api

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

object NoOpSubscriptionProviderUi: SubscriptionProviderUi {
    @Composable
    override fun RemotePaywall(
        placementId: String?,
        listener: PurchaseEventsListener
    ) {
        Text("NoOpSubscriptionProviderUi")
    }
}