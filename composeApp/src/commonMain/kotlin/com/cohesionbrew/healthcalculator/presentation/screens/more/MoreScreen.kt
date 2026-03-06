package com.cohesionbrew.healthcalculator.presentation.screens.more

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.AppCardContainer
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun MoreScreen(uiStateHolder: MoreUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    MoreScreen(
        uiState = uiState,
        onUiEvent = uiStateHolder::onUiEvent
    )
}

@Composable
fun MoreScreen(
    uiState: MoreUiState,
    onUiEvent: (MoreUiEvent) -> Unit,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {},
    onNavigateToAbout: () -> Unit = {},
    onNavigateToReferences: () -> Unit = {},
    onNavigateToPremium: () -> Unit = {}
) {
    ScreenWithToolbar(
        title = stringResource(Res.string.title_screen_more),
        isScrollableContent = true,
        includeBottomInsets = false
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (!uiState.isPro) {
                MoreMenuItem(
                    icon = Icons.Filled.Star,
                    title = stringResource(Res.string.upgrade_to_pro),
                    subtitle = stringResource(Res.string.upgrade_to_pro_subtitle),
                    onClick = onNavigateToPremium
                )
            }

            MoreMenuItem(
                icon = Icons.Filled.Person,
                title = stringResource(Res.string.my_profile),
                subtitle = stringResource(Res.string.my_profile_subtitle),
                onClick = onNavigateToProfile
            )

            MoreMenuItem(
                icon = Icons.Filled.Settings,
                title = stringResource(Res.string.settings),
                subtitle = stringResource(Res.string.settings_subtitle),
                onClick = onNavigateToSettings
            )

            MoreMenuItem(
                icon = Icons.Filled.Menu,
                title = stringResource(Res.string.references),
                subtitle = stringResource(Res.string.references_subtitle),
                onClick = onNavigateToReferences
            )

            MoreMenuItem(
                icon = Icons.Filled.Info,
                title = stringResource(Res.string.about),
                subtitle = stringResource(Res.string.about_subtitle),
                onClick = onNavigateToAbout
            )
        }
    }
}

@Composable
private fun MoreMenuItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    AppCardContainer(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
