package com.cohesionbrew.healthcalculator.presentation.screens.bodyfat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthDropdownSelector
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthGenderSelectorToggle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.domain.model.BodyFatMethod
import com.cohesionbrew.healthcalculator.generated.resources.*
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun BodyFatScreen(uiStateHolder: BodyFatUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BodyFatScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BodyFatScreen(uiState: BodyFatUiState, onUiEvent: (BodyFatUiEvent) -> Unit) {
    ScreenWithToolbar(title = stringResource(Res.string.title_screen_body_fat), isScrollableContent = true, includeBottomInsets = false) {
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HealthScreenTitle(text = stringResource(Res.string.bodyfat_title))
            val navyMethodLabel = stringResource(Res.string.bodyfat_method_navy)
            val rfmMethodLabel = stringResource(Res.string.bodyfat_method_rfm)
            HealthDropdownSelector(
                caption = stringResource(Res.string.label_method),
                options = listOf(navyMethodLabel, rfmMethodLabel),
                selected = if (uiState.method == BodyFatMethod.NAVY) navyMethodLabel else rfmMethodLabel,
                onSelect = { onUiEvent(BodyFatUiEvent.OnMethodChanged(if (it == navyMethodLabel) BodyFatMethod.NAVY else BodyFatMethod.RFM)) }
            )
            HealthGenderSelectorToggle(
                maleLabel = stringResource(Res.string.male),
                femaleLabel = stringResource(Res.string.female),
                isMaleSelected = uiState.gender == "male",
                onMaleSelected = { onUiEvent(BodyFatUiEvent.OnGenderChanged("male")) },
                onFemaleSelected = { onUiEvent(BodyFatUiEvent.OnGenderChanged("female")) }
            )
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.heightCm),
                onValueChange = { onUiEvent(BodyFatUiEvent.OnHeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.height)) }, suffix = { Text(stringResource(Res.string.unit_cm)) },
                modifier = Modifier.fillMaxWidth()
            )
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.waistCm),
                onValueChange = { onUiEvent(BodyFatUiEvent.OnWaistChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.bodyfat_waist)) }, suffix = { Text(stringResource(Res.string.unit_cm)) },
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.method == BodyFatMethod.NAVY) {
                FormattedDoubleTextField(
                    value = formatDoubleDisplay(uiState.neckCm),
                    onValueChange = { onUiEvent(BodyFatUiEvent.OnNeckChanged(it ?: 0.0)) },
                    label = { Text(stringResource(Res.string.bodyfat_neck)) }, suffix = { Text(stringResource(Res.string.unit_cm)) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.gender == "female") {
                    FormattedDoubleTextField(
                        value = formatDoubleDisplay(uiState.hipCm),
                        onValueChange = { onUiEvent(BodyFatUiEvent.OnHipChanged(it ?: 0.0)) },
                        label = { Text(stringResource(Res.string.bodyfat_hip)) }, suffix = { Text(stringResource(Res.string.unit_cm)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            HealthActionButton(text = stringResource(Res.string.calculate), isLoading = uiState.isLoading, onClick = { onUiEvent(BodyFatUiEvent.OnCalculate) })
            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(Res.string.bodyfat_result_title), style = MaterialTheme.typography.titleMedium)
                        Text("${((uiState.resultPercent * 10).roundToInt() / 10.0)}%", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        uiState.category?.let { Text("Category: ${it.name}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    }
                }
            }
        }
    }
}
