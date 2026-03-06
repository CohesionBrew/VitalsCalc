package com.cohesionbrew.healthcalculator.util

import com.cohesionbrew.healthcalculator.data.source.featureflag.FeatureFlagManager
import com.cohesionbrew.healthcalculator.presentation.components.ads.AdsManager
import com.cohesionbrew.healthcalculator.presentation.components.ads.IosAdsDisplayer
import com.cohesionbrew.healthcalculator.util.analytics.Analytics

/**
This factory is used to help to use swift libraries in KMP. Actual implementations are provided in swift.
 */
interface SwiftLibDependencyFactory {
    fun provideFeatureFlagManagerImpl(): FeatureFlagManager
    fun provideFirebaseAnalyticsImpl(): Analytics
    fun provideAdsManagerImpl(): AdsManager
    fun provideIosAdsDisplayer(): IosAdsDisplayer
}