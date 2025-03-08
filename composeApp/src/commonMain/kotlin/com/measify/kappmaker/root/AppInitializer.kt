package com.measify.kappmaker.root

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.measify.kappmaker.common.BuildConfig
import com.measify.kappmaker.data.BackgroundExecutor
import com.measify.kappmaker.data.repository.UserRepository
import com.measify.kappmaker.data.source.featureflag.FeatureFlagManager
import com.measify.kappmaker.data.source.local.AppDatabase
import com.measify.kappmaker.data.source.local.DatabaseProvider
import com.measify.kappmaker.data.source.preferences.UserPreferences
import com.measify.kappmaker.data.source.preferences.UserPreferencesImpl
import com.measify.kappmaker.data.source.remote.HttpClientFactory
import com.measify.kappmaker.data.source.remote.apiservices.ApiService
import com.measify.kappmaker.presentation.components.ads.AdsManager
import com.measify.kappmaker.presentation.screens.onboarding.OnBoardingUiStateHolder
import com.measify.kappmaker.presentation.screens.paywall.PaywallUiStateHolder
import com.measify.kappmaker.presentation.screens.profile.ProfileUiStateHolder
import com.measify.kappmaker.util.ApplicationScope
import com.measify.kappmaker.util.analytics.Analytics
import com.measify.kappmaker.util.isAndroid
import com.measify.kappmaker.util.isDebug
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
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
        //Add common application start functions below

        //initialize logging
        AppLogger.initialize(isDebug = isDebug)

        refreshFeatureFlags()
        initializeAnalytics()
        initializeNotification()
        initializeAuthentication()
        initializeInAppPurchase()
        initializeAds()

    }
}

private fun KoinApplication.refreshFeatureFlags() {
    val featureFlagManager by this.koin.inject<FeatureFlagManager>()
    featureFlagManager.syncsFlagsAsync()
}

private fun KoinApplication.initializeAnalytics() {
    val featureFlagManager by this.koin.inject<FeatureFlagManager>()
    val analytics by this.koin.inject<Analytics>()
    val isAnalyticsEnabled = featureFlagManager.getBoolean(FeatureFlagManager.Keys.IS_ANALYTICS_ENABLED)
    analytics.setEnabled(enabled = isAnalyticsEnabled)
}

private fun KoinApplication.initializeAds() {
    val backgroundScope = CoroutineScope(Dispatchers.IO)
    val adsManager by this.koin.inject<AdsManager>()
    val featureFlagManager by this.koin.inject<FeatureFlagManager>()
    val isAdsEnabled = featureFlagManager.getBoolean(FeatureFlagManager.Keys.IS_ADS_ENABLED)
    if (isAdsEnabled.not()) return

    //Initialize ads
    backgroundScope.launch {
        adsManager.initialize()
    }
}

private fun initializeNotification() {
    NotifierManager.addListener(object : NotifierManager.Listener {

        /**
         * This method is called when a new FCM token is generated.
         * You can use this token for sending notifications to the specific device or saving in the server.
         * It is logged for debugging purposes.
         */
        override fun onNewToken(token: String) {
            super.onNewToken(token)
            AppLogger.d("Firebase onNewToken: $token")
        }

        /**
         * This method is invoked when the user clicks on a notification.
         * @param data parameter contains the payload data sent with the notification
         */
        override fun onNotificationClicked(data: PayloadData) {
            super.onNotificationClicked(data)
            AppLogger.d("onNotification clicked: $data")

        }

        /**
         * This method is invoked when receiving a data type notification.
         * @param data parameter contains the payload data sent with the notification
         */
        override fun onPayloadData(data: PayloadData) {
            super.onPayloadData(data)
            AppLogger.d("Firebase notification onPayloadData: $data")

        }

        /**
         * This method is invoked when receiving a notification
         */
        override fun onPushNotification(title: String?, body: String?) {
            super.onPushNotification(title, body)
            AppLogger.d("Firebase onPushNotification: title: $title, body: $body")

        }
    })
}

private fun initializeAuthentication() {
    GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = BuildConfig.GOOGLE_WEB_CLIENT_ID))
}

private fun initializeInAppPurchase() {
    val revenueCatApiKey =
        if (isAndroid) BuildConfig.REVENUECAT_ANDROID_API_KEY else BuildConfig.REVENUECAT_IOS_API_KEY
    Purchases.configure(PurchasesConfiguration(apiKey = revenueCatApiKey))
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

    //Local Source
    single<AppDatabase> {
        val databaseProvider = get<DatabaseProvider>()
        databaseProvider.provideAppDatabaseBuilder()
            .fallbackToDestructiveMigration(dropAllTables = true)
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
    single { get<AppDatabase>().exampleDao() }

    //Repositories
    single { UserRepository(get(), get()) }
}

private val presentationModule = module {
    factoryOf(::OnBoardingUiStateHolder)
    factoryOf(::ProfileUiStateHolder)
    factoryOf(::PaywallUiStateHolder)
}
private val appModules: List<Module> get() = platformModule + domainModule + dataModule + presentationModule
        
