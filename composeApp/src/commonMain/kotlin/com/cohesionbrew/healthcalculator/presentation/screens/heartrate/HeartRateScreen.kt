package com.cohesionbrew.healthcalculator.presentation.screens.heartrate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthInfoTooltip
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthUnitSystemSwitch
import com.cohesionbrew.healthcalculator.designsystem.components.health.TooltipMode
import com.cohesionbrew.healthcalculator.domain.calculator.HeartRateZoneCalculator
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun HeartRateScreen(uiStateHolder: HeartRateUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    HeartRateScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun HeartRateScreen(uiState: HeartRateUiState, onUiEvent: (HeartRateUiEvent) -> Unit) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_heart_rate),
        isScrollableContent = true,
        includeBottomInsets = false
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HealthScreenTitle(text = stringResource(Res.string.heart_rate))

            OutlinedTextField(
                value = if (uiState.age > 0) uiState.age.toString() else "",
                onValueChange = { onUiEvent(HeartRateUiEvent.OnAgeChanged(it.toIntOrNull() ?: 0)) },
                label = { Text(stringResource(Res.string.label_age)) },
                suffix = { Text(stringResource(Res.string.unit_years)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // "I know my resting heart rate" checkbox with info tooltip
            HeartRateZoneDetailsCard(
                knowsRestingHr = uiState.knowsRestingHr,
                restingHr = uiState.restingHr,
                restingHrError = uiState.restingHrError,
                onKnowsRestingHrToggled = { onUiEvent(HeartRateUiEvent.OnKnowsRestingHrToggled) },
                onRestingHrChanged = { onUiEvent(HeartRateUiEvent.OnRestingHrChanged(it)) }
            )

            HealthUnitSystemSwitch(
                label = stringResource(Res.string.tanaka_formula),
                description = if (uiState.useTanaka) stringResource(Res.string.tanaka_formula_desc) else stringResource(Res.string.standard_formula_desc),
                isMetric = uiState.useTanaka,
                onUnitSystemChange = { onUiEvent(HeartRateUiEvent.OnMethodChanged(it)) }
            )

            HealthActionButton(
                text = stringResource(Res.string.calculate_zones),
                isLoading = uiState.isLoading,
                onClick = { onUiEvent(HeartRateUiEvent.OnCalculate) }
            )

            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(Res.string.max_heart_rate, uiState.maxHr),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                HeartRateZonesChart(zones = uiState.zones)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// -- Zone Details Card (checkbox + resting HR input) --

@Composable
private fun HeartRateZoneDetailsCard(
    knowsRestingHr: Boolean,
    restingHr: Int,
    restingHrError: String?,
    onKnowsRestingHrToggled: () -> Unit,
    onRestingHrChanged: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Checkbox(
                checked = knowsRestingHr,
                onCheckedChange = { onKnowsRestingHrToggled() }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(Res.string.i_know_my_resting_heart_rate),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f, fill = false)
            )
            Spacer(modifier = Modifier.width(4.dp))
            HealthInfoTooltip(
                mode = TooltipMode.Dialog,
                tooltipText = stringResource(Res.string.tooltip_knowing_resting_hr),
                tooltipTitle = stringResource(Res.string.heart_rate_info)
            )
        }

        if (knowsRestingHr) {
            OutlinedTextField(
                value = if (restingHr > 0) restingHr.toString() else "",
                onValueChange = { onRestingHrChanged(it.toIntOrNull() ?: 0) },
                label = { Text(stringResource(Res.string.resting_heart_rate)) },
                suffix = { Text(stringResource(Res.string.unit_bpm)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = restingHrError != null,
                supportingText = restingHrError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            )
        }
    }
}

// -- Heart Rate Zones Chart --

private data class ZoneStaticData(
    val zoneNumber: String,
    val zoneName: String,
    val zonePercentage: String,
    val zoneColor: Color,
    val description: String
)

/** Zone colors: Z5=Red, Z4=Orange, Z3=Green, Z2=Blue, Z1=Gray (displayed top-to-bottom Z5..Z1) */
private val zoneColors = listOf(
    Color(0xFFD32F2F), // Zone 5 - Red
    Color(0xFFFFA000), // Zone 4 - Orange
    Color(0xFF388E3C), // Zone 3 - Green
    Color(0xFF1976D2), // Zone 2 - Blue
    Color(0xFF616161)  // Zone 1 - Gray
)

@Composable
fun HeartRateZonesChart(zones: List<HeartRateZoneCalculator.Zone>) {
    if (zones.isEmpty()) return

    // Build static data for each zone (displayed in reverse order: Z5 at top, Z1 at bottom)
    val zoneStaticData = buildZoneStaticData()

    // Build calculated HR strings from zones (reversed so Z5 is first)
    val calculatedHrValues = zones.reversed().map { "${it.minBpm} - ${it.maxBpm}" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color.DarkGray.copy(alpha = 0.7f)))
    ) {
        // Header row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.03f))
                .padding(vertical = 8.dp, horizontal = 4.dp)
                .border(BorderStroke(0.5.dp, Color.DarkGray.copy(alpha = 0.5f)))
        ) {
            Text(
                stringResource(Res.string.target_zone),
                modifier = Modifier.weight(0.35f).padding(start = 8.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                stringResource(Res.string.calculated_heart_rate),
                modifier = Modifier.weight(0.20f),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                stringResource(Res.string.description),
                modifier = Modifier.weight(0.45f).padding(start = 10.dp),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        }

        // Zone rows
        Column(modifier = Modifier.fillMaxWidth()) {
            zoneStaticData.zip(calculatedHrValues).forEach { (staticData, hrValue) ->
                ZoneDetailRow(staticData = staticData, calculatedHeartRate = hrValue)
            }
        }
    }
}

@Composable
private fun ZoneDetailRow(staticData: ZoneStaticData, calculatedHeartRate: String) {
    val accessibilityDesc = "Zone ${staticData.zoneNumber}, ${staticData.zoneName}, " +
            "${staticData.zonePercentage} intensity, $calculatedHeartRate beats per minute, " +
            staticData.description

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .defaultMinSize(minHeight = 80.dp)
            .border(BorderStroke(0.5.dp, Color.LightGray.copy(alpha = 0.8f)))
            .semantics(mergeDescendants = true) {
                contentDescription = accessibilityDesc
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Cell 1: Target Zone (zone number + name + percentage)
        Box(
            modifier = Modifier
                .weight(0.35f)
                .background(staticData.zoneColor)
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = staticData.zoneNumber,
                    style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.padding(end = 12.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = staticData.zoneName,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        text = staticData.zonePercentage,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // Cell 2: Calculated Heart Rate
        Box(
            modifier = Modifier
                .weight(0.20f)
                .background(staticData.zoneColor)
                .border(BorderStroke(width = 1.dp, color = Color.White.copy(alpha = 0.5f)))
                .padding(8.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = calculatedHeartRate,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        // Cell 3: Description
        Box(
            modifier = Modifier
                .weight(0.45f)
                .background(Color.White)
                .border(BorderStroke(width = 1.dp, color = Color.LightGray.copy(alpha = 0.8f)))
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .fillMaxHeight(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = staticData.description,
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun buildZoneStaticData(): List<ZoneStaticData> {
    return listOf(
        ZoneStaticData(
            zoneNumber = "5",
            zoneName = stringResource(Res.string.hr_zone_5_name),
            zonePercentage = "90 - 100%",
            zoneColor = zoneColors[0],
            description = stringResource(Res.string.hr_zone_5_description)
        ),
        ZoneStaticData(
            zoneNumber = "4",
            zoneName = stringResource(Res.string.hr_zone_4_name),
            zonePercentage = "80 - 90%",
            zoneColor = zoneColors[1],
            description = stringResource(Res.string.hr_zone_4_description)
        ),
        ZoneStaticData(
            zoneNumber = "3",
            zoneName = stringResource(Res.string.hr_zone_3_name),
            zonePercentage = "70 - 80%",
            zoneColor = zoneColors[2],
            description = stringResource(Res.string.hr_zone_3_description)
        ),
        ZoneStaticData(
            zoneNumber = "2",
            zoneName = stringResource(Res.string.hr_zone_2_name),
            zonePercentage = "60 - 70%",
            zoneColor = zoneColors[3],
            description = stringResource(Res.string.hr_zone_2_description)
        ),
        ZoneStaticData(
            zoneNumber = "1",
            zoneName = stringResource(Res.string.hr_zone_1_name),
            zonePercentage = "50 - 60%",
            zoneColor = zoneColors[4],
            description = stringResource(Res.string.hr_zone_1_description)
        )
    )
}
