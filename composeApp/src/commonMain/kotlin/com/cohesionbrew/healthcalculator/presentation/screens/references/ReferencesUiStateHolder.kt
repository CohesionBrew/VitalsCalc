package com.cohesionbrew.healthcalculator.presentation.screens.references

import com.cohesionbrew.healthcalculator.util.UiStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ReferencesUiStateHolder : UiStateHolder() {
    private val _uiState = MutableStateFlow(ReferencesUiState())
    val uiState: StateFlow<ReferencesUiState> = _uiState.asStateFlow()

    fun onUiEvent(event: ReferencesUiEvent) {}
}
