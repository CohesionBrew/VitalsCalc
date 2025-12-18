package com.measify.kappmaker.subscription.adapty

import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory


val SubscriptionProviderFactory.Companion.Adapty: SubscriptionProviderFactory
    get() = subscriptionProviderFactory

internal expect val subscriptionProviderFactory: SubscriptionProviderFactory