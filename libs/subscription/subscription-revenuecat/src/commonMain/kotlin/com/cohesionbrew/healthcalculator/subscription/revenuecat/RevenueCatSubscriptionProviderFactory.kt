package com.cohesionbrew.healthcalculator.subscription.revenuecat

import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProviderFactory


val SubscriptionProviderFactory.Companion.RevenueCat: SubscriptionProviderFactory
    get() = subscriptionProviderFactory

internal expect val subscriptionProviderFactory: SubscriptionProviderFactory