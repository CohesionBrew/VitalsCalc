package com.cohesionbrew.healthcalculator.subscription.revenuecat

import com.cohesionbrew.healthcalculator.subscription.api.GrantedAccess
import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProviderUser
import com.revenuecat.purchases.kmp.models.CustomerInfo

internal fun CustomerInfo.asSubscriptionProviderUser(): SubscriptionProviderUser {

    val grantedAccesses = entitlements.active.values.associate { entitlement ->
        entitlement.identifier to GrantedAccess(
            id = entitlement.identifier,
            expirationDateMillis = entitlement.expirationDateMillis,
            isLifetime = entitlement.expirationDateMillis == null && entitlement.willRenew,
            willRenew = entitlement.willRenew,
            productIdentifier = entitlement.productIdentifier,
        )
    }

    return SubscriptionProviderUser(
        grantedAccesses = grantedAccesses,
        activeSubscriptionIds = activeSubscriptions
    )
}
