package com.measify.kappmaker.presentation.components.premium

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.measify.kappmaker.domain.model.Subscription
import com.measify.kappmaker.domain.model.isFree
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.ic_check
import com.measify.kappmaker.generated.resources.ic_close
import com.measify.kappmaker.presentation.components.PreviewHelper
import com.measify.kappmaker.presentation.theme.AppTheme
import org.jetbrains.compose.resources.vectorResource

data class PremiumFeatureUiState(
    val text: String,
    val isIncluded: Boolean = true,
){
    companion object {
        //TODO Add default features here
        val defaultFreeFeatures = listOf(
            PremiumFeatureUiState("Access to free features only"),
            PremiumFeatureUiState("Access to premium features", isIncluded = false),
        )
        val defaultPremiumFeatures = listOf(
            PremiumFeatureUiState("Access to premium features"),
        )

        fun ofSubscription(subscription: Subscription?): List<PremiumFeatureUiState> {
            return if (subscription.isFree) defaultFreeFeatures
            else (subscription?.benefits?.map { PremiumFeatureUiState(it) } ?: emptyList())
                .ifEmpty { defaultPremiumFeatures }
        }
    }
}

@Composable
fun PremiumFeaturesList(
    features: List<PremiumFeatureUiState> = emptyList(),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.verticalListItemSpacingSmall)
    ) {

        features.forEach { feature ->
            val icon = if (feature.isIncluded) Res.drawable.ic_check else Res.drawable.ic_close
            val iconTint =
                if (feature.isIncluded) AppTheme.colors.text.primary else AppTheme.colors.status.error
            val textColor =
                if (feature.isIncluded) AppTheme.colors.text.primary else AppTheme.colors.text.primary.copy(
                    alpha = 0.5f
                )
            val textDecoration =
                if (feature.isIncluded) TextDecoration.None else TextDecoration.LineThrough
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.inputIconTextSpacing)
            ) {
                Icon(
                    imageVector = vectorResource(icon),
                    tint = iconTint,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )

                Text(
                    text = feature.text,
                    style = AppTheme.typography.bodyExtraLarge,
                    textDecoration = textDecoration,
                    color = textColor,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun PremiumFeaturesListPreview() {
    PreviewHelper {
        PremiumFeaturesList(
            features = listOf(
                PremiumFeatureUiState(text = "Feature 1", isIncluded = true),
                PremiumFeatureUiState(text = "Feature 2", isIncluded = true),
                PremiumFeatureUiState(text = "Feature 3", isIncluded = true),
                PremiumFeatureUiState(text = "Feature 4", isIncluded = false),
                PremiumFeatureUiState(text = "Feature 5", isIncluded = false),
            )
        )
    }


}