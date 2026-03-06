package com.cohesionbrew.healthcalculator.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
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
import com.cohesionbrew.healthcalculator.designsystem.components.premium.UpgradePremiumBanner
import com.cohesionbrew.healthcalculator.presentation.components.health.getBmiCategoryColor
import com.cohesionbrew.healthcalculator.presentation.components.health.getBodyFatCategoryColor
import com.cohesionbrew.healthcalculator.presentation.components.health.getBpCategoryColor
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
        isScrollableContent = false,
        title = stringResource(Res.string.title_screen_home),
        includeBottomInsets = false
    ) {
        if (uiState.historyCount == 0 && !uiState.isLoading) {
            // Empty / welcome state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    stringResource(Res.string.welcome_title),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    stringResource(Res.string.welcome_message),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            // Dashboard grid with section headers
            val hasMeasurements = uiState.latestBmi != null || uiState.latestBodyFat != null || uiState.latestBp != null
            val hasGoals = uiState.latestBmr != null

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Section: Your Measurements
                if (hasMeasurements) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = stringResource(Res.string.your_measurements),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    uiState.latestBmi?.let { entry ->
                        item {
                            val cat = entry.category
                            val bmiCategoryIndex = getBmiCategoryIndexFromName(cat)
                            DashboardMetricCard(
                                title = stringResource(Res.string.bmi),
                                value = fmt(entry.primaryValue),
                                unit = stringResource(Res.string.unit_kg_m2),
                                subtitle = cat,
                                icon = Icons.Filled.Person,
                                isElevated = true,
                                statusColor = if (cat != null) getBmiCategoryColor(bmiCategoryIndex) else null,
                                isPro = uiState.isPro
                            )
                        }
                    }

                    uiState.latestBodyFat?.let { entry ->
                        item {
                            val cat = entry.category
                            DashboardMetricCard(
                                title = stringResource(Res.string.body_fat),
                                value = fmt(entry.primaryValue),
                                unit = "%",
                                subtitle = cat,
                                icon = Icons.Filled.Info,
                                isElevated = true,
                                statusColor = if (cat != null) getBodyFatCategoryColor(cat) else null,
                                isPro = uiState.isPro
                            )
                        }
                    }

                    uiState.latestBp?.let { entry ->
                        item {
                            val cat = entry.category
                            DashboardMetricCard(
                                title = stringResource(Res.string.bp_title),
                                value = entry.primaryValue.toInt().toString(),
                                unit = stringResource(Res.string.unit_mmhg),
                                subtitle = cat,
                                icon = Icons.Filled.Favorite,
                                isElevated = true,
                                statusColor = if (cat != null) getBpCategoryColor(cat) else null,
                                isPro = uiState.isPro
                            )
                        }
                    }
                }

                // Section: Your Goals & Recommendations
                if (hasGoals) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = stringResource(Res.string.your_goals_recommendations),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = if (hasMeasurements) 16.dp else 0.dp)
                        )
                    }

                    uiState.latestBmr?.let { entry ->
                        item {
                            DashboardMetricCard(
                                title = stringResource(Res.string.bmr),
                                value = fmt(entry.primaryValue),
                                unit = stringResource(Res.string.unit_kcal),
                                icon = Icons.Filled.Star,
                                isElevated = false,
                                isPro = uiState.isPro
                            )
                        }
                    }
                }

                // Pro upgrade banner (if not Pro)
                if (!uiState.isPro) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        UpgradePremiumBanner(
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }

                // Footer: calculation count
                if (uiState.historyCount > 0) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Text(
                            text = stringResource(Res.string.calculations_recorded, uiState.historyCount),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

private fun fmt(v: Double): String = ((v * 10).roundToInt() / 10.0).toString()

private fun getBmiCategoryIndexFromName(categoryName: String?): Int {
    return when (categoryName) {
        "Underweight" -> 0
        "Healthy Weight" -> 1
        "Overweight" -> 2
        "Obese Class I" -> 3
        "Obese Class II" -> 4
        "Obese Class III" -> 5
        else -> 0
    }
}
