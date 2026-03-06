package com.cohesionbrew.healthcalculator.presentation.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import io.github.robinpcrd.cupertino.adaptive.AdaptiveSwitch
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthAlertDialog
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(uiStateHolder: SettingsUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    SettingsScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onUiEvent: (SettingsUiEvent) -> Unit
) {
    if (uiState.showClearConfirmation) {
        HealthAlertDialog(
            title = stringResource(Res.string.settings_clear_data_title),
            message = stringResource(Res.string.settings_clear_data_message),
            confirmText = stringResource(Res.string.settings_clear_button),
            dismissText = stringResource(Res.string.cancel),
            onConfirm = { onUiEvent(SettingsUiEvent.OnConfirmClear) },
            onDismiss = { onUiEvent(SettingsUiEvent.OnDismissClear) }
        )
    }

    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_settings),
        isScrollableContent = true,
        includeBottomInsets = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                stringResource(Res.string.measurement_units),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(stringResource(Res.string.use_metric), style = MaterialTheme.typography.bodyLarge)
                    Text(
                        stringResource(if (uiState.useMetric) Res.string.metric_kg_cm else Res.string.imperial_lb_in),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                @OptIn(ExperimentalAdaptiveApi::class)
                AdaptiveSwitch(
                    checked = uiState.useMetric,
                    onCheckedChange = { onUiEvent(SettingsUiEvent.OnUnitSystemChanged(it)) }
                )
            }

            Text(
                stringResource(Res.string.settings_section_data),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 16.dp, bottom = 4.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onUiEvent(SettingsUiEvent.OnClearData) }
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
                Text(
                    stringResource(Res.string.settings_clear_all_history),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
