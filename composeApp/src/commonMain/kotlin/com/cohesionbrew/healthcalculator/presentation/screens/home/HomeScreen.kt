package com.cohesionbrew.healthcalculator.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.premium.UpgradePremiumBanner
import com.cohesionbrew.healthcalculator.domain.model.history.BloodPressureHistoryEntry
import com.cohesionbrew.healthcalculator.domain.model.history.BmrHistoryEntry
import com.cohesionbrew.healthcalculator.generated.resources.*
import com.cohesionbrew.healthcalculator.presentation.components.health.DashboardMetricCard
import com.cohesionbrew.healthcalculator.presentation.components.health.getBmiCategoryColor
import com.cohesionbrew.healthcalculator.presentation.components.health.getBodyFatCategoryColor
import com.cohesionbrew.healthcalculator.presentation.components.health.getBpCategoryColor
import com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure.BloodPressureScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bmi.BmiScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bmr.BmrScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bodyfat.BodyFatScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.heartrate.HeartRateScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.idealweight.IdealWeightScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.waterintake.WaterIntakeScreenRoute
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: HomeUiStateHolder,
    onNavigateToCalculator: (Any) -> Unit = {}
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = { event ->
            when (event) {
                is HomeUiEvent.OnNavigateToCalculator -> onNavigateToCalculator(event.route)
                else -> uiStateHolder.onUiEvent(event)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeUiState,
    onUiEvent: (HomeUiEvent) -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = { onUiEvent(HomeUiEvent.OnRefresh) },
        state = pullRefreshState,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            // Section 1: My Measurements (logged health data)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(Res.string.my_measurements),
                    style = MaterialTheme.typography.titleLarge
                )
            }

            // Weight
            item {
                val entry = uiState.latestWeight
                val weightUnit = if (uiState.useMetric) stringResource(Res.string.unit_kg) else stringResource(Res.string.unit_lbs)
                DashboardMetricCard(
                    title = stringResource(Res.string.weight),
                    value = entry?.let { fmt(it.primaryValue) },
                    unit = weightUnit,
                    icon = Icons.Filled.Home,
                    isElevated = true,
                    isPro = uiState.isPro,
                    onClick = { onUiEvent(HomeUiEvent.OnNavigateToCalculator(BmiScreenRoute())) }
                )
            }

            // BMI
            item {
                val entry = uiState.latestBmi
                val cat = entry?.category
                val bmiCategoryIndex = getBmiCategoryIndexFromName(cat)
                DashboardMetricCard(
                    title = stringResource(Res.string.bmi),
                    value = entry?.let { fmt(it.primaryValue) },
                    unit = stringResource(Res.string.unit_kg_m2),
                    subtitle = cat,
                    icon = Icons.Filled.Home,
                    isElevated = true,
                    statusColor = if (cat != null) getBmiCategoryColor(bmiCategoryIndex) else null,
                    isPro = uiState.isPro,
                    onClick = { onUiEvent(HomeUiEvent.OnNavigateToCalculator(BmiScreenRoute())) }
                )
            }

            // Body Fat
            item {
                val entry = uiState.latestBodyFat
                val cat = entry?.category
                DashboardMetricCard(
                    title = stringResource(Res.string.body_fat),
                    value = entry?.let { fmt(it.primaryValue) },
                    unit = "%",
                    subtitle = cat,
                    icon = Icons.Filled.Person,
                    isElevated = true,
                    statusColor = if (cat != null) getBodyFatCategoryColor(cat) else null,
                    isPro = uiState.isPro,
                    onClick = { onUiEvent(HomeUiEvent.OnNavigateToCalculator(BodyFatScreenRoute())) }
                )
            }

            // Blood Pressure
            item {
                val entry = uiState.latestBp
                val cat = entry?.category
                val bpValue = (entry as? BloodPressureHistoryEntry)?.let {
                    "${it.systolic}/${it.diastolic}"
                }
                DashboardMetricCard(
                    title = stringResource(Res.string.bp_title),
                    value = bpValue,
                    unit = stringResource(Res.string.unit_mmhg),
                    subtitle = cat,
                    icon = Icons.Filled.Favorite,
                    isElevated = true,
                    statusColor = if (cat != null) getBpCategoryColor(cat) else null,
                    isPro = uiState.isPro,
                    onClick = { onUiEvent(HomeUiEvent.OnNavigateToCalculator(BloodPressureScreenRoute())) }
                )
            }

            // Section 2: My Goals & Recommendations (calculated targets)
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(
                    text = stringResource(Res.string.my_goals_recommendations),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            // Ideal Weight
            item {
                val entry = uiState.latestIdealWeight
                val idealWeightUnit = if (uiState.useMetric) stringResource(Res.string.unit_kg) else stringResource(Res.string.unit_lbs)
                DashboardMetricCard(
                    title = stringResource(Res.string.ideal_weight),
                    value = entry?.let { fmt(it.primaryValue) },
                    unit = idealWeightUnit,
                    icon = Icons.Filled.Person,
                    isElevated = false,
                    isPro = uiState.isPro,
                    onClick = { onUiEvent(HomeUiEvent.OnNavigateToCalculator(IdealWeightScreenRoute())) }
                )
            }

            // BMR/TDEE
            item {
                val entry = uiState.latestBmr
                val bmrEntry = entry as? BmrHistoryEntry
                val subtitle = bmrEntry?.tdee?.let { "${it.roundToInt()} ${stringResource(Res.string.unit_kcal)}" }
                DashboardMetricCard(
                    title = stringResource(Res.string.bmr_tdee),
                    value = entry?.let { "${it.primaryValue.roundToInt()}" },
                    unit = stringResource(Res.string.unit_kcal),
                    subtitle = subtitle,
                    icon = Icons.Filled.Star,
                    isElevated = false,
                    isPro = uiState.isPro,
                    onClick = { onUiEvent(HomeUiEvent.OnNavigateToCalculator(BmrScreenRoute())) }
                )
            }

            // Training Zone
            item {
                DashboardMetricCard(
                    title = stringResource(Res.string.training_zone),
                    value = null,
                    unit = stringResource(Res.string.unit_bpm),
                    icon = Icons.Filled.Favorite,
                    isElevated = false,
                    isPro = uiState.isPro,
                    onClick = { onUiEvent(HomeUiEvent.OnNavigateToCalculator(HeartRateScreenRoute())) }
                )
            }

            // Water Intake
            item {
                val entry = uiState.latestWaterIntake
                DashboardMetricCard(
                    title = stringResource(Res.string.water_intake),
                    value = entry?.let { fmt(it.primaryValue) },
                    unit = stringResource(Res.string.unit_fl_oz),
                    icon = Icons.Filled.Info,
                    isElevated = false,
                    isPro = uiState.isPro,
                    onClick = { onUiEvent(HomeUiEvent.OnNavigateToCalculator(WaterIntakeScreenRoute())) }
                )
            }

            // Pro upgrade banner (if not Pro) - spans full width
            if (!uiState.isPro) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    UpgradePremiumBanner(
                        modifier = Modifier.padding(top = 8.dp)
                    )
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
