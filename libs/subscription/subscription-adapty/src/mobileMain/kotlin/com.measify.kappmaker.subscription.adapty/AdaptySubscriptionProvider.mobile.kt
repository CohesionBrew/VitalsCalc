package com.measify.kappmaker.subscription.adapty

import com.adapty.kmp.Adapty
import com.adapty.kmp.OnProfileUpdatedListener
import com.adapty.kmp.models.AdaptyConfig
import com.adapty.kmp.models.AdaptyLogLevel
import com.adapty.kmp.models.AdaptyPaywallProduct
import com.adapty.kmp.models.AdaptyProfile
import com.adapty.kmp.models.AdaptyProfileParameters
import com.adapty.kmp.models.AdaptyPurchaseResult
import com.adapty.kmp.models.AdaptyResult
import com.adapty.kmp.models.fold
import com.measify.kappmaker.subscription.api.GrantedAccess
import com.measify.kappmaker.subscription.api.PurchasePackageId
import com.measify.kappmaker.subscription.api.SubscriptionProvider
import com.measify.kappmaker.subscription.api.SubscriptionProviderFactory
import com.measify.kappmaker.subscription.api.SubscriptionProviderUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

internal actual val subscriptionProviderFactory: SubscriptionProviderFactory
    get() = {
        AdaptySubscriptionProvider()
    }

private class AdaptySubscriptionProvider : SubscriptionProvider {

    private val paywallProductsCache = mutableMapOf<String, AdaptyPaywallProduct>()

    override val currentSubscriptionProviderUserFlow: Flow<SubscriptionProviderUser?> = callbackFlow {
        val listener = OnProfileUpdatedListener { adaptyProfile ->
            trySend(adaptyProfile.asSubscriptionProviderUser())
        }
        Adapty.setOnProfileUpdatedListener(listener)
        awaitClose {
            Adapty.setOnProfileUpdatedListener(null)
        }
    }


    override suspend fun initialize(apiKey: String): Result<Unit> {
        val adaptyResult = Adapty.activate(
            configuration = AdaptyConfig.Builder(apiKey = apiKey)
                .withActivateUI(true)
                .build()
        )
        return adaptyResult.asResult(onSuccess = {})
    }

    override suspend fun setLogEnabled(enabled: Boolean) {
        val adaptyLogLevel = when (enabled) {
            true -> AdaptyLogLevel.VERBOSE
            false -> AdaptyLogLevel.ERROR
        }
        Adapty.setLogLevel(adaptyLogLevel)
    }

    override suspend fun login(userId: String): Result<Unit> {

        return Adapty.identify(userId).asResult {}
    }

    override suspend fun logout(): Result<Unit> {
        return Adapty.logout().asResult {}
    }

    override suspend fun setCustomAttributes(attributes: Map<String, Any?>) {
        val builder = AdaptyProfileParameters.Builder()
        attributes.forEach { (key, value) ->
            when (value) {
                null -> builder.withRemovedCustomAttribute(key)
                is String -> builder.withCustomAttribute(key, value)
                is Double -> builder.withCustomAttribute(key, value)
                else -> builder.withCustomAttribute(key, value.toString())
            }
        }
        Adapty.updateProfile(builder.build())
    }

    override suspend fun getUser(): Result<SubscriptionProviderUser> {
        return Adapty.getProfile().asResult { it.asSubscriptionProviderUser() }
    }

    override suspend fun purchase(purchasePackageId: PurchasePackageId): Result<SubscriptionProviderUser> {
        val packageToBuy = paywallProductsCache[purchasePackageId.value]

        if (packageToBuy == null) return Result.failure(Exception("Package is not found in Adapty paywall cache. Make sure, you called getPurchasePackages first"))
        val purchaseResult = Adapty.makePurchase(packageToBuy)

        return when (purchaseResult) {
            is AdaptyResult.Error -> Result.failure(purchaseResult.error)
            is AdaptyResult.Success<AdaptyPurchaseResult> -> {
                when (val successfulPurchaseResult = purchaseResult.value) {
                    is AdaptyPurchaseResult.Success -> {

                        Result.success(successfulPurchaseResult.profile.asSubscriptionProviderUser())
                    }

                    AdaptyPurchaseResult.Pending -> {
                        Result.failure(Exception("Purchase is pending"))
                    }

                    AdaptyPurchaseResult.UserCanceled -> {
                        Result.failure(Exception("Purchase was canceled"))
                    }
                }
            }
        }
    }

    override suspend fun restorePurchase(): Result<SubscriptionProviderUser> {
        return Adapty.restorePurchases().fold(
            onSuccess = { adaptyProfile ->
                val isActiveAccessLevelsEmpty =
                    adaptyProfile.accessLevels.filter { it.value.isActive }.isEmpty()
                if (isActiveAccessLevelsEmpty) Result.failure(Exception("Restore failed. No active subscription found"))
                else Result.success(adaptyProfile.asSubscriptionProviderUser())
            },
            onError = { error -> Result.failure(error) }
        )
    }

    private fun AdaptyProfile.asSubscriptionProviderUser(): SubscriptionProviderUser {
        val grantedAccesses = accessLevels.filter { it.value.isActive }.mapValues { (id, access) ->
            GrantedAccess(
                id = id,
                expirationDateMillis = access.expiresAt.asTimeInMilliseconds(),
                willRenew = access.willRenew,
                productIdentifier = access.vendorProductId,
                isLifetime = access.isLifetime,
            )
        }
        return SubscriptionProviderUser(
            grantedAccesses = grantedAccesses,
            activeSubscriptionIds = subscriptions.keys
        )
    }

    private inline fun <T, R> AdaptyResult<T>.asResult(onSuccess: (T) -> R): Result<R> = this.fold(
        onSuccess = { resultData -> Result.success(onSuccess(resultData)) },
        onError = { error -> Result.failure(error) }
    )

    @OptIn(ExperimentalTime::class)
    private fun String?.asTimeInMilliseconds(): Long? = runCatching {
        if (this == null) return null

        val cleaned = this
            .replace(Regex("\\.(\\d{3})\\d*"), ".$1") // keep only 3 digits of millis
            .replace(Regex("([+-]\\d{2})(\\d{2})$"), "$1:$2") // convert +0000 to +00:00

        val instant = Instant.parse(cleaned)
        instant.toEpochMilliseconds()

    }.getOrNull()

}