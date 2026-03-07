package com.cohesionbrew.healthcalculator.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.UiRes
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.acme_regular
import org.jetbrains.compose.resources.Font


private val fontFamily
    @Composable get() = FontFamily(
        Font(
            UiRes.font.acme_regular,
            FontWeight.Normal,
            FontStyle.Normal
        ),
    )


/**
 * App typography values
 * @param h2: Used for example app name in splash screen
 * @param h3: h3 can be used in screens like big title, where it is like one part of screen.
 * For example, in Forgot Password screen or OnBoarding Screen title, this big text can use h3
 *
 * @param h4: Most used title. For example toolbar title, card title, dialog title, etc.
 * @param h5: Mostly used in section titles
 */
@Immutable
class AppTypography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val h5: TextStyle,
    val h6: TextStyle,
    val bodyExtraLarge: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val bodyExtraSmall: TextStyle,
)


val MaterialThemAppTypography
    @Composable
    get() = Typography().copy(
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 20.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 22.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
        )
    )

val appTypography
    @Composable get() = AppTypography(
        h1 = TextStyle(
            fontFamily = fontFamily,
            letterSpacing = 0.sp,
            fontWeight = FontWeight.Bold,
            fontSize = 48.sp,
            lineHeight = 67.sp
        ),
        h2 = TextStyle(
            letterSpacing = 0.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            lineHeight = 56.sp

        ),
        h3 = TextStyle(
            letterSpacing = 0.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 45.sp
        ),
        h4 = TextStyle(
            letterSpacing = 0.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            lineHeight = 34.sp
        ),
        h5 = TextStyle(
            letterSpacing = 0.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            lineHeight = 28.sp
        ),
        h6 = TextStyle(
            letterSpacing = 0.sp,
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 25.sp
        ),
        bodyExtraLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp,
            fontSize = 18.sp,
            lineHeight = 29.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp,
            fontSize = 16.sp,
            lineHeight = 26.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp,
            fontSize = 14.sp,
            lineHeight = 22.sp
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp,
            fontSize = 12.sp,
            lineHeight = 19.sp
        ),
        bodyExtraSmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            letterSpacing = 0.2.sp,
            fontSize = 10.sp,
            lineHeight = 16.sp
        ),
    )
