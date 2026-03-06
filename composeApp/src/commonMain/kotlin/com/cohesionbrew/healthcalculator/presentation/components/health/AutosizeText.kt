package com.cohesionbrew.healthcalculator.presentation.components.health

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * A composable that automatically resizes its text to fit within its bounds.
 * Ported from old VitalsCalc core.presentation.components.
 */
@Composable
fun AutosizeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurface,
    textAlign: TextAlign? = null,
    targetFontSize: TextUnit = 12.sp,
    fontWeight: FontWeight? = null
) {
    var fontSize by remember(text) { mutableStateOf(targetFontSize) }
    var hasFitted by remember(text) { mutableStateOf(false) }

    Text(
        text = text,
        color = color,
        modifier = modifier,
        textAlign = textAlign,
        fontWeight = fontWeight,
        style = LocalTextStyle.current.copy(fontSize = fontSize),
        softWrap = false,
        maxLines = 1,
        onTextLayout = { textLayoutResult ->
            if (!hasFitted && textLayoutResult.didOverflowWidth) {
                fontSize *= 0.95f
            } else {
                hasFitted = true
            }
        }
    )
}
