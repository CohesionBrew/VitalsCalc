package com.cohesionbrew.healthcalculator.presentation.screens.bmi.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cohesionbrew.healthcalculator.generated.resources.*
import com.cohesionbrew.healthcalculator.designsystem.components.AutoResizableText
import org.jetbrains.compose.resources.stringResource

/**
 * Data class representing a BMI category for the dynamic graphic chart.
 */
data class BmiGraphicCategory(
    val range: String,
    val color: Color,
    val label: String
)

/**
 * Position of a segment in the arrow chart.
 */
private enum class SegmentPosition {
    START, MIDDLE, END
}

/**
 * Builds the list of BMI categories with localized labels.
 */
@Composable
fun getBmiCategoriesForDynamicGraphic(): List<BmiGraphicCategory> {
    return listOf(
        BmiGraphicCategory(
            range = "< 18.5",
            color = Color(0xFF1E88E5),
            label = stringResource(Res.string.underweight)
        ),
        BmiGraphicCategory(
            range = "18.5 - 24.9",
            color = Color(0xFF4CAF50),
            label = stringResource(Res.string.healthy_weight)
        ),
        BmiGraphicCategory(
            range = "25 - 29.9",
            color = Color(0xFFFDD835),
            label = stringResource(Res.string.overweight)
        ),
        BmiGraphicCategory(
            range = "30 - 34.9",
            color = Color(0xFFFB8C00),
            label = stringResource(Res.string.obese_class_i)
        ),
        BmiGraphicCategory(
            range = "35 - 39.9",
            color = Color(0xFFD32F2F),
            label = stringResource(Res.string.obese_class_ii)
        ),
        BmiGraphicCategory(
            range = "> 40",
            color = Color(0xFFB71C1C),
            label = stringResource(Res.string.obese_class_iii)
        )
    )
}

/**
 * A composable that displays a complete BMI chart with category indicators,
 * colored arrow bars, and translatable labels.
 *
 * @param currentBmi The user's current BMI value, used to highlight the active category.
 * @param modifier The modifier to be applied to the chart container.
 */
@Composable
fun BmiDynamicGraphicsChart(
    currentBmi: Double,
    modifier: Modifier = Modifier
) {
    val categories = getBmiCategoriesForDynamicGraphic()

    // Determine which category is the current one based on the BMI value.
    val currentCategory = categories.find { cat ->
        val parts = cat.range.split("-").map { it.trim() }
        try {
            when {
                parts.size == 1 && parts[0].startsWith("<") -> currentBmi < parts[0].removePrefix("<")
                    .trim().toFloat()
                parts.size == 1 && parts[0].startsWith(">") -> currentBmi > parts[0].removePrefix(">")
                    .trim().toFloat()
                parts.size == 2 -> currentBmi >= parts[0].toFloat() && currentBmi <= parts[1].toFloat()
                else -> false
            }
        } catch (e: NumberFormatException) {
            false
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val targetLabelSize = if (this.maxWidth > 500.dp) 16.sp else 12.sp

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Row for the person icons and highlight circles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                categories.forEach { category ->
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        PersonIcon(
                            isCurrent = category == currentCategory,
                            color = category.color
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row for the colored arrow segments with BMI ranges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                categories.forEachIndexed { index, category ->
                    val position = when (index) {
                        0 -> SegmentPosition.START
                        categories.lastIndex -> SegmentPosition.END
                        else -> SegmentPosition.MIDDLE
                    }
                    ArrowSegment(
                        category = category,
                        position = position,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Row for the translatable category labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                categories.forEach { category ->
                    AutoResizableText(
                        text = category.label,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 2.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = targetLabelSize,
                            fontWeight = if (category == currentCategory) FontWeight.Bold else FontWeight.Normal
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * Displays the person icon with an optional highlight circle.
 */
@Composable
private fun PersonIcon(isCurrent: Boolean, color: Color) {
    val iconColor = LocalContentColor.current.copy(alpha = 0.8f)
    Box(contentAlignment = Alignment.Center) {
        if (isCurrent) {
            Canvas(modifier = Modifier.size(40.dp)) {
                drawCircle(
                    color = color,
                    style = Stroke(width = 6f)
                )
            }
        }
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = if (isCurrent) color else iconColor
        )
    }
}

/**
 * Draws a single colored segment of the BMI chart with the correct arrow shape.
 */
@Composable
private fun ArrowSegment(
    category: BmiGraphicCategory,
    position: SegmentPosition,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val width = size.width
            val height = size.height
            val arrowWidth = 20f

            val path = Path().apply {
                when (position) {
                    SegmentPosition.START -> {
                        moveTo(0f, 0f)
                        lineTo(width - arrowWidth, 0f)
                        lineTo(width, height / 2)
                        lineTo(width - arrowWidth, height)
                        lineTo(0f, height)
                        close()
                    }
                    SegmentPosition.MIDDLE -> {
                        moveTo(0f, height / 2)
                        lineTo(arrowWidth, 0f)
                        lineTo(width - arrowWidth, 0f)
                        lineTo(width, height / 2)
                        lineTo(width - arrowWidth, height)
                        lineTo(arrowWidth, height)
                        close()
                    }
                    SegmentPosition.END -> {
                        moveTo(0f, height / 2)
                        lineTo(arrowWidth, 0f)
                        lineTo(width, 0f)
                        lineTo(width, height)
                        lineTo(arrowWidth, height)
                        close()
                    }
                }
            }
            drawPath(path = path, color = category.color)
        }
        Text(
            text = category.range,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}
