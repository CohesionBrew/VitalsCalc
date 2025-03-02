package com.measify.kappmaker.util

import com.measify.kappmaker.data.source.featureflag.FeatureFlagManager

/**
This factory is used to help to use swift libraries in KMP. Actual implementations are provided in swift.
 */
interface SwiftLibDependencyFactory {
    fun provideFeatureFlagManagerImpl(): FeatureFlagManager
}