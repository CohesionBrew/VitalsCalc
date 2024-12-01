package com.measify.kappmaker.util

import com.measify.kappmaker.data.source.local.DatabaseProvider
import com.measify.kappmaker.data.source.local.DatabaseProviderImpl
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.experimental.ExperimentalNativeApi

internal actual val platformModule: Module = module {
    singleOf(::DatabaseProviderImpl) bind DatabaseProvider::class
}

internal actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(NotificationPlatformConfiguration.Ios())

}

internal actual val isAndroid = false
@OptIn(ExperimentalNativeApi::class)
internal actual val isDebug = Platform.isDebugBinary