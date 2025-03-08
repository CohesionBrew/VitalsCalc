package com.measify.kappmaker.presentation.components.ads

interface IosAdsDisplayer {

    fun provideInterstitialAdDisplayer(adLoader: FullScreenAdLoader): FullScreenAdDisplayer
    fun provideRewardedAdDisplayer(adLoader: FullScreenAdLoader): FullScreenAdDisplayer
}