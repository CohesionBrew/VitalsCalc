package com.cohesionbrew.healthcalculator.presentation.components.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardMetricCard(
    title: String,
    value: String?,
    unit: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    icon: ImageVector? = null,
    statusColor: Color? = null,
    isElevated: Boolean = true,
    trendDirection: TrendDirection? = null,
    trendPercentChange: Float? = null,
    isPro: Boolean = false,
    onClick: (() -> Unit)? = null
) {
    val hasData = value != null
    val cardAlpha = if (hasData) 1f else 0.6f
    val accessibilityText = buildString {
        append(title)
        if (hasData) {
            append(": $value $unit")
            subtitle?.let { append(", $it") }
        } else {
            append(": No data yet")
        }
    }

    val cardModifier = modifier
        .fillMaxWidth()
        .defaultMinSize(minHeight = 100.dp)
        .alpha(cardAlpha)
        .semantics(mergeDescendants = true) {
            contentDescription = accessibilityText
        }

    val cardContent: @Composable () -> Unit = {
        DashboardMetricCardContent(
            title = title,
            value = value,
            unit = unit,
            subtitle = subtitle,
            icon = icon,
            statusColor = statusColor,
            isElevated = isElevated,
            trendDirection = trendDirection,
            trendPercentChange = trendPercentChange,
            isPro = isPro
        )
    }

    if (isElevated) {
        ElevatedCard(
            onClick = onClick ?: {},
            enabled = onClick != null,
            modifier = cardModifier
        ) {
            cardContent()
        }
    } else {
        OutlinedCard(
            onClick = onClick ?: {},
            enabled = onClick != null,
            modifier = cardModifier,
            border = CardDefaults.outlinedCardBorder()
        ) {
            cardContent()
        }
    }
}

@Composable
private fun DashboardMetricCardContent(
    title: String,
    value: String?,
    unit: String,
    subtitle: String?,
    icon: ImageVector?,
    statusColor: Color?,
    isElevated: Boolean,
    trendDirection: TrendDirection?,
    trendPercentChange: Float?,
    isPro: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Header row with icon circle and name
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(Modifier.width(8.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium
            )
        }

        // Value and category section
        if (value != null) {
            Column {
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = statusColor ?: MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                }
                subtitle?.let { category ->
                    Text(
                        text = category,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor ?: MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }
        } else {
            // Empty state
            val ctaText = if (isElevated) {
                stringResource(Res.string.tap_to_log)
            } else {
                stringResource(Res.string.tap_to_calculate)
            }
            Column {
                Text(
                    text = stringResource(Res.string.no_data_yet),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = ctaText,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Trend badge for Pro users
        if (isPro && trendDirection != null && trendPercentChange != null &&
            trendDirection != TrendDirection.STABLE
        ) {
            TrendBadge(direction = trendDirection, percentChange = trendPercentChange)
        }
    }
}
