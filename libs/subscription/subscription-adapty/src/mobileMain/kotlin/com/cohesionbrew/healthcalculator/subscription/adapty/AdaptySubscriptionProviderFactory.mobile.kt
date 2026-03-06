package com.cohesionbrew.healthcalculator.subscription.adapty

import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProvider
import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProviderFactory
import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProviderUi

internal actual val subscriptionProviderFactory: SubscriptionProviderFactory
    get() = object : SubscriptionProviderFactory {
        override fun createProvider(): SubscriptionProvider {
            return AdaptySubscriptionProvider()
        }

        override fun createProviderUi(): SubscriptionProviderUi {
            return AdaptySubscriptionProviderUi()
        }
    }