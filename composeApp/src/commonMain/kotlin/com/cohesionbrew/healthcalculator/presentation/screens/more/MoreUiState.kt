package com.cohesionbrew.healthcalculator.presentation.screens.more

import androidx.compose.runtime.Immutable

@Immutable
data class MoreUiState(
    val isPro: Boolean = false
)

sealed interface MoreUiEvent
