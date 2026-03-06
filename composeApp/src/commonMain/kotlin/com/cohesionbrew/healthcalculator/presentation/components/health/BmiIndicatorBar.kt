package com.cohesionbrew.healthcalculator.presentation.components.health

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private val bmiRanges = listOf(
    Triple(0f, 18.5f, Color(0xFF2196F3)),     // Underweight - Blue
    Triple(18.5f, 25f, Color(0xFF4CAF50)),     // Normal - Green
    Triple(25f, 30f, Color(0xFFFFC107)),       // Overweight - Amber
    Triple(30f, 35f, Color(0xFFFF9800)),       // Obese I - Orange
    Triple(35f, 40f, Color(0xFFFF5722)),       // Obese II - Deep Orange
    Triple(40f, 50f, Color(0xFFF44336)),       // Obese III - Red
)

@Composable
fun BmiIndicatorBar(
    bmiValue: Double,
    categoryLabel: String,
    modifier: Modifier = Modifier,
    minBmi: Float = 10f,
    maxBmi: Float = 45f
) {
    val clampedBmi = bmiValue.toFloat().coerceIn(minBmi, maxBmi)

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = categoryLabel,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp)
        )

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
        ) {
            val barWidth = size.width
            val barHeight = size.height
            val totalRange = maxBmi - minBmi

            // Draw colored segments
            bmiRanges.forEach { (rangeStart, rangeEnd, color) ->
                val segStart = ((rangeStart.coerceAtLeast(minBmi) - minBmi) / totalRange) * barWidth
                val segEnd = ((rangeEnd.coerceAtMost(maxBmi) - minBmi) / totalRange) * barWidth
                if (segEnd > segStart) {
                    drawRoundRect(
                        color = color,
                        topLeft = Offset(segStart, 0f),
                        size = Size(segEnd - segStart, barHeight),
                        cornerRadius = CornerRadius(4f)
                    )
                }
            }

            // Draw indicator needle
            val indicatorX = ((clampedBmi - minBmi) / totalRange) * barWidth
            drawCircle(
                color = Color.White,
                radius = barHeight / 2f + 2f,
                center = Offset(indicatorX, barHeight / 2f)
            )
            drawCircle(
                color = Color.DarkGray,
                radius = barHeight / 2f - 1f,
                center = Offset(indicatorX, barHeight / 2f)
            )
        }
    }
}
