package com.measify.kappmaker.root

import com.measify.kappmaker.BuildConfig
import com.measify.kappmaker.data.BackgroundExecutor
import com.measify.kappmaker.data.repository.UserRepository
import com.measify.kappmaker.data.source.preferences.UserPreferences
import com.measify.kappmaker.data.source.preferences.UserPreferencesImpl
import com.measify.kappmaker.data.source.remote.HttpClientFactory
import com.measify.kappmaker.data.source.remote.apiservices.ApiService
import com.measify.kappmaker.presentation.screens.onboarding.OnBoardingUiStateHolder
import com.measify.kappmaker.presentation.screens.paywall.PaywallUiStateHolder
import com.measify.kappmaker.presentation.screens.profile.ProfileUiStateHolder
import com.measify.kappmaker.util.ApplicationScope
import com.measify.kappmaker.util.isAndroid
import com.measify.kappmaker.util.logging.AppLogger
import com.measify.kappmaker.util.onApplicationStartPlatformSpecific
import com.measify.kappmaker.util.platformModule
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.revenuecat.purchases.kmp.Purchases
import com.revenuecat.purchases.kmp.PurchasesConfiguration
import com.russhwolf.settings.Settings
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

    //Preferences Source
    single { Settings() } bind Settings::class
    singleOf(::UserPreferencesImpl) bind UserPreferences::class

    //Remote source
    single { HttpClientFactory.default() }
    factoryOf(::ApiService)

    //Repositories
    single { UserRepository(get(), get()) }
}

private val presentationModule = module {
    factoryOf(::OnBoardingUiStateHolder)
    factoryOf(::ProfileUiStateHolder)
    factoryOf(::PaywallUiStateHolder)
}
private val appModules: List<Module> get() = platformModule + domainModule + dataModule + presentationModule
        
