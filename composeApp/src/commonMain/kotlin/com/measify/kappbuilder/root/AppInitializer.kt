package com.measify.kappbuilder.root

import com.measify.kappbuilder.BuildConfig
import com.measify.kappbuilder.data.BackgroundExecutor
import com.measify.kappbuilder.data.repository.UserRepository
import com.measify.kappbuilder.data.source.remote.HttpClientFactory
import com.measify.kappbuilder.data.source.remote.apiservices.ApiService
import com.measify.kappbuilder.presentation.screens.paywall.PaywallUiStateHolder
import com.measify.kappbuilder.presentation.screens.profile.ProfileUiStateHolder
import com.measify.kappbuilder.util.ApplicationScope
import com.measify.kappbuilder.util.isAndroid
import com.measify.kappbuilder.util.logging.AppLogger
import com.measify.kappbuilder.util.onApplicationStartPlatformSpecific
import com.measify.kappbuilder.util.platformModule
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.coroutines.CoroutineContext

object AppInitializer {

    fun initialize(onKoinStart: KoinApplication.() -> Unit) {
        startKoin {
            onKoinStart()
            modules(appModules)
            onApplicationStart()
        }
    }

    private fun KoinApplication.onApplicationStart() {
        onApplicationStartPlatformSpecific()
        //Add common application start functions here
        AppLogger.initialize(isDebug = true)

        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                super.onNewToken(token)
                AppLogger.d("Firebase onNewToken: $token")
            }

            override fun onNotificationClicked(data: PayloadData) {
                super.onNotificationClicked(data)
                AppLogger.d("onNotification clicked: $data")

            }

            override fun onPayloadData(data: PayloadData) {
                super.onPayloadData(data)
                AppLogger.d("Firebase notification onPayloadData: $data")

            }

            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                AppLogger.d("Firebase onPushNotification: title: $title, body: $body")

            }
        })

        GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = BuildConfig.GOOGLE_WEB_CLIENT_ID))
        val revenueCatApiKey =
            if (isAndroid) BuildConfig.REVENUECAT_ANDROID_API_KEY else BuildConfig.REVENUECAT_IOS_API_KEY
        Purchases.configure(PurchasesConfiguration(apiKey = revenueCatApiKey))
    }
}

private val domainModule = module {

}

private val dataModule = module {

    singleOf(::ApplicationScope)
    factory { Dispatchers.IO } bind CoroutineContext::class
    factory { BackgroundExecutor.IO } bind BackgroundExecutor::class

    //Remote source
    single { HttpClientFactory.default() }
    factoryOf(::ApiService)

    //Repositories
    factory { UserRepository(get()) }
}

private val presentationModule = module {
    factoryOf(::ProfileUiStateHolder)
    factoryOf(::PaywallUiStateHolder)
}
private val appModules: List<Module> get() = platformModule + domainModule + dataModule + presentationModule
        
