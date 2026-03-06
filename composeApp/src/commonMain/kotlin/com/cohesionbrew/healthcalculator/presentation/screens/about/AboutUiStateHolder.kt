package com.cohesionbrew.healthcalculator.presentation.screens.about

import com.cohesionbrew.healthcalculator.util.AppUtil
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AboutUiStateHolder(
    private val appUtil: AppUtil
) : UiStateHolder() {
    private val _uiState = MutableStateFlow(AboutUiState(appVersion = appUtil.getAppVersionInfo()))
    val uiState: StateFlow<AboutUiState> = _uiState.asStateFlow()

    fun onUiEvent(event: AboutUiEvent) {
        when (event) {
            AboutUiEvent.OnContactSupport -> appUtil.openFeedbackMail()
        }
    }
}
