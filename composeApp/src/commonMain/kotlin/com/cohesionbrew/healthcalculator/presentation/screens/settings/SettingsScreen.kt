package com.cohesionbrew.healthcalculator.presentation.screens.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.health.AdaptiveSettingsToggleRow
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthLanguageDropdown
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthMenuPagesHeaderText
import com.cohesionbrew.healthcalculator.designsystem.components.health.LanguageMenuItem
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
    val languageMenuOptions = remember {
        listOf(
            LanguageMenuItem("English", "en", Res.drawable.us),
            LanguageMenuItem("Espanol / Spanish", "es", Res.drawable.es),
            LanguageMenuItem("Francais / French", "fr", Res.drawable.fr),
            LanguageMenuItem("Deutsch / German", "de", Res.drawable.flag_germany),
            LanguageMenuItem("Portugues / Portuguese", "pt-BR", Res.drawable.flag_brazil),
            LanguageMenuItem("Japanese", "ja", Res.drawable.japan_flag),
            LanguageMenuItem("Korean", "ko", Res.drawable.flag_south_korea),
            LanguageMenuItem("Chinese (Simplified)", "zh-CN", Res.drawable.flag_china),
            LanguageMenuItem("Chinese (Traditional - Taiwan)", "zh-TW", Res.drawable.flag_taiwan),
            LanguageMenuItem("Chinese (Traditional - Hong Kong)", "zh-HK", Res.drawable.flag_hong_kong),
        )
    }

    val selectedLanguage = remember(uiState.language) {
        languageMenuOptions.find { it.code == uiState.language }
    }

    val metricSubtitle = stringResource(
        Res.string.settings_you_have_chosen,
        if (uiState.useMetric) {
            stringResource(Res.string.settings_metric_cm_kg)
        } else {
            stringResource(Res.string.settings_imperial_inch_pound)
        }
    )

    val advancedModeSubtitle = if (uiState.advancedMode) {
        stringResource(Res.string.settings_advanced_mode_enabled)
    } else {
        stringResource(Res.string.settings_advanced_mode_disabled)
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(6.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HealthMenuPagesHeaderText(
                    text = stringResource(Res.string.title_screen_settings)
                )

                HorizontalDivider(thickness = 1.dp)

                AdaptiveSettingsToggleRow(
                    title = stringResource(Res.string.settings_use_metric_units),
                    subtitle = metricSubtitle,
                    checked = uiState.useMetric,
                    onCheckedChange = { onUiEvent(SettingsUiEvent.OnUseMetricChanged(it)) }
                )

                HorizontalDivider(thickness = 1.dp)

                AdaptiveSettingsToggleRow(
                    title = stringResource(Res.string.settings_advanced_mode),
                    subtitle = advancedModeSubtitle,
                    checked = uiState.advancedMode,
                    onCheckedChange = { onUiEvent(SettingsUiEvent.OnAdvancedModeChanged(it)) }
                )

                HorizontalDivider(thickness = 1.dp)

                HealthLanguageDropdown(
                    label = stringResource(Res.string.settings_language),
                    options = languageMenuOptions,
                    selectedOption = selectedLanguage,
                    onOptionSelected = { onUiEvent(SettingsUiEvent.OnLanguageChanged(it.code)) },
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(thickness = 1.dp)

                HealthActionButton(
                    text = stringResource(Res.string.settings_save_and_close),
                    isLoading = uiState.isSaving,
                    onClick = { onUiEvent(SettingsUiEvent.OnSave) }
                )
            }
        }
    }
}
