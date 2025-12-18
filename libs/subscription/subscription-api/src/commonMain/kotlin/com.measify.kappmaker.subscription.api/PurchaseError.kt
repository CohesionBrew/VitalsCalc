package com.measify.kappmaker.subscription.api

data class PurchaseError(
    val message: String,
    val code: String? = null,
)
