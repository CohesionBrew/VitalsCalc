package com.measify.kappmaker.presentation.components.premium

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.lifetime_premium_subscription_renewal_text
import com.measify.kappmaker.generated.resources.premium_subscription_non_recurring_text
import com.measify.kappmaker.generated.resources.premium_subscription_renewal_text
import com.measify.kappmaker.generated.resources.text_here
import com.measify.kappmaker.presentation.theme.AppTheme
import com.measify.kappmaker.util.isAndroid
import org.jetbrains.compose.resources.stringResource

@Composable
fun ManageSubscriptionText(
    isLifetime: Boolean = false,
    expirationDate: String? = null,
    productId: String? = null,
    isRecurring: Boolean = true,
) {

    val uriHandler = LocalUriHandler.current
    //TODO Fix below
    val subscriptionUrl =
        if (isAndroid) "https://play.google.com/store/account/subscriptions"
        else "https://apps.apple.com/account/subscriptions"

    val annotatedString = when {
        isLifetime -> buildAnnotatedString {
            append(stringResource(Res.string.lifetime_premium_subscription_renewal_text))
        }

        expirationDate != null -> {
            val textRes =
                if (isRecurring) Res.string.premium_subscription_renewal_text else Res.string.premium_subscription_non_recurring_text
            val text = stringResource(textRes, expirationDate)
            val hereText = stringResource(Res.string.text_here)
            val clickableTextStyle = SpanStyle(
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                color = AppTheme.colors.primary
            )
            val nextBillingDateTextStyle = SpanStyle(
                fontWeight = FontWeight.SemiBold
            )

            buildAnnotatedString {
                append(text.substringBefore(expirationDate))
                withStyle(style = nextBillingDateTextStyle) {
                    append(expirationDate)
                }
                val remainingText = text.substringAfter(expirationDate)
                append(remainingText.substringBefore(hereText))
                withStyle(style = clickableTextStyle) {
                    withLink(LinkAnnotation.Clickable(hereText, null) {
                        uriHandler.openUri(subscriptionUrl)
                    }) {
                        append(hereText)
                    }

                }
                append(remainingText.substringAfter(hereText))
            }
        }

        else -> buildAnnotatedString { }

    }
    Text(
        text = annotatedString,
        style = AppTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = AppTheme.colors.text.secondary
    )

}