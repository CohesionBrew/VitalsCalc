package com.measify.kappmaker.subscription.adapty

import com.measify.kappmaker.subscription.api.NoOpSubscriptionProvider
import com.measify.kappmaker.subscription.api.NoOpSubscriptionProviderUi
import com.measify.kappmaker.subscription.api.SubscriptionProvider
import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory
import com.measify.kappmaker.subscription.api.SubscriptionProviderUi

internal actual val subscriptionProviderFactory: SubscriptionProviderFactory
    get() = object : SubscriptionProviderFactory {
        override fun createProvider(): SubscriptionProvider {
            return NoOpSubscriptionProvider
        }

        override fun createProviderUi(): SubscriptionProviderUi {
            return NoOpSubscriptionProviderUi
        }
    }