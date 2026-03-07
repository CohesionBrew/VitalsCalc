package com.cohesionbrew.healthcalculator.designsystem.theme


import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color


/**
 * In case you don't know what colors to choose, choose any color from Material color palette,
 * then use material color shade generator, set 900 as primary, and 50 as alternative.
 *
 * For supporting colors, based on primary color HSB values, keep  S, and B values the same or
 * 5-10 difference, and for H value based on status set like green, yellow, red, blue
 *
 * @param primary: Primary color for the application
 * @param onPrimary: Text color on the primary color
 * @param alternative: Subtle color elements that is less important than the primary colored action
 * @param onAlternative: Text color on the subtle color
 * @param background: Background color for the application
 * @param surfaceContainer: Used for background color of dialog, bottomsheet
 * @param text: General text colors used in the application
 * @param textInput: Text input colors used in the application
 * @param bottomNav: Bottom navigation bar colors used in the application
 * @param outline: Outline colors used in the application. Ex: border colors, divider colors, inactive indicator colors, etc.
 * @param status: Status colors used in the application. Default values are usually okay for most cases.
 */
@Immutable
data class AppColors(
    val primary: Color = Color.Unspecified,
    val onPrimary: Color = Color.Unspecified,
    val alternative: Color = Color.Unspecified,
    val onAlternative: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val surfaceContainer: Color = Color.Unspecified,
    val text: Text = Text(),
    val status: Status = Status(),
    val textInput: TextInput = TextInput(),
    val bottomNav: BottomNav = BottomNav(),
    val outline: Color = Color.Unspecified,
) {

    //Backup for Material Color Scheme
    fun asMaterialColorScheme(isDark: Boolean): androidx.compose.material3.ColorScheme {
        return if (isDark) darkColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = alternative,
            onPrimaryContainer = onAlternative,
            error = status.error,
            errorContainer = status.errorContainer,
            background = background,
            surfaceContainer = surfaceContainer,
            surfaceContainerHighest = surfaceContainer,
            surfaceContainerLow = surfaceContainer,
            onSurface = text.primary,
            onBackground = text.primary,
            surfaceVariant = Color(0xFF43474E),
            onSurfaceVariant = Color(0xFFC4C6CF),
            outline = outline,
            outlineVariant = Color(0xFF43474E),
        ) else lightColorScheme(
            primary = primary,
            onPrimary = onPrimary,
            primaryContainer = alternative,
            onPrimaryContainer = onAlternative,
            surfaceContainer = surfaceContainer,
            surfaceContainerHighest = surfaceContainer,
            surfaceContainerLow = surfaceContainer,
            error = status.error,
            errorContainer = status.errorContainer,
            background = background,
            onSurface = text.primary,
            onBackground = text.primary,
            surfaceVariant = Color(0xFFE0E2EC),
            onSurfaceVariant = Color(0xFF43474E),
            outline = outline,
            outlineVariant = Color(0xFFC4C6CF),
        )
    }

    @Immutable
    data class Status(
        val error: Color = Color(0xFFF75555),
        val errorContainer: Color = Color(0xFFFFEFED),
        val warning: Color = Color(0xFFFACC15),
        val warningContainer: Color = Color(0xFFFFFBEB),
        val info: Color = Color(0xFF235DFF),
        val infoContainer: Color = Color(0xFF235DFF).copy(alpha = 0.08f),
        val success: Color = Color(0xFF12D18E),
        val successContainer: Color = Color(0xFFEBF8F3),
    )

    @Immutable
    data class TextInput(
        val textIcon: Color = Color.Unspecified,
        val background: Color = Color.Unspecified,
        val placeholder: Color = Color.Unspecified,
        val readOnlyBackground: Color = Color.Unspecified,
        val disabledTextIcon: Color = Color.Unspecified,
        val disabledBackground: Color = Color.Unspecified,
    ) {
        companion object {
            val DefaultLight = TextInput(
                textIcon = Color.GRAY_900,
                background = Color.GRAY_50,
                placeholder = Color.GRAY_500,
                readOnlyBackground = Color.GRAY_100,
                disabledTextIcon = Color.GRAY_600,
                disabledBackground = Color(0xFFD8D8D8)
            )
            val DefaultDark = TextInput(
                textIcon = Color.White,
                background = Color.BLACK_3_HEX_1F,
                placeholder = Color.GRAY_500,
                readOnlyBackground = Color.BLACK_2_HEX_1E,
                disabledBackground = Color(0xFF23252B),
                disabledTextIcon = Color.GRAY_600
            )
        }
    }


    /**
     * @param primary: Primary text color, used for important text, such as title
     * @param secondary: Secondary text color, used for less important text, such as description
     */
    @Immutable
    data class Text(
        val primary: Color = Color.Unspecified,
        val secondary: Color = Color.Unspecified,
    ) {
        companion object {
            val DefaultLight = Text(
                primary = Color.GRAY_900,
                secondary = Color.GRAY_700
            )
            val DefaultDark = Text(
                primary = Color.White,
                secondary = Color.GRAY_200
            )
        }
    }

    /**
     * @param background: Background color for the bottom navigation bar
     * @param selectedTextIcon: Text color for the selected item
     * @param unselectedTextIcon: Text color for the unselected item
     */
    @Immutable
    data class BottomNav(
        val background: Color = Color.Unspecified,
        val selectedTextIcon: Color = Color.Unspecified,
        val unselectedTextIcon: Color = Color.Unspecified,
    ) {
        companion object {
            val DefaultLight = BottomNav(
                background = Color.White,
                selectedTextIcon = PrimaryColor,
                unselectedTextIcon = Color.GRAY_500
            )
            val DefaultDark = BottomNav(
                background = Color.BLACK_1_HEX_18,
                selectedTextIcon = Color.White,
                unselectedTextIcon = Color.GRAY_500
            )
        }
    }


}

