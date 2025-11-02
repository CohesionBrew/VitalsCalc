package com.measify.kappmaker.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.measify.kappmaker.designsystem.components.ScreenWithToolbar
import com.measify.kappmaker.designsystem.theme.AppTheme
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.title_screen_home
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: HomeUiStateHolder,
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()

    HomeScreen(
        modifier = modifier.fillMaxSize(),
        uiState = uiState,
        onUiEvent = uiStateHolder::onUiEvent
    )
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
        includeBottomInsets = false // Set to true if bottom nav is not visible
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.sectionSpacing)) {
            Text("HomeScreen")
        }
    }
}