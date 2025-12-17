package com.measify.kappmaker.subscription.revenuecat

import com.measify.kappmaker.subscription.api.SubscriptionProvider
import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory


val SubscriptionProvider.Companion.RevenueCat: SubscriptionProviderFactory
    get() = subscriptionProviderFactory

internal expect val subscriptionProviderFactory: SubscriptionProviderFactory