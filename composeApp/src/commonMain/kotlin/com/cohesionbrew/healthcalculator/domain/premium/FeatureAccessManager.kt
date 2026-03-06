package com.cohesionbrew.healthcalculator.domain.premium

import com.cohesionbrew.healthcalculator.data.repository.SubscriptionRepository
import com.cohesionbrew.healthcalculator.util.ApplicationScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class FeatureAccessManager(
    private val subscriptionRepository: SubscriptionRepository,
    private val applicationScope: ApplicationScope
) {
    val isPro: Flow<Boolean> = flow {
        emit(subscriptionRepository.hasPremiumAccess())
    }.shareIn(applicationScope, SharingStarted.Lazily, 1)

    fun hasAccess(feature: PremiumFeature): Flow<Boolean> {
        return isPro.map { pro ->
            when (feature) {
                PremiumFeature.AD_FREE -> pro
                PremiumFeature.UNLIMITED_HISTORY -> pro
                PremiumFeature.CHARTS -> pro
                PremiumFeature.HEALTH_SYNC -> pro
                PremiumFeature.FULL_WIDGETS -> pro
                PremiumFeature.DATA_EXPORT -> pro
            }
        }
    }

    fun getHistoryDaysLimit(isPro: Boolean): Int? {
        return if (isPro) null else 30
    }

    companion object {
        const val FREE_HISTORY_DAYS = 30
    }
}
