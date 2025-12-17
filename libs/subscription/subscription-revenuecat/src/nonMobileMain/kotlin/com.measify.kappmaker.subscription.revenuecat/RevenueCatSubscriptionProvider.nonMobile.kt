package com.measify.kappmaker.subscription.revenuecat

import com.measify.kappmaker.subscription.api.NoOpSubscriptionProvider
import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory

internal actual val subscriptionProviderFactory: SubscriptionProviderFactory
    get() = { NoOpSubscriptionProvider }