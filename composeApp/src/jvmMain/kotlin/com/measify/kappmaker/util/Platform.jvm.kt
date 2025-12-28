package com.measify.kappmaker.util

import com.measify.kappmaker.data.source.featureflag.FeatureFlagManager
import com.measify.kappmaker.data.source.featureflag.NoImplFeatureFlagManager
import com.measify.kappmaker.data.source.local.DatabaseProvider
import com.measify.kappmaker.data.source.local.DatabaseProviderImpl
import com.measify.kappmaker.data.source.local.nonWebModule
import com.measify.kappmaker.presentation.components.ads.AdsManager
import com.measify.kappmaker.presentation.components.ads.NoImplAdsManager
import com.measify.kappmaker.util.analytics.Analytics
import com.measify.kappmaker.util.analytics.NoImplAnalytics
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    includes(nonWebModule)
    singleOf(::DatabaseProviderImpl) bind DatabaseProvider::class
    factoryOf(::AppUtilImpl) bind AppUtil::class
    single { NoImplFeatureFlagManager } bind FeatureFlagManager::class
    single { NoImplAnalytics } bind Analytics::class
    single { NoImplAdsManager } bind AdsManager::class
}

internal actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(NotificationPlatformConfiguration.Desktop())
}

internal actual val isAndroid: Boolean
    get() = false
internal actual val isDebug: Boolean
    get() = System.getProperty("app.debug") == "true"
        || System.getenv("APP_DEBUG") == "true"

actual val defaultAsyncDispatcher: CoroutineDispatcher = Dispatchers.IO
