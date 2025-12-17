package com.measify.kappmaker.subscription.revenuecat

import com.measify.kappmaker.subscription.api.GrantedAccess
import com.measify.kappmaker.subscription.api.PurchasePackageId
import com.measify.kappmaker.subscription.api.SubscriptionProvider
import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory
import com.measify.kappmaker.subscription.api.SubscriptionProviderUser
import com.measify.kappmaker.subscription.api.runCatchingSuspend
import com.revenuecat.purchases.kmp.LogLevel
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import com.revenuecat.purchases.kmp.PurchasesDelegate
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import com.revenuecat.purchases.kmp.ktx.awaitLogIn
import com.revenuecat.purchases.kmp.ktx.awaitLogOut
import com.revenuecat.purchases.kmp.ktx.awaitPurchase
import com.revenuecat.purchases.kmp.ktx.awaitRestore
import com.revenuecat.purchases.kmp.models.CustomerInfo
import com.revenuecat.purchases.kmp.models.Package
import com.revenuecat.purchases.kmp.models.PurchasesError
import com.revenuecat.purchases.kmp.models.StoreProduct
import com.revenuecat.purchases.kmp.models.StoreTransaction
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal actual val subscriptionProviderFactory: SubscriptionProviderFactory
    get() = {
        RevenueCatSubscriptionProvider()
    }

private class RevenueCatSubscriptionProvider : SubscriptionProvider {

    private val packagesCache = mutableMapOf<PurchasePackageId, Package>()

    override val currentSubscriptionProviderUserFlow: Flow<SubscriptionProviderUser?> = callbackFlow {
        val delegate = object : PurchasesDelegate {
            override fun onCustomerInfoUpdated(customerInfo: CustomerInfo) {
                trySend(customerInfo.asSubscriptionProviderUser())
            }

            override fun onPurchasePromoProduct(
                product: StoreProduct,
                startPurchase: (onError: (error: PurchasesError, userCancelled: Boolean) -> Unit, onSuccess: (storeTransaction: StoreTransaction, customerInfo: CustomerInfo) -> Unit) -> Unit
            ) {
            }
        }
        Purchases.sharedInstance.delegate = delegate
        awaitClose {
            Purchases.sharedInstance.delegate = null
        }
    }


    override suspend fun initialize(apiKey: String): Result<Unit> {
        Purchases.configure(PurchasesConfiguration(apiKey = apiKey))
        return Result.success(Unit)
    }

    override suspend fun setLogEnabled(enabled: Boolean) {
        val revenueCatLogLevel = when (enabled) {
            true -> LogLevel.VERBOSE
            false -> LogLevel.ERROR
        }
        Purchases.logLevel = revenueCatLogLevel
    }

    override suspend fun login(userId: String): Result<Unit> = runCatchingSuspend {
        Purchases.sharedInstance.awaitLogIn(userId)
    }.map { }


    override suspend fun logout(): Result<Unit> = runCatchingSuspend {
        Purchases.sharedInstance.awaitLogOut()
    }.map { }

    override suspend fun getUser(): Result<SubscriptionProviderUser> = runCatchingSuspend {
        Purchases.sharedInstance.awaitCustomerInfo()
    }.map {
        it.asSubscriptionProviderUser()
    }

    override suspend fun setCustomAttributes(attributes: Map<String, Any?>) {
        val mappedAttributes = attributes.mapValues { (key, value) ->
            when (value) {
                null -> null
                is String -> value
                else -> value.toString()
            }
        }
        Purchases.sharedInstance.setAttributes(mappedAttributes)
    }

    override suspend fun restorePurchase(): Result<SubscriptionProviderUser> = runCatchingSuspend {
        val customerInfo = Purchases.sharedInstance.awaitRestore()
        val hasSuccessfulRestore = customerInfo.entitlements.all.any { it.value.isActive }
        if (!hasSuccessfulRestore) throw Exception("Restore failed. No active subscription found")
        customerInfo
    }.map {
        it.asSubscriptionProviderUser()
    }

    override suspend fun purchase(purchasePackageId: PurchasePackageId): Result<SubscriptionProviderUser> = runCatchingSuspend {
        val packageToBuy = packagesCache[purchasePackageId]
            ?: throw Exception("Package is not found in RevenueCat cache. Make sure, you called getPurchasePackages first")
        val successfulPurchaseResult = Purchases.sharedInstance.awaitPurchase(packageToBuy)
       successfulPurchaseResult.customerInfo
    }.map {
        it.asSubscriptionProviderUser()
    }

    private fun CustomerInfo.asSubscriptionProviderUser(): SubscriptionProviderUser {

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
}