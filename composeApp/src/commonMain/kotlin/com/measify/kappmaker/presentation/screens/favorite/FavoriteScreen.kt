package com.measify.kappmaker.presentation.screens.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.measify.kappmaker.presentation.components.ExampleNativeTextView
import com.measify.kappmaker.presentation.components.PrimaryButton
import com.measify.kappmaker.presentation.components.ads.AdmobBanner
import com.measify.kappmaker.presentation.components.ads.rememberInterstitialAdDisplayer
import com.measify.kappmaker.presentation.components.ads.rememberRewardedAdDisplayer
import com.measify.kappmaker.util.logging.AppLogger

@Composable
fun FavoriteScreen(modifier: Modifier = Modifier, onPaymentRequired: () -> Unit) {
    val interstitialAdDisplayer = rememberInterstitialAdDisplayer()
    val rewardedAdDisplayer = rememberRewardedAdDisplayer(onRewarded = {rewardItem ->
        AppLogger.d("User earned reward: amount: ${rewardItem.amount}, type: ${rewardItem.type}")
    })
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //For Native view we have to pass width, height params to the view.
        ExampleNativeTextView(
            text = "Native Text View",
            modifier = Modifier.fillMaxWidth().height(56.dp)
        )
        PrimaryButton(text = "Get Premium Content") {
            onPaymentRequired()
        }
        PrimaryButton(text = "Show Interstitial Ads") {
            interstitialAdDisplayer?.show()
        }
        PrimaryButton(text = "Show Rewarded Ads") {
            rewardedAdDisplayer?.show()
        }
        Spacer(modifier = Modifier.weight(1f))
        AdmobBanner(modifier = Modifier.fillMaxWidth())
    }
}