package com.measify.kappmaker.util.inappreview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import platform.StoreKit.SKStoreReviewController

class InAppReviewManagerImpl : InAppReviewManager {

    @Composable
    override fun requestReview() {
        LaunchedEffect(Unit) {
            SKStoreReviewController.requestReview()
        }
    }
}