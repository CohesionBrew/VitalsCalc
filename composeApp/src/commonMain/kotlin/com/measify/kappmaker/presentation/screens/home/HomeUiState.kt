package com.measify.kappmaker.presentation.screens.home

class HomeUiState()

sealed class HomeUiEvent {
    data object OnClick : HomeUiEvent()
}