package com.measify.kappmaker.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.measify.kappmaker.util.Constants
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.and
import com.measify.kappmaker.generated.resources.privacy_policy
import com.measify.kappmaker.generated.resources.terms_conditions
import com.measify.kappmaker.generated.resources.txt_agree_privacy_policy_and_terms
import org.jetbrains.compose.resources.stringResource

//TODO: Uncomment below when composer version 1.7.x is stable

//@Composable
//fun AgreePrivacyPolicyTermsConditionsText(modifier: Modifier) {
//    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
//        val privacyPolicy = stringResource(Res.string.privacy_policy)
//        val termsConditions = stringResource(Res.string.terms_conditions)
//        val customStyle = SpanStyle(
//            fontWeight = FontWeight.SemiBold,
//            color = MaterialTheme.colorScheme.secondary,
//            textDecoration = TextDecoration.Underline
//        )
//        val annotatedString = buildAnnotatedString {
//            append(stringResource(Res.string.txt_agree_privacy_policy_and_terms))
//
//            withStyle(style = customStyle) {
//                withLink(LinkAnnotation.Url(url = Constants.URL_PRIVACY_POLICY)) {
//                    append(privacyPolicy)
//                }
//            }
//            append(" ${stringResource(Res.string.and)} ")
//            withStyle(customStyle) {
//                withLink(LinkAnnotation.Url(url = Constants.URL_TERMS_CONDITIONS)) {
//                    append(termsConditions)
//                }
//            }
//
//        }
//        Text(text = annotatedString, style = MaterialTheme.typography.bodyMedium)
//    }
//}

//TODO:Replace with above code when composer version 1.7.x is stable
@Composable
fun AgreePrivacyPolicyTermsConditionsText(modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        val privacyPolicy = stringResource(Res.string.privacy_policy)
        val termsConditions = stringResource(Res.string.terms_conditions)
        val localUriHandler = LocalUriHandler.current
        val customStyle = SpanStyle(
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.secondary,
            textDecoration = TextDecoration.Underline
        )
        val annotatedString = buildAnnotatedString {
            append(stringResource(Res.string.txt_agree_privacy_policy_and_terms))
            withStyle(style = customStyle) {
                pushStringAnnotation(
                    tag = privacyPolicy,
                    annotation = privacyPolicy
                )
                append(privacyPolicy)
            }
            append(" ${stringResource(Res.string.and)} ")
            withStyle(style = customStyle) {
                pushStringAnnotation(
                    tag = termsConditions,
                    annotation = termsConditions
                )
                append(termsConditions)
            }

        }

        ClickableText(
            text = annotatedString, style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
        ) { offset ->
            annotatedString.getStringAnnotations(offset, offset)
                .firstOrNull()?.let { span ->
                    when (span.item) {
                        privacyPolicy -> localUriHandler.openUri(Constants.URL_PRIVACY_POLICY)
                        termsConditions -> localUriHandler.openUri(Constants.URL_TERMS_CONDITIONS)
                    }
                }
        }
    }
}