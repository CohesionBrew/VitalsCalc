package com.cohesionbrew.healthcalculator.presentation.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutScreen(uiStateHolder: AboutUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    AboutScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun AboutScreen(
    uiState: AboutUiState,
    onUiEvent: (AboutUiEvent) -> Unit
) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_about),
        isScrollableContent = true,
        includeBottomInsets = true
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                stringResource(Res.string.about_tagline),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (uiState.appVersion.isNotEmpty()) {
                Text(
                    stringResource(Res.string.version, uiState.appVersion),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                stringResource(Res.string.about_description),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                stringResource(Res.string.about_disclaimer),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { onUiEvent(AboutUiEvent.OnContactSupport) }) {
                Text(stringResource(Res.string.item_contact_support))
            }
        }
    }
}
