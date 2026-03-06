package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

enum class TooltipMode {
    Inline,
    Dialog
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthInfoTooltip(
    mode: TooltipMode,
    tooltipText: String,
    modifier: Modifier = Modifier,
    tooltipTitle: String = "Info",
    dismissLabel: String = "Got it",
    infoContentDescription: String = "Info",
) {
    var showTooltip by remember { mutableStateOf(false) }

    Icon(
        imageVector = Icons.Default.Info,
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        contentDescription = infoContentDescription,
        modifier = modifier
            .size(18.dp)
            .clickable { showTooltip = true }
    )

    when (mode) {
        TooltipMode.Inline -> {
            if (showTooltip) {
                LaunchedEffect(Unit) {
                    delay(3000)
                    showTooltip = false
                }

                AnimatedVisibility(
                    visible = showTooltip,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    ) {
                        Text(tooltipText, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }

        TooltipMode.Dialog -> {
            if (showTooltip) {
                BasicAlertDialog(
                    onDismissRequest = { showTooltip = false }
                ) {
                    BoxWithConstraints {
                        val density = LocalDensity.current
                        val safe = WindowInsets.safeDrawing
                        val verticalInsets = with(density) {
                            (safe.getTop(this) + safe.getBottom(this)).toDp()
                        }
                        val isLandscape = maxWidth < maxHeight
                        val heightFraction = if (isLandscape) 0.8f else 0.9f
                        val maxDialogHeight = (maxHeight - verticalInsets) * heightFraction

                        Surface(
                            modifier = Modifier
                                .padding(24.dp)
                                .sizeIn(
                                    maxWidth = 560.dp,
                                    maxHeight = maxDialogHeight
                                ),
                            shape = RoundedCornerShape(16.dp),
                            tonalElevation = 8.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(Modifier.padding(20.dp)) {
                                Text(
                                    text = tooltipTitle,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(Modifier.size(12.dp))
                                Column(
                                    modifier = Modifier
                                        .weight(1f, fill = false)
                                        .verticalScroll(rememberScrollState())
                                ) {
                                    Text(
                                        text = tooltipText,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                                Spacer(Modifier.size(16.dp))
                                TextButton(
                                    onClick = { showTooltip = false },
                                    modifier = Modifier.align(Alignment.End)
                                ) {
                                    Text(dismissLabel)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
