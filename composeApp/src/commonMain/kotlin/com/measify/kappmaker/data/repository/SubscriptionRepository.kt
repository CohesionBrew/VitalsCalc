package com.measify.kappmaker.data.repository

import com.measify.kappmaker.data.BackgroundExecutor
import com.measify.kappmaker.data.source.preferences.UserPreferences
import com.measify.kappmaker.domain.model.Subscription
import com.measify.kappmaker.subscription.api.PurchasePackage
import com.measify.kappmaker.subscription.api.PurchasePackageId
import com.measify.kappmaker.subscription.api.SubscriptionProvider
import com.measify.kappmaker.subscription.api.SubscriptionProviderUser
import com.measify.kappmaker.subscription.api.hasAccess
import com.measify.kappmaker.util.ApplicationScope
import com.measify.kappmaker.util.Constants.PAYWALL_PREMIUM_ACCESS
import com.measify.kappmaker.util.analytics.Analytics
import com.measify.kappmaker.util.logging.AppLogger
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class SubscriptionRepository(
    private val applicationScope: ApplicationScope,
    private val subscriptionProvider: SubscriptionProvider,
    private val userPreferences: UserPreferences,
    private val analytics: Analytics,
    private val backgroundExecutor: BackgroundExecutor = BackgroundExecutor.IO
) {

    val currentSubscriptionFlow: Flow<Subscription?> =
        subscriptionProvider.currentSubscriptionProviderUserFlow
            .flatMapLatest { subscriptionProviderUser ->
                flow {
                    if (subscriptionProviderUser == null) {
                        emit(null)
                    } else {
                        //Initial fast emit value without extra details
                        emit(subscriptionProviderUser.asPremiumSubscription(retrieveExtraDetails = false))
                        emit(subscriptionProviderUser.asPremiumSubscription(retrieveExtraDetails = true))
                    }
                }

            }
            .catch { error ->
                AppLogger.e("Error occurred while getting current subscription", error)
            }
            .flowOn(backgroundExecutor.scope)
            .shareIn(applicationScope, SharingStarted.Eagerly, 1)


    var currentPlacementId: String? = null

    suspend fun login(userId: String) {
        subscriptionProvider.login(userId).onFailure {
            AppLogger.e("Error occurred while logging in for subscription provider", it)
        }
    }

    suspend fun logOut() {
        subscriptionProvider.logout().onFailure {
            AppLogger.e("Error occurred while logging out for subscription provider", it)
        }
    }

    suspend fun hasPremiumAccess(): Boolean = hasAccess(PAYWALL_PREMIUM_ACCESS)

    suspend fun hasAccess(key: String): Boolean = subscriptionProvider.hasAccess(key = key)


    fun onPaywallDismissed() = applicationScope.launch {
        currentPlacementId = null
        val nbTimesPaywallIsDismissed =
            userPreferences.getInt(UserPreferences.KEY_NB_PAYWALL_DISMISSED, 0) ?: 0
        val newNbTimesPaywallIsDismissed = nbTimesPaywallIsDismissed + 1
        userPreferences.putInt(
            UserPreferences.KEY_NB_PAYWALL_DISMISSED,
            newNbTimesPaywallIsDismissed
        )
        analytics.logEvent(Analytics.EVENT_PAYWALL_DISMISSED)
        subscriptionProvider.setCustomAttributes(
            mapOf(Analytics.PARAM_NB_PAYWALL_DISMISSED to newNbTimesPaywallIsDismissed)
        )
    }

    suspend fun getPackageList(placementId: String? = null): Result<List<PurchasePackage>> =
        subscriptionProvider.getPurchasePackages(placementId = placementId)
            .map { purchasePackages ->
                purchasePackages.sortedBy { it.price.amount }
            }


    suspend fun purchase(purchasePackageId: PurchasePackageId): Result<SubscriptionProviderUser> =
        subscriptionProvider.purchase(purchasePackageId)

    suspend fun restorePurchase(): Result<SubscriptionProviderUser> =
        subscriptionProvider.restorePurchase()

    suspend fun SubscriptionProviderUser.asPremiumSubscription(retrieveExtraDetails: Boolean = true): Subscription? {
        if (retrieveExtraDetails) {
            //TODO Fix
            return null

//            return getActiveSubscriptionList(subscriptionProviderUser = this).find { it.accessId in PREMIUM_ACCESS_ID_LIST }
        } else {
            val grantedAccess = grantedAccesses.values.firstOrNull()
            return grantedAccess?.let {
                Subscription(
                    accessId = grantedAccess.id,
                    expirationDateInMillis = grantedAccess.expirationDateMillis,
                    willRenew = grantedAccess.willRenew
                )
            }
        }
    }

    companion object {
        val PREMIUM_ACCESS_ID_LIST = listOf(PAYWALL_PREMIUM_ACCESS)
    }

}