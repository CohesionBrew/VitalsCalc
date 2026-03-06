package com.cohesionbrew.healthcalculator.presentation.screens.calculators

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
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
    CalculatorsScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent, onNavigateToCalculator = onNavigateToCalculator)
}

@Composable
fun CalculatorsScreen(
    uiState: CalculatorsUiState,
    onUiEvent: (CalculatorsUiEvent) -> Unit,
    onNavigateToCalculator: (ScreenRoute) -> Unit = {}
) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_calculators),
        isScrollableContent = false,
        includeBottomInsets = false
    ) {
        CalculatorTileGrid(
            tiles = uiState.tiles,
            onTileClick = { tile -> onNavigateToCalculator(tile.route as ScreenRoute) },
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}
