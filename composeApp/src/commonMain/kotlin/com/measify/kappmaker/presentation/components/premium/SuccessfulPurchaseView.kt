package com.measify.kappmaker.presentation.components.premium

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.benefits
import com.measify.kappmaker.generated.resources.btn_action_successful_purchase
import com.measify.kappmaker.generated.resources.confetti
import com.measify.kappmaker.generated.resources.ic_premium_feature_crown
import com.measify.kappmaker.generated.resources.subtitle_successful_purchase
import com.measify.kappmaker.generated.resources.title_successful_purchase
import com.measify.kappmaker.presentation.components.AppButton
import com.measify.kappmaker.presentation.components.Divider
import com.measify.kappmaker.presentation.components.PreviewHelper
import com.measify.kappmaker.presentation.components.ScreenTitle
import com.measify.kappmaker.presentation.components.SectionContainer
import com.measify.kappmaker.presentation.theme.AppTheme
import com.measify.kappmaker.util.inappreview.rememberInAppReviewTrigger
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

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
        inAppReviewTrigger.triggerAfterSuccessfulPurchase()
    }

    val statusBarHeight = with(LocalDensity.current) {
        WindowInsets.statusBars.getTop(this).toDp()
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .zIndex(4f)
            .background(AppTheme.colors.background)
            .padding(
                start = AppTheme.spacing.outerSpacing,
                end = AppTheme.spacing.outerSpacing,
                bottom = AppTheme.spacing.outerSpacing
            ),
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .offset(y = -statusBarHeight)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(Res.drawable.confetti),
                contentDescription = null
            )

            SectionContainer {
                Image(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(Res.drawable.ic_premium_feature_crown),
                    contentDescription = null
                )
            }

            SectionContainer {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.groupedVerticalElementSpacing)
                ) {

                    ScreenTitle(text = stringResource(Res.string.title_successful_purchase))
                    Text(
                        stringResource(Res.string.subtitle_successful_purchase),
                        textAlign = TextAlign.Center,
                        style = AppTheme.typography.bodyExtraLarge,
                        color = AppTheme.colors.text.secondary
                    )
                }
            }

            Divider(modifier = Modifier.fillMaxWidth())

            if (features.isNotEmpty()) {
                SectionContainer {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(Res.string.benefits),
                            style = AppTheme.typography.h4,
                            color = AppTheme.colors.text.primary
                        )
                        Spacer(modifier = Modifier.height(AppTheme.spacing.verticalListItemSpacingSmall))
                        PremiumFeaturesList(features = features)
                    }

                }
                Divider(modifier = Modifier.fillMaxWidth())
            }
            ManageSubscriptionText(
                expirationDate = expirationDate,
                isLifetime = isLifetime,
                isRecurring = isRecurring,
            )


        }

        AppButton(
            text = stringResource(Res.string.btn_action_successful_purchase),
            modifier = Modifier.fillMaxWidth(),
            onClick = { onContinue() }
        )

    }

}

@Composable
fun SuccessfulPurchasePreview() {
    PreviewHelper {
        SuccessfulPurchaseView(
            features = listOf(
                PremiumFeatureUiState(text = "Feature 1", isIncluded = true),
                PremiumFeatureUiState(text = "Feature 2", isIncluded = true),
                PremiumFeatureUiState(text = "Feature 3", isIncluded = true),
                PremiumFeatureUiState(text = "Feature 4", isIncluded = false),
            ),
            modifier = Modifier.height(900.dp),
            expirationDate = "27 September 2024",
            onContinue = {}
        )
    }

}