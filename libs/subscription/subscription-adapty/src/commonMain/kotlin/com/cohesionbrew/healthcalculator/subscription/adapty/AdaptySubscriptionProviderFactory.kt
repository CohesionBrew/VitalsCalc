package com.cohesionbrew.healthcalculator.subscription.adapty

import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProviderFactory


val SubscriptionProviderFactory.Companion.Adapty: SubscriptionProviderFactory
    get() = subscriptionProviderFactory

internal expect val subscriptionProviderFactory: SubscriptionProviderFactory