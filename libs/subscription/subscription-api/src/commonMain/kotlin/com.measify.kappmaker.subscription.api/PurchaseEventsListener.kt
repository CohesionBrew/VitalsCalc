package com.measify.kappmaker.subscription.api

interface PurchaseEventsListener {
    fun onPurchaseSuccess(info: SubscriptionProviderUser, productIds: List<String>)
    fun onRestoreSuccess(info: SubscriptionProviderUser)
    fun onPurchaseFailure(error: PurchaseError)
    fun onRestoreFailure(error: PurchaseError)
    fun onDismiss() {}
    fun onUnknownError(error: Exception) {}
}
