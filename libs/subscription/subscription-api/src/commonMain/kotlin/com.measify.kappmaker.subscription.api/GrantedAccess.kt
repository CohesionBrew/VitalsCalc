package com.measify.kappmaker.subscription.api

data class GrantedAccess(
    val id: String,
    val expirationDateMillis: Long?,
    val willRenew: Boolean,
    val isLifetime: Boolean = false,
    val productIdentifier: String
)