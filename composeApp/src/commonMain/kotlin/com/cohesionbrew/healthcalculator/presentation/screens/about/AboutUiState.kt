package com.cohesionbrew.healthcalculator.presentation.screens.about

import androidx.compose.runtime.Immutable

@Immutable
data class AboutUiState(
    val appVersion: String = ""
)

sealed interface AboutUiEvent {
    data object OnContactSupport : AboutUiEvent
}
