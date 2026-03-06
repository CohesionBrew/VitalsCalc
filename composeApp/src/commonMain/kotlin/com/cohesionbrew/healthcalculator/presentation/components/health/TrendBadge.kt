package com.cohesionbrew.healthcalculator.presentation.components.health

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.cohesionbrew.healthcalculator.generated.resources.*
import kotlin.math.absoluteValue
import org.jetbrains.compose.resources.stringResource

enum class TrendDirection {
    UP, DOWN, STABLE
}

@Composable
fun TrendBadge(
    direction: TrendDirection,
    percentChange: Float,
    invertColors: Boolean = false,
    modifier: Modifier = Modifier
) {
    val colors = rememberHealthStatusColors()

    val icon = when (direction) {
        TrendDirection.UP -> Icons.Filled.KeyboardArrowUp
        TrendDirection.DOWN -> Icons.Filled.KeyboardArrowDown
        TrendDirection.STABLE -> Icons.Filled.KeyboardArrowUp // won't be shown
    }

    val tint = when (direction) {
        TrendDirection.UP -> if (invertColors) colors.healthy else colors.alert
        TrendDirection.DOWN -> if (invertColors) colors.alert else colors.healthy
        TrendDirection.STABLE -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val pct = percentChange.absoluteValue.toInt()
    val accessibilityText = when (direction) {
        TrendDirection.UP -> stringResource(Res.string.trending_up, pct)
        TrendDirection.DOWN -> stringResource(Res.string.trending_down, pct)
        TrendDirection.STABLE -> stringResource(Res.string.stable_trend)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.semantics(mergeDescendants = true) {
            contentDescription = accessibilityText
        }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
        if (direction != TrendDirection.STABLE) {
            Spacer(Modifier.width(2.dp))
            Text(
                text = "${percentChange.absoluteValue.toInt()}%",
                style = MaterialTheme.typography.labelSmall,
                color = tint
            )
        }
    }
}
