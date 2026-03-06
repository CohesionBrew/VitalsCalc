package com.cohesionbrew.healthcalculator.subscription.api

interface SubscriptionProviderFactory {
    companion object {}

    fun createProvider(): SubscriptionProvider
    fun createProviderUi(): SubscriptionProviderUi
}