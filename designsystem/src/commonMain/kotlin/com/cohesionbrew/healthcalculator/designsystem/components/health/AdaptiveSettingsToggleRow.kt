package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cohesionbrew.healthcalculator.designsystem.util.isIos
import io.github.robinpcrd.cupertino.adaptive.AdaptiveSwitch
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi

/**
 * Adaptive settings row that renders full-width tappable rows with platform-appropriate styling.
 *
 * ## iOS Style
 * - Full-width tappable area
 * - Title left-aligned, trailing content right-aligned
 * - Subtitle smaller gray text below title
 * - No ripple effect (iOS uses highlight)
 *
 * ## Android Style
 * - Similar layout with Material styling
 * - Ripple effect on tap
 *
 * @param title Primary text for the row
 * @param subtitle Optional secondary text below the title
 * @param trailingContent Optional composable for right side (switch, chevron, etc.)
 * @param onClick Optional click handler for the entire row
 * @param modifier Modifier for the row
 */
@Composable
fun AdaptiveSettingsRow(
    title: String,
    subtitle: String? = null,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val rowModifier = if (onClick != null) {
        modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = if (isIos) 12.dp else 8.dp)
    } else {
        modifier
            .fillMaxWidth()
            .padding(vertical = if (isIos) 12.dp else 8.dp)
    }

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        trailingContent?.invoke()
    }
}

/**
 * Settings row specifically for toggle switches with proper accessibility.
 *
 * The entire row is tappable to toggle the switch, making it easier to use
 * and more consistent with iOS Settings.app behavior.
 *
 * @param title Primary text for the row
 * @param subtitle Optional secondary text below the title
 * @param checked Current toggle state
 * @param onCheckedChange Callback when toggle changes
 * @param enabled Whether the toggle is enabled
 * @param modifier Modifier for the row
 */
@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun AdaptiveSettingsToggleRow(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(
                value = checked,
                enabled = enabled,
                role = Role.Switch,
                onValueChange = onCheckedChange
            )
            .padding(vertical = if (isIos) 12.dp else 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                }
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                    }
                )
            }
        }

        AdaptiveSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}
