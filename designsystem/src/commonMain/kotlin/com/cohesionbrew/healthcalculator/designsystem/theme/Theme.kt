package com.cohesionbrew.healthcalculator.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import com.cohesionbrew.healthcalculator.designsystem.util.isIos
import io.github.robinpcrd.cupertino.adaptive.AdaptiveTheme
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.robinpcrd.cupertino.adaptive.Theme
import io.github.robinpcrd.cupertino.theme.CupertinoTheme


val LocalThemeIsDark = compositionLocalOf { true }
val LocalAppColors = staticCompositionLocalOf { lightModeAppColors }
val LocalAppTypography =
    staticCompositionLocalOf<AppTypography> { error("Typography not provided") }
val LocalAppSpacing = staticCompositionLocalOf { AppSpacing() }

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AppTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val target = if (isIos) Theme.Cupertino else Theme.Material3

    CompositionLocalProvider(
        LocalThemeIsDark provides isDarkMode,
        LocalAppColors provides if (isDarkMode) darkModeAppColors else lightModeAppColors,
        LocalAppTypography provides appTypography,
        LocalAppSpacing provides appSpacing
    ) {
        SystemAppearance(isDarkMode)

        AdaptiveTheme(
            material = { materialContent ->
                MaterialTheme(
                    colorScheme = LocalAppColors.current.asMaterialColorScheme(isDarkMode),
                    typography = MaterialThemAppTypography,
                    content = { materialContent() }
                )
            },
            cupertino = { cupertinoContent ->
                CupertinoTheme {
                    MaterialTheme(
                        colorScheme = LocalAppColors.current.asMaterialColorScheme(isDarkMode),
                        typography = MaterialThemAppTypography,
                        content = { cupertinoContent() }
                    )
                }
            },
            target = target,
        ) {
            Surface(
                content = content,
                color = AppTheme.colors.background
            )
        }
    }
}


object AppTheme {

    val colors: AppColors
        @Composable @ReadOnlyComposable get() = LocalAppColors.current

    val typography: AppTypography
        @Composable @ReadOnlyComposable get() = LocalAppTypography.current

    val spacing: AppSpacing
        @Composable @ReadOnlyComposable get() = LocalAppSpacing.current
}

@Composable
internal expect fun SystemAppearance(isDark: Boolean)
