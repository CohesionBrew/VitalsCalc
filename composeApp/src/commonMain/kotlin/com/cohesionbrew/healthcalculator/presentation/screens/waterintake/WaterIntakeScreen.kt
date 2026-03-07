package com.cohesionbrew.healthcalculator.presentation.screens.waterintake

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.generated.resources.*
import io.github.robinpcrd.cupertino.adaptive.AdaptiveSlider
import io.github.robinpcrd.cupertino.adaptive.AdaptiveSwitch
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun WaterIntakeScreen(uiStateHolder: WaterIntakeUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    WaterIntakeScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun WaterIntakeScreen(uiState: WaterIntakeUiState, onUiEvent: (WaterIntakeUiEvent) -> Unit) {
    val title = stringResource(Res.string.water_title)

    // Build subtitle showing profile weight
    val weightText = if (uiState.weightKg > 0) {
        if (uiState.useMetric) {
            stringResource(Res.string.water_weight_label, formatNumber(uiState.weightKg) + " kg")
        } else {
            val weightLbs = uiState.weightKg * 2.20462
            stringResource(Res.string.water_weight_label, "${weightLbs.roundToInt()} lbs")
        }
    } else {
        null
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            LandscapeLayout(
                title = title,
                subtitle = weightText,
                uiState = uiState,
                onUiEvent = onUiEvent
            )
        } else {
            PortraitLayout(
                title = title,
                subtitle = weightText,
                uiState = uiState,
                onUiEvent = onUiEvent
            )
        }
    }
}

// --- Portrait Layout (matches old StandardHealthScaffold PortraitLayout) ---

@Composable
private fun PortraitLayout(
    title: String,
    subtitle: String?,
    uiState: WaterIntakeUiState,
    onUiEvent: (WaterIntakeUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HealthScreenTitle(text = title)

            subtitle?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            // Input card - calculation happens automatically on changes
            WaterIntakeInputCard(
                uiState = uiState,
                onUiEvent = onUiEvent,
                modifier = Modifier.fillMaxWidth()
            )

            // Results card - shows automatically when weight > 0
            if (uiState.isCalculated) {
                WaterResultCard(
                    liters = uiState.resultLiters,
                    ounces = uiState.resultOunces,
                    useMetric = uiState.useMetric,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        // Ad banner slot would go here if needed
    }
}

// --- Landscape Layout (matches old StandardHealthScaffold LandscapeLayout) ---

@Composable
private fun LandscapeLayout(
    title: String,
    subtitle: String?,
    uiState: WaterIntakeUiState,
    onUiEvent: (WaterIntakeUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HealthScreenTitle(text = title)

                subtitle?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Input card
                WaterIntakeInputCard(
                    uiState = uiState,
                    onUiEvent = onUiEvent
                )
            }

            // Right Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.isCalculated) {
                    WaterResultCard(
                        liters = uiState.resultLiters,
                        ounces = uiState.resultOunces,
                        useMetric = uiState.useMetric
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.water_set_weight_message),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        // Ad banner slot would go here if needed
    }
}

// --- WaterIntakeInputCard (activity slider + climate switch in a card) ---

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
private fun WaterIntakeInputCard(
    uiState: WaterIntakeUiState,
    onUiEvent: (WaterIntakeUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Activity level slider
            Text(
                text = "${stringResource(Res.string.activity_level)}: ${uiState.activityLevelDescription}",
                style = MaterialTheme.typography.titleMedium
            )

            AdaptiveSlider(
                value = uiState.activityLevel.toFloat(),
                onValueChange = { onUiEvent(WaterIntakeUiEvent.OnActivityLevelChanged(it.roundToInt())) },
                valueRange = 1f..5f,
                steps = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(Res.string.sedentary),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    stringResource(Res.string.extra_active),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Hot climate toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = stringResource(Res.string.hot_climate),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(Res.string.hot_climate_description),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                AdaptiveSwitch(
                    checked = uiState.isHotClimate,
                    onCheckedChange = { onUiEvent(WaterIntakeUiEvent.OnClimateChanged(it)) }
                )
            }
        }
    }
}

// --- WaterResultCard (water drop icon, primary/secondary values, glasses count) ---

@Composable
private fun WaterResultCard(
    liters: Double,
    ounces: Double,
    useMetric: Boolean,
    modifier: Modifier = Modifier
) {
    // Primary display (based on unit preference)
    val primaryValue = if (useMetric) liters else ounces
    val primaryUnit = if (useMetric) "L" else "oz"

    // Secondary display (alternate unit)
    val secondaryValue = if (useMetric) ounces else liters
    val secondaryUnit = if (useMetric) "oz" else "L"

    // Glasses of water (assuming 250ml/8oz per glass)
    val glasses = (liters * 1000 / 250).roundToInt()

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Water drop icon (Material Design WaterDrop, inlined to avoid extended icons dependency)
            val waterDropIcon = rememberWaterDropIcon()
            Icon(
                imageVector = waterDropIcon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF2196F3) // Water blue
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title - small, subdued label
            Text(
                text = stringResource(Res.string.daily_water_goal),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Primary value - large, prominent (no decimal)
            Text(
                text = "${primaryValue.roundToInt()} $primaryUnit",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Secondary value - smaller, supporting
            Text(
                text = "(${formatNumber(secondaryValue)} $secondaryUnit)",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Glasses info - small, supplementary
            Text(
                text = stringResource(Res.string.water_result_glasses, glasses),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

private fun formatNumber(value: Double): String {
    val rounded = (value * 10).roundToInt() / 10.0
    return if (rounded == rounded.toLong().toDouble()) {
        "${rounded.toLong()}.0"
    } else {
        rounded.toString()
    }
}

/**
 * Material Design WaterDrop icon (Filled variant).
 * Inlined to avoid pulling in the large material-icons-extended dependency.
 * SVG path from the official Material Symbols set.
 */
@Composable
private fun rememberWaterDropIcon(): ImageVector = remember {
    ImageVector.Builder(
        name = "WaterDrop",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(fill = SolidColor(Color.Black)) {
            // Material Design water_drop filled path
            moveTo(12f, 2.0f)
            curveToRelative(-0.41f, 0.0f, -0.77f, 0.25f, -0.92f, 0.62f)
            curveTo(9.36f, 6.53f, 5.0f, 10.46f, 5.0f, 14.5f)
            curveTo(5.0f, 18.64f, 8.13f, 22.0f, 12.0f, 22.0f)
            curveToRelative(3.87f, 0.0f, 7.0f, -3.36f, 7.0f, -7.5f)
            curveTo(19.0f, 10.46f, 14.64f, 6.53f, 12.92f, 2.62f)
            curveTo(12.77f, 2.25f, 12.41f, 2.0f, 12.0f, 2.0f)
            close()
        }
    }.build()
}
