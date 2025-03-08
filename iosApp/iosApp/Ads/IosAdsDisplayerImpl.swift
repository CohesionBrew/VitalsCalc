//
//  IosAdsDisplayerImpl.swift
//  iosApp
//
//  Created by Mirzamehdi on 07/03/2025.
//

import Foundation
import ComposeApp
import SwiftUI
import GoogleMobileAds


class IosAdsDisplayerImpl: IosAdsDisplayer {
    
    func provideInterstitialAdDisplayer(adLoader: FullScreenAdLoader) -> FullScreenAdDisplayer {
        return InterstitialAdDisplayer(adLoader: adLoader)
    }

    func provideRewardedAdDisplayer(adLoader: FullScreenAdLoader) -> FullScreenAdDisplayer {
        return RewardedAdDisplayer(adLoader: adLoader)
    }
}
