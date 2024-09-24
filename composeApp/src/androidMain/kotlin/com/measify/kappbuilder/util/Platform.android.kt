package com.measify.kappbuilder.util

import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.configuration.NotificationPlatformConfiguration

import org.koin.core.module.Module
import org.koin.dsl.module

internal actual val platformModule: Module = module {

}

internal actual fun onApplicationStartPlatformSpecific() {
    NotifierManager.initialize(
        configuration = NotificationPlatformConfiguration.Android(
            notificationIconResId = android.R.drawable.ic_menu_compass
        )
    )

}

internal actual val isAndroid = true