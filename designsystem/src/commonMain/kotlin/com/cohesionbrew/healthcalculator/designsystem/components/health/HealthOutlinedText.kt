package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

@Composable
fun HealthOutlinedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color,
    outlineColor: Color,
    fontWeight: FontWeight? = null,
    fontSize: TextUnit,
    textAlign: TextAlign = TextAlign.Center
) {
    val boxAlignment = when (textAlign) {
        TextAlign.Start -> Alignment.CenterStart
        TextAlign.End -> Alignment.CenterEnd
        else -> Alignment.Center
    }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = boxAlignment
    ) {
        Text(
            text = text,
            color = outlineColor,
            fontWeight = fontWeight,
            fontSize = fontSize,
            textAlign = textAlign,
            style = TextStyle.Default.copy(
                drawStyle = Stroke(
                    miter = 10f,
                    width = 4f,
                    join = StrokeJoin.Round
                )
            )
        )
        Text(
            text = text,
            color = color,
            fontWeight = fontWeight,
            fontSize = fontSize,
            textAlign = textAlign
        )
    }
}
