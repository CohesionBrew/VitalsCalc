package com.cohesionbrew.healthcalculator.presentation.screens.references

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.AppCardContainer
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.generated.resources.Res
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_references
import org.jetbrains.compose.resources.stringResource

@Composable
fun ReferencesScreen(uiStateHolder: ReferencesUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    ReferencesScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun ReferencesScreen(
    uiState: ReferencesUiState,
    onUiEvent: (ReferencesUiEvent) -> Unit
) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_references),
        isScrollableContent = false,
        includeBottomInsets = true
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.references) { ref ->
                ReferenceCard(ref)
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun ReferenceCard(item: ReferenceItem) {
    AppCardContainer(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                item.calculator,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                item.formula,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                item.source,
                style = MaterialTheme.typography.bodySmall,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
