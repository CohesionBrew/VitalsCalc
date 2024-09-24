package com.measify.kappmaker.presentation.screens.main

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.measify.kappmaker.presentation.components.bottomnav.BottomNavItem


data class MainScreenUiState(
    val bottomNavUiState: BottomNavUiState = BottomNavUiState(),
    val toolbarUiState: ToolbarUiState = ToolbarUiState(),
    val contentPadding: Dp = 20.dp
)

data class ToolbarUiState(
    val isVisible: Boolean = false,
    val text: String = "",
)

data class BottomNavUiState(
    val isVisible: Boolean = true,
    val selectedBottomNavIndex: Int = 0
)

sealed interface MainScreenUiEvent {
    data class OnBottomNavItemClick(val bottomNavItem: BottomNavItem) : MainScreenUiEvent
    data object OnToolbarNavItemClick : MainScreenUiEvent
}