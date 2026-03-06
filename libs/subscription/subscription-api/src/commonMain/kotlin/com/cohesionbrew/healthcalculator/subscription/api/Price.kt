package com.cohesionbrew.healthcalculator.subscription.api

data class Price(
    val amount: Float,
    val currencyCodeOrSymbol: String? = null,
    val localizedString: String? = null
)