val PrimaryColor = Color(0xFF3F5F90)
val PrimaryAlpha8Color = Color(0xFFD6E3FF) // primaryContainer from old VitalsCalc

val lightModeAppColors = AppColors(
    primary = PrimaryColor,                    // #3F5F90
    onPrimary = Color.White,
    alternative = PrimaryAlpha8Color,          // #D6E3FF (primaryContainer)
    onAlternative = Color(0xFF264777),         // onPrimaryContainer
    background = Color(0xFFF9F9FF),            // old app background
    surfaceContainer = Color(0xFFEDEDF4),      // old surfaceContainerLight
    text = AppColors.Text(
        primary = Color(0xFF191C20),           // onBackgroundLight
        secondary = Color(0xFF43474E),         // onSurfaceVariantLight
    ),
    textInput = AppColors.TextInput.DefaultLight,
    bottomNav = AppColors.BottomNav(
        background = Color(0xFFF9F9FF),        // match background
        selectedTextIcon = PrimaryColor,
        unselectedTextIcon = Color.GRAY_500
    ),
    outline = Color(0xFF74777F),               // outlineLight from old app
)

val darkModeAppColors = AppColors(
    primary = Color(0xFFA8C8FF),               // primaryDark from old app
    onPrimary = Color(0xFF07305F),             // onPrimaryDark
    alternative = Color(0xFF264777),           // primaryContainerDark
    onAlternative = Color(0xFFD6E3FF),         // onPrimaryContainerDark
    background = Color(0xFF111318),            // old app dark background
    surfaceContainer = Color(0xFF1D2024),      // surfaceContainerDark from old
    text = AppColors.Text(
        primary = Color(0xFFE2E2E9),           // onBackgroundDark
        secondary = Color(0xFFC4C6CF),         // onSurfaceVariantDark
    ),
    textInput = AppColors.TextInput.DefaultDark,
    bottomNav = AppColors.BottomNav(
        background = Color(0xFF111318),        // match background
        selectedTextIcon = Color(0xFFA8C8FF),  // primaryDark
        unselectedTextIcon = Color.GRAY_500
    ),
    outline = Color(0xFF8E9099),               // outlineDark from old
)




