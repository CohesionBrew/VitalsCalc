package com.cohesionbrew.healthcalculator.presentation.screens.more

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.presentation.screens.about.AboutScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.references.ReferencesScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.settings.SettingsScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.userprofile.UserProfileScreenRoute
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import com.cohesionbrew.healthcalculator.util.uiStateHolder
import kotlinx.serialization.Serializable

@Serializable
class MoreScreenRoute : ScreenRoute {
    @Composable
    override fun Content() {
        val holder = uiStateHolder<MoreUiStateHolder>()
        val uiState by holder.uiState.collectAsStateWithLifecycle()
        val navigator = LocalNavigator.current
        MoreScreen(
            uiState = uiState,
            onUiEvent = holder::onUiEvent,
            onNavigateToSettings = { navigator.navigate(SettingsScreenRoute()) },
            onNavigateToProfile = { navigator.navigate(UserProfileScreenRoute()) },
            onNavigateToAbout = { navigator.navigate(AboutScreenRoute()) },
            onNavigateToReferences = { navigator.navigate(ReferencesScreenRoute()) },
            onNavigateToPremium = { navigator.navigate(com.cohesionbrew.healthcalculator.presentation.screens.paywall.PaywallScreenRoute()) }
        )
    }
}
