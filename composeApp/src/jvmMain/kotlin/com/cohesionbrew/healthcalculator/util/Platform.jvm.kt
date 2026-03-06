package com.cohesionbrew.healthcalculator.util

import com.cohesionbrew.healthcalculator.data.source.featureflag.FeatureFlagManager
import com.cohesionbrew.healthcalculator.data.source.featureflag.NoImplFeatureFlagManager
import com.cohesionbrew.healthcalculator.data.source.local.DatabaseProvider
import com.cohesionbrew.healthcalculator.data.source.local.DatabaseProviderImpl
import com.cohesionbrew.healthcalculator.data.source.local.nonWebModule
import com.cohesionbrew.healthcalculator.presentation.components.ads.AdsManager
import com.cohesionbrew.healthcalculator.presentation.components.ads.NoImplAdsManager
import com.cohesionbrew.healthcalculator.domain.widget.NoOpWidgetUpdater
import com.cohesionbrew.healthcalculator.domain.widget.WidgetUpdater
import com.cohesionbrew.healthcalculator.util.analytics.Analytics
import com.cohesionbrew.healthcalculator.util.analytics.NoImplAnalytics
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
    single<WidgetUpdater> { NoOpWidgetUpdater() }
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
