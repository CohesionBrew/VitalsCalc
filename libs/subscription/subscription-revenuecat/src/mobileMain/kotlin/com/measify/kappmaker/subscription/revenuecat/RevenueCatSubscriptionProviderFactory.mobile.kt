package com.measify.kappmaker.subscription.revenuecat

import com.measify.kappmaker.subscription.api.SubscriptionProvider
import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory
import com.measify.kappmaker.subscription.api.SubscriptionProviderUi

internal actual val subscriptionProviderFactory: SubscriptionProviderFactory
    get() =  object : SubscriptionProviderFactory {
        override fun createProvider(): SubscriptionProvider {
            return RevenueCatSubscriptionProvider()
        }

        override fun createProviderUi(): SubscriptionProviderUi {
            return RevenueCatSubscriptionProviderUi()
        }
    }