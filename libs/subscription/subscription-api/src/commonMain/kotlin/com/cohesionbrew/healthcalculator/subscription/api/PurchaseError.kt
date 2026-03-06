package com.cohesionbrew.healthcalculator.subscription.api

data class PurchaseError(
    val message: String,
    val code: String? = null,
)
