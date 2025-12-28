package com.measify.kappmaker.util.inappreview

import androidx.compose.runtime.Composable

@Composable
actual fun rememberInAppReviewManager(): InAppReviewManager {
    return NoImplInAppReviewManager
}