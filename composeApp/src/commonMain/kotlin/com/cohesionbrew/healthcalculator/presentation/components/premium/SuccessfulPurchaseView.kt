package com.cohesionbrew.healthcalculator.presentation.components.premium

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.cohesionbrew.healthcalculator.designsystem.components.premium.PremiumFeatureUiState
import com.cohesionbrew.healthcalculator.designsystem.components.premium.SuccessfulPurchaseContent
import com.cohesionbrew.healthcalculator.util.Constants.subscriptionUrl
import com.cohesionbrew.healthcalculator.util.inappreview.rememberInAppReviewTrigger
import com.cohesionbrew.healthcalculator.util.logging.AppLogger
import com.cohesionbrew.healthcalculator.util.logging.logSuccessfulPurchase

@Composable
fun SuccessfulPurchaseView(
    features: List<PremiumFeatureUiState> = emptyList(),
    expirationDate: String? = null,
    isLifetime: Boolean = false,
    isRecurring: Boolean = true,
    modifier: Modifier = Modifier,
    onContinue: () -> Unit = {}
) {

    val inAppReviewTrigger = rememberInAppReviewTrigger()
    LaunchedEffect(Unit) {
        AppLogger.logSuccessfulPurchase()
        inAppReviewTrigger.triggerAfterSuccessfulPurchase()
    }

    SuccessfulPurchaseContent(
        subscriptionUrl = subscriptionUrl,
        features = features,
        expirationDate = expirationDate,
        isLifetime = isLifetime,
        isRecurring = isRecurring,
        modifier = modifier,
        onContinue = onContinue
    )
}

