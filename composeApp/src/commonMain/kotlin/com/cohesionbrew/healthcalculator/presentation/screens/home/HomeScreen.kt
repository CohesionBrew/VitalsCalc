package com.cohesionbrew.healthcalculator.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.generated.resources.*
import com.cohesionbrew.healthcalculator.presentation.components.health.DashboardMetricCard
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: HomeUiStateHolder
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    HomeScreen(modifier = modifier.fillMaxSize(), uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit
) {
    ScreenWithToolbar(
        modifier = modifier,
        isScrollableContent = true,
        title = stringResource(Res.string.title_screen_home),
        includeBottomInsets = false
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                stringResource(Res.string.health_dashboard),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            if (uiState.historyCount == 0 && !uiState.isLoading) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(stringResource(Res.string.welcome_title), style = MaterialTheme.typography.titleLarge)
                    Text(
                        stringResource(Res.string.welcome_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    uiState.latestBmi?.let {
                        DashboardMetricCard(
                            title = stringResource(Res.string.bmi),
                            value = fmt(it.primaryValue),
                            unit = stringResource(Res.string.unit_kg_m2),
                            subtitle = it.category,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    uiState.latestBmr?.let {
                        DashboardMetricCard(
                            title = stringResource(Res.string.bmr),
                            value = fmt(it.primaryValue),
                            unit = stringResource(Res.string.unit_kcal),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    uiState.latestBodyFat?.let {
                        DashboardMetricCard(
                            title = stringResource(Res.string.body_fat),
                            value = fmt(it.primaryValue),
                            unit = "%",
                            subtitle = it.category,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    uiState.latestBp?.let {
                        DashboardMetricCard(
                            title = stringResource(Res.string.bp_title),
                            value = it.primaryValue.toInt().toString(),
                            unit = stringResource(Res.string.unit_mmhg),
                            subtitle = it.category,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                if (uiState.historyCount > 0) {
                    Text(
                        "${uiState.historyCount} calculations recorded",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun fmt(v: Double): String = ((v * 10).roundToInt() / 10.0).toString()
