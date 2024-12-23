package com.measify.kappmaker.util

import com.measify.kappmaker.BuildConfig
import com.measify.kappmaker.data.source.local.DatabaseProvider
import com.measify.kappmaker.data.source.local.DatabaseProviderImpl
import com.measify.kappmaker.util.inappreview.InAppReviewManager
import com.measify.kappmaker.util.inappreview.InAppReviewManagerImpl
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal actual val platformModule: Module = module {
    singleOf(::DatabaseProviderImpl) bind DatabaseProvider::class
    factoryOf(::InAppReviewManagerImpl) bind InAppReviewManager::class
}

internal actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = android.R.drawable.ic_menu_compass
        )
    )

}

internal actual val isAndroid = true
internal actual val isDebug = BuildConfig.DEBUG