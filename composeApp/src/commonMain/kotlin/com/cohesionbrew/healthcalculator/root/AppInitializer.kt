package com.cohesionbrew.healthcalculator.root

import com.cohesionbrew.healthcalculator.auth.api.AuthServiceProvider
import com.cohesionbrew.healthcalculator.auth.api.AuthServiceProviderFactory
import com.cohesionbrew.healthcalculator.common.BuildConfig
import com.cohesionbrew.healthcalculator.data.BackgroundExecutor
import com.cohesionbrew.healthcalculator.data.repository.CreditRepository
import com.cohesionbrew.healthcalculator.data.repository.HistoryRepository
import com.cohesionbrew.healthcalculator.data.repository.HistoryRepositoryImpl
import com.cohesionbrew.healthcalculator.data.repository.SubscriptionRepository
import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepository
import com.cohesionbrew.healthcalculator.data.repository.UserProfileRepositoryImpl
import com.cohesionbrew.healthcalculator.data.repository.UserRepository
import com.cohesionbrew.healthcalculator.data.source.featureflag.FeatureFlagManager
import com.cohesionbrew.healthcalculator.data.source.preferences.UserPreferences
import com.cohesionbrew.healthcalculator.data.source.preferences.UserPreferencesImpl
import com.cohesionbrew.healthcalculator.data.source.remote.HttpClientFactory
import com.cohesionbrew.healthcalculator.data.source.remote.apiservices.ApiService
import com.cohesionbrew.healthcalculator.data.source.remote.apiservices.ai.OpenAiApiService
import com.cohesionbrew.healthcalculator.data.source.remote.apiservices.ai.ReplicateApiService
import com.cohesionbrew.healthcalculator.domain.model.credit.creditSystemConfig
import com.cohesionbrew.healthcalculator.presentation.components.ads.AdsManager
import com.cohesionbrew.healthcalculator.presentation.screens.about.AboutUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.account.AccountUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure.BloodPressureUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.bmi.BmiUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.bmr.BmrUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.bodyfat.BodyFatUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.calculators.CalculatorsUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.heartrate.HeartRateUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.history.HistoryUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.home.HomeUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.idealweight.IdealWeightUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.more.MoreUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.onboarding.OnBoardingUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.paywall.PaywallUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.profile.ProfileUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.references.ReferencesUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.settings.SettingsUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.subscriptions.SubscriptionsUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.userprofile.UserProfileUiStateHolder
import com.cohesionbrew.healthcalculator.presentation.screens.waterintake.WaterIntakeUiStateHolder
import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProvider
import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProviderFactory
import com.cohesionbrew.healthcalculator.subscription.api.SubscriptionProviderUi
import com.cohesionbrew.healthcalculator.util.ApplicationScope
import com.cohesionbrew.healthcalculator.util.Constants
import com.cohesionbrew.healthcalculator.util.analytics.Analytics
import com.cohesionbrew.healthcalculator.util.defaultAsyncDispatcher
import com.cohesionbrew.healthcalculator.util.isAndroid
import com.cohesionbrew.healthcalculator.util.isDebug
import com.cohesionbrew.healthcalculator.domain.premium.FeatureAccessManager
import com.cohesionbrew.healthcalculator.util.logging.AppLogger
import com.cohesionbrew.healthcalculator.util.logging.Logger
import com.cohesionbrew.healthcalculator.util.logging.NapierLogger
import com.cohesionbrew.healthcalculator.util.logging.TelegramLogger
import com.cohesionbrew.healthcalculator.util.onApplicationStartPlatformSpecific
import com.cohesionbrew.healthcalculator.util.platformModule
import com.mmk.kmpauth.google.GoogleAuthCredentials
import com.mmk.kmpauth.google.GoogleAuthProvider
import com.mmk.kmpnotifier.notification.NotifierManager
import com.mmk.kmpnotifier.notification.PayloadData
import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
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

        val userRepository by this.koin.inject<UserRepository>()
        userRepository.signInAnonymouslyIfNecessary()

    }
}

private fun KoinApplication.refreshFeatureFlags() {
    val featureFlagManager by this.koin.inject<FeatureFlagManager>()
    featureFlagManager.syncsFlagsAsync()
}

private fun KoinApplication.initializeAnalytics() {
    val featureFlagManager by this.koin.inject<FeatureFlagManager>()
    val analytics by this.koin.inject<Analytics>()
    val isAnalyticsEnabled =
        featureFlagManager.getBoolean(FeatureFlagManager.Keys.IS_ANALYTICS_ENABLED)
    analytics.setEnabled(enabled = isAnalyticsEnabled)
}

private fun KoinApplication.initializeAds() {
    val backgroundScope = CoroutineScope(defaultAsyncDispatcher)
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
         * This method is invoked when receiving a push notification.
         * @param title parameter contains the notification title
         * @param body parameter contains the notification body
         * @param data parameter contains the payload data sent with the notification
         */
        override fun onPushNotificationWithPayloadData(
            title: String?,
            body: String?,
            data: PayloadData
        ) {
            super.onPushNotificationWithPayloadData(title, body, data)
            AppLogger.d("Firebase onPushNotification: title: $title, body: $body, data: $data")
        }
    })
}

