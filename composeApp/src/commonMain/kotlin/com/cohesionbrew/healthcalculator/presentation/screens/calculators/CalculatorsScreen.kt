package com.cohesionbrew.healthcalculator.presentation.screens.calculators

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.generated.resources.Res
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_calculators
import com.cohesionbrew.healthcalculator.presentation.components.health.CalculatorTileGrid
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import org.jetbrains.compose.resources.stringResource

@Composable
fun CalculatorsScreen(
    uiStateHolder: CalculatorsUiStateHolder,
    onNavigateToCalculator: (ScreenRoute) -> Unit = {}
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    CalculatorsScreen(uiState = uiState, onNavigateToCalculator = onNavigateToCalculator)
}

/**
 * Calculators grid screen displaying all available calculators as tiles.
 *
 * Uses a responsive grid layout that adapts to screen width,
 * showing 2-4 columns depending on available space.
 * Matches the old VitalsCalc layout exactly: adaptive grid, square elevated
 * card tiles, "Calculators" section header spanning full width.
 */
@Composable
fun CalculatorsScreen(
    uiState: CalculatorsUiState,
    onNavigateToCalculator: (ScreenRoute) -> Unit = {}
) {
    CalculatorTileGrid(
        tiles = uiState.tiles,
        onTileClick = { tile -> onNavigateToCalculator(tile.route as ScreenRoute) },
        sectionTitle = stringResource(Res.string.title_screen_calculators)
    )
}
