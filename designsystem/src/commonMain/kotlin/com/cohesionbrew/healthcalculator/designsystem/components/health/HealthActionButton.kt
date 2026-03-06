package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cohesionbrew.healthcalculator.designsystem.theme.AppTheme
import io.github.robinpcrd.cupertino.adaptive.AdaptiveCircularProgressIndicator
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun HealthActionButton(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val accessibilityState = when {
        isLoading -> "Loading"
        !enabled -> "Disabled"
        else -> null
    }

    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = AppTheme.colors.outline,
            disabledContentColor = AppTheme.colors.text.primary
        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier
            .height(55.dp)
            .width(IntrinsicSize.Max)
            .semantics(mergeDescendants = true) {
                contentDescription = text
                accessibilityState?.let { stateDescription = it }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            AdaptiveCircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(if (isLoading) 1f else 0f),
                adaptationScope = {
                    material {
                        color = MaterialTheme.colorScheme.onPrimary
                        strokeWidth = 1.5.dp
                    }
                }
            )
            Text(
                text = text,
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