private fun initializeAuthentication() {
    GoogleAuthProvider.create(credentials = GoogleAuthCredentials(serverId = BuildConfig.GOOGLE_WEB_CLIENT_ID))
}

private fun KoinApplication.initializeInAppPurchase() {
    val subscriptionProvider by this.koin.inject<SubscriptionProvider>()
    val applicationScope by this.koin.inject<ApplicationScope>()

    applicationScope.launch(Dispatchers.Main.immediate) {
        val subscriptionProviderApiKey =
            if (isAndroid) BuildConfig.SUBSCRIPTION_PROVIDER_ANDROID_API_KEY else BuildConfig.SUBSCRIPTION_PROVIDER_IOS_API_KEY
        subscriptionProvider.initialize(subscriptionProviderApiKey)
    }
}

private fun Module.initializeCreditSystem() {
    single {
        val subscriptionRepository = get<SubscriptionRepository>()
        val appCreditSystemConfig = creditSystemConfig {

            //Give 2 credits to new users one time
//            oneTimeBonus("welcome_bonus", 2)
//
//            //Give 2 credits per week for free users
//            recurringWeekly(
//                id = "free_plan_weekly",
//                amount = 2,
//                condition = {
//                    !subscriptionRepository.hasPremiumAccess()
//                }
//            )
//
//            // Give 10 credits per week for premium users
//            recurringWeekly(
//                id = "premium_plan_weekly",
//                amount = 10,
//                condition = {
//                    subscriptionRepository.hasPremiumAccess()
//                }
//            )
//
//            // Give 1 credit per day for premium users
//            recurringDaily(
//                id = "premium_plan_daily",
//                amount = 1,
//                condition = {
//                    subscriptionRepository.hasPremiumAccess()
//                }
//            )
        }

        CreditRepository(appCreditSystemConfig, get(), get(), get(), get())
    }
}

private val domainModule = module {
    single { FeatureAccessManager(get(), get()) }
}

private val dataModule = module {
    singleOf(::ApplicationScope)
    factory { defaultAsyncDispatcher } bind CoroutineContext::class
    factory { BackgroundExecutor.IO } bind BackgroundExecutor::class

    //Preferences Source
    single { Settings() } bind Settings::class
    singleOf(::UserPreferencesImpl) bind UserPreferences::class

    //Remote source
    single { HttpClientFactory.default(get()) }
    factoryOf(::ApiService)

    //AI API services
    factoryOf(::OpenAiApiService)
    factoryOf(::ReplicateApiService)


    //Auth provider
    factory { Constants.authServiceProviderFactory } bind AuthServiceProviderFactory::class
    single { get<AuthServiceProviderFactory>().create() } bind AuthServiceProvider::class

    //Subscription Provider
    factory { Constants.subscriptionProviderFactory } bind SubscriptionProviderFactory::class
    single { get<SubscriptionProviderFactory>().createProvider() } bind SubscriptionProvider::class
    factory { get<SubscriptionProviderFactory>().createProviderUi() } bind SubscriptionProviderUi::class

    //Repositories
    single { UserRepository(get(), get(), get(), get(), get()) }
    single { SubscriptionRepository(get(), get(), get(), get()) }
    single { HistoryRepositoryImpl(get()) } bind HistoryRepository::class
    single { UserProfileRepositoryImpl(get()) } bind UserProfileRepository::class

    //Loggers
    factory { TelegramLogger(get(), get(), get()) } bind Logger::class
    factory { NapierLogger() } bind Logger::class

    initializeCreditSystem()
}

private val presentationModule = module {
    viewModelOf(::OnBoardingUiStateHolder)
    viewModelOf(::HomeUiStateHolder)
    viewModelOf(::ProfileUiStateHolder)
    viewModelOf(::PaywallUiStateHolder)
    viewModelOf(::AccountUiStateHolder)
    viewModelOf(::SubscriptionsUiStateHolder)
    viewModelOf(::CalculatorsUiStateHolder)
    viewModelOf(::HistoryUiStateHolder)
    viewModelOf(::MoreUiStateHolder)
    viewModelOf(::BmiUiStateHolder)
    viewModelOf(::BmrUiStateHolder)
    viewModelOf(::HeartRateUiStateHolder)
    viewModelOf(::BodyFatUiStateHolder)
    viewModelOf(::IdealWeightUiStateHolder)
    viewModelOf(::WaterIntakeUiStateHolder)
    viewModelOf(::BloodPressureUiStateHolder)
    viewModelOf(::SettingsUiStateHolder)
    viewModelOf(::UserProfileUiStateHolder)
    viewModelOf(::AboutUiStateHolder)
    viewModelOf(::ReferencesUiStateHolder)
}
private val appModules: List<Module> get() = platformModule + domainModule + dataModule + presentationModule
        
