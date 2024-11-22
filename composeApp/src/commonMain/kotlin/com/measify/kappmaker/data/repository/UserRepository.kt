package com.measify.kappmaker.data.repository

import com.measify.kappmaker.data.BackgroundExecutor
import com.measify.kappmaker.domain.exceptions.UnAuthorizedException
import com.measify.kappmaker.domain.model.User
import com.measify.kappmaker.util.ApplicationScope
import com.measify.kappmaker.util.Constants.PAYWALL_PREMIUM_ENTITLEMENT
import com.measify.kappmaker.util.logging.AppLogger
import com.revenuecat.purchases.kmp.models.CacheFetchPolicy
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class UserRepository(
    private val backgroundExecutor: BackgroundExecutor = BackgroundExecutor.IO,
    private val applicationScope: ApplicationScope
) {

    val currentUser: SharedFlow<Result<User>> = Firebase.auth.authStateChanged.map { currentUser ->
        if (currentUser == null) {
            Purchases.sharedInstance.logOut(onError = {}, onSuccess = {})
            Result.failure(UnAuthorizedException())
        } else {
            Purchases.sharedInstance.logIn(
                newAppUserID = currentUser.uid,
                onSuccess = { _, _ -> },
                onError = {
                    AppLogger.e("Error occurred while login user for purchase")
                }
            )
            val user = User(
                id = currentUser.uid,
                email = currentUser.email,
                hasPremiumAccess = hasPremiumAccess()
            )

            Result.success(user)
        }

    }.shareIn(applicationScope, SharingStarted.Lazily, 1)


    suspend fun hasPremiumAccess(): Boolean = hasEntitlementAccess(PAYWALL_PREMIUM_ENTITLEMENT)

    suspend fun hasEntitlementAccess(key: String): Boolean = backgroundExecutor.execute {
        val customerInfo =
            Purchases.sharedInstance.awaitCustomerInfo(fetchPolicy = CacheFetchPolicy.CACHED_OR_FETCHED)
        val hasPremium =
            customerInfo.entitlements.all[key]?.isActive ?: false
        Result.success(hasPremium)
    }.getOrNull() ?: false


    suspend fun logOut() = backgroundExecutor.execute {
        Purchases.sharedInstance.logOut(onError = {}, onSuccess = {})
        Firebase.auth.signOut()
        Result.success(Unit)
    }

    suspend fun deleteAccount() = backgroundExecutor.execute {
        val currentUser = Firebase.auth.currentUser
        //Here you can send delete request to the server if needed

        currentUser?.delete()
        logOut()
        Result.success(Unit)
    }


}



