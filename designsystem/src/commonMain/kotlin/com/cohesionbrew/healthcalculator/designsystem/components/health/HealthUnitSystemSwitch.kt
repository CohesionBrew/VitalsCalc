package com.cohesionbrew.healthcalculator.designsystem.components.health

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
import io.github.robinpcrd.cupertino.adaptive.AdaptiveSwitch
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun HealthUnitSystemSwitch(
    modifier: Modifier = Modifier,
    isMetric: Boolean,
    label: String,
    description: String,
    onUnitSystemChange: (isMetric: Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .toggleable(
                value = isMetric,
                role = Role.Switch,
                onValueChange = { newState -> onUnitSystemChange(newState) }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        AdaptiveSwitch(
            checked = isMetric,
            onCheckedChange = onUnitSystemChange
        )
    }
}
