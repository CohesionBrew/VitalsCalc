package com.measify.kappmaker.presentation.screens.home

import com.measify.kappmaker.util.UiStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeUiStateHolder() : UiStateHolder() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    fun onUiEvent(event: HomeUiEvent){
        when(event){
            HomeUiEvent.OnClick -> TODO()
        }
    }
}