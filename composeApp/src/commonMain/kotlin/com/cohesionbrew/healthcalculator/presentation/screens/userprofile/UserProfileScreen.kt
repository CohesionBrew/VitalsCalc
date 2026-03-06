package com.cohesionbrew.healthcalculator.presentation.screens.userprofile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthGenderSelectorToggle
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserProfileScreen(uiStateHolder: UserProfileUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    UserProfileScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun UserProfileScreen(
    uiState: UserProfileUiState,
    onUiEvent: (UserProfileUiEvent) -> Unit
) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_profile),
        isScrollableContent = true,
        includeBottomInsets = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                stringResource(Res.string.profile_defaults_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            HealthGenderSelectorToggle(
                maleLabel = stringResource(Res.string.male),
                femaleLabel = stringResource(Res.string.female),
                isMaleSelected = uiState.gender == "male",
                onMaleSelected = { onUiEvent(UserProfileUiEvent.OnGenderChanged(true)) },
                onFemaleSelected = { onUiEvent(UserProfileUiEvent.OnGenderChanged(false)) }
            )

            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.heightCm),
                onValueChange = { onUiEvent(UserProfileUiEvent.OnHeightChanged(it)) },
                label = { Text(stringResource(if (uiState.useMetric) Res.string.profile_height_metric else Res.string.profile_height_imperial)) },
                modifier = Modifier.fillMaxWidth()
            )

            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.weightKg),
                onValueChange = { onUiEvent(UserProfileUiEvent.OnWeightChanged(it)) },
                label = { Text(stringResource(if (uiState.useMetric) Res.string.profile_weight_metric else Res.string.profile_weight_imperial)) },
                modifier = Modifier.fillMaxWidth()
            )

            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.restingHr),
                onValueChange = { onUiEvent(UserProfileUiEvent.OnRestingHrChanged(it)) },
                label = { Text(stringResource(Res.string.profile_resting_hr)) },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(Res.string.use_metric), style = MaterialTheme.typography.bodyLarge)
                @OptIn(ExperimentalAdaptiveApi::class)
                AdaptiveSwitch(
                    checked = uiState.useMetric,
                    onCheckedChange = { onUiEvent(UserProfileUiEvent.OnUseMetricChanged(it)) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            HealthActionButton(
                text = stringResource(if (uiState.isSaved) Res.string.profile_saved else Res.string.profile_save_button),
                isLoading = false,
                onClick = { onUiEvent(UserProfileUiEvent.OnSave) },
                enabled = !uiState.isSaved,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
