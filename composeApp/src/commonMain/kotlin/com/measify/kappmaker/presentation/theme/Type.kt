package com.measify.kappmaker.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.poppins_bold
import com.measify.kappmaker.generated.resources.poppins_medium
import com.measify.kappmaker.generated.resources.poppins_regular
import com.measify.kappmaker.generated.resources.poppins_semibold


private val fontFamily
    @Composable get() = FontFamily(
        Font(
            Res.font.poppins_regular,
            FontWeight.Normal,
            FontStyle.Normal
        ),
        Font(
            Res.font.poppins_medium,
            FontWeight.Medium,
            FontStyle.Normal
        ),
        Font(
            Res.font.poppins_semibold,
            FontWeight.SemiBold,
            FontStyle.Normal
        ),
        Font(
            Res.font.poppins_bold,
            FontWeight.Bold,
            FontStyle.Normal
        )
    )


// Default Material 3 typography values
private val baseline = Typography()

val AppTypography
    @Composable
    get() = Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = fontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = fontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = fontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = fontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = fontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = fontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = fontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = fontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = fontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = fontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = fontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = fontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = fontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = fontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = fontFamily),
    )

