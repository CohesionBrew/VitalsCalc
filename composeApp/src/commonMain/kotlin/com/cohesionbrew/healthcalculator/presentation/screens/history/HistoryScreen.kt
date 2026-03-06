package com.cohesionbrew.healthcalculator.presentation.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthAlertDialog
import com.cohesionbrew.healthcalculator.generated.resources.*
import com.cohesionbrew.healthcalculator.presentation.components.health.HistoryEntryCard
import com.cohesionbrew.healthcalculator.presentation.components.health.HistoryFilterChips
import org.jetbrains.compose.resources.stringResource

@Composable
fun HistoryScreen(uiStateHolder: HistoryUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    HistoryScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun HistoryScreen(uiState: HistoryUiState, onUiEvent: (HistoryUiEvent) -> Unit) {
    if (uiState.showClearConfirmation) {
        HealthAlertDialog(
            title = stringResource(Res.string.history_clear_confirm_title),
            message = stringResource(Res.string.history_clear_confirm_message),
            confirmText = stringResource(Res.string.history_clear_all),
            dismissText = stringResource(Res.string.cancel),
            onConfirm = { onUiEvent(HistoryUiEvent.OnConfirmClear) },
            onDismiss = { onUiEvent(HistoryUiEvent.OnDismissClear) }
        )
    }

    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_history),
        isScrollableContent = false,
        includeBottomInsets = false,
        toolbarExtraContent = {
            if (uiState.entries.isNotEmpty()) {
                IconButton(onClick = { onUiEvent(HistoryUiEvent.OnClearHistory) }) {
                    Icon(Icons.Filled.Delete, contentDescription = stringResource(Res.string.history_clear_icon_desc))
                }
            }
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            HistoryFilterChips(
                selectedType = uiState.selectedFilter,
                onTypeSelected = { onUiEvent(HistoryUiEvent.OnFilterSelected(it)) }
            )

            if (!uiState.isPro && uiState.selectedFilter == null) {
                Text(
                    stringResource(Res.string.history_limited_banner),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            if (uiState.entries.isEmpty() && !uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(stringResource(Res.string.history_empty_message), style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.entries, key = { it.id }) { entry ->
                        HistoryEntryCard(entry = entry)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}
