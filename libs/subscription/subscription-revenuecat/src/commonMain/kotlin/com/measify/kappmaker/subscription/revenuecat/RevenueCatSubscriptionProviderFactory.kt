package com.measify.kappmaker.subscription.revenuecat

import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory


val SubscriptionProviderFactory.Companion.RevenueCat: SubscriptionProviderFactory
    get() = subscriptionProviderFactory

internal expect val subscriptionProviderFactory: SubscriptionProviderFactory