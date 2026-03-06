package com.cohesionbrew.healthcalculator.presentation.screens.idealweight

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthGenderSelectorToggle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.generated.resources.*
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun IdealWeightScreen(uiStateHolder: IdealWeightUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    IdealWeightScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun IdealWeightScreen(uiState: IdealWeightUiState, onUiEvent: (IdealWeightUiEvent) -> Unit) {
    ScreenWithToolbar(title = stringResource(Res.string.title_screen_ideal_weight), isScrollableContent = true, includeBottomInsets = false) {
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HealthScreenTitle(text = stringResource(Res.string.title_screen_ideal_weight))
            HealthGenderSelectorToggle(
                maleLabel = stringResource(Res.string.male),
                femaleLabel = stringResource(Res.string.female),
                isMaleSelected = uiState.gender == "male",
                onMaleSelected = { onUiEvent(IdealWeightUiEvent.OnGenderChanged("male")) },
                onFemaleSelected = { onUiEvent(IdealWeightUiEvent.OnGenderChanged("female")) }
            )
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.heightCm),
                onValueChange = { onUiEvent(IdealWeightUiEvent.OnHeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.height)) },
                suffix = { Text(stringResource(Res.string.unit_cm)) },
                modifier = Modifier.fillMaxWidth()
            )
            HealthActionButton(text = stringResource(Res.string.calculate), isLoading = uiState.isLoading, onClick = { onUiEvent(IdealWeightUiEvent.OnCalculate) })
            uiState.results?.let { r ->
                Spacer(modifier = Modifier.height(8.dp))
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)) {
                    Column(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(Res.string.idealweight_result_title), style = MaterialTheme.typography.titleMedium)
                        Text("${fmt(r.averageKg)} kg (avg)", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        HorizontalDivider()
                        FormulaRow(stringResource(Res.string.idealweight_robinson), r.robinsonKg)
                        FormulaRow(stringResource(Res.string.idealweight_miller), r.millerKg)
                        FormulaRow(stringResource(Res.string.idealweight_devine), r.devineKg)
                        FormulaRow(stringResource(Res.string.idealweight_hamwi), r.hamwiKg)
                    }
                }
            }
        }
    }
}

@Composable
private fun FormulaRow(name: String, kg: Double) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(name, style = MaterialTheme.typography.bodyMedium)
        Text("${fmt(kg)} kg", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

private fun fmt(v: Double): String = ((v * 10).roundToInt() / 10.0).toString()
