package com.measify.kappmaker.subscription.api

interface SubscriptionProviderFactory {
    companion object {}

    fun createProvider(): SubscriptionProvider
    fun createProviderUi(): SubscriptionProviderUi
}