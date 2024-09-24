package com.measify.kappbuilder.data.repository

import com.measify.kappbuilder.data.BackgroundExecutor
import com.measify.kappbuilder.domain.exceptions.UnAuthorizedException
import com.measify.kappbuilder.domain.model.User
import com.measify.kappbuilder.util.Constants.PAYWALL_PREMIUM_ENTITLEMENT
import com.measify.kappbuilder.util.logging.AppLogger
import com.revenuecat.purchases.kmp.CacheFetchPolicy
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.ktx.awaitCustomerInfo
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val backgroundExecutor: BackgroundExecutor = BackgroundExecutor.IO) {

    fun getUser(): Flow<Result<User>> {
        return Firebase.auth.authStateChanged.map { currentUser ->
            if (currentUser == null) Result.failure(UnAuthorizedException())
            else {
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

        }
    }

    suspend fun hasPremiumAccess(): Boolean = hasEntitlementAccess(PAYWALL_PREMIUM_ENTITLEMENT)

    suspend fun hasEntitlementAccess(key: String): Boolean = backgroundExecutor.execute {
        val customerInfo =
            Purchases.sharedInstance.awaitCustomerInfo(fetchPolicy = CacheFetchPolicy.CACHED_OR_FETCHED)
        val hasPremium =
            customerInfo.entitlements.all[key]?.isActive ?: false
        Result.success(hasPremium)
    }.getOrNull() ?: false


    suspend fun logOut() = backgroundExecutor.execute {
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



