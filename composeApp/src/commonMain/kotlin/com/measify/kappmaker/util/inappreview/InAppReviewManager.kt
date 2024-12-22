package com.measify.kappmaker.util.inappreview

import androidx.compose.runtime.Composable
import org.koin.compose.koinInject


@Composable
fun rememberInAppReviewManager(): InAppReviewManager {
    val reviewManager = koinInject<InAppReviewManager>()
    return reviewManager
}

interface InAppReviewManager {

    @Composable
    fun requestReview()
}