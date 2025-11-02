package com.measify.kappmaker.presentation.screens.main

import com.measify.kappmaker.designsystem.components.bottomnav.BottomNavItem
import com.measify.kappmaker.util.ScreenRoute


data class MainScreenUiState(
    val bottomNavUiState: BottomNavUiState = BottomNavUiState()
)

data class BottomNavUiState(
    val items: List<Pair<BottomNavItem, ScreenRoute>> = emptyList(),
    val isVisible: Boolean = true,
    val selectedBottomNavIndex: Int = 0
)

sealed interface MainScreenUiEvent {
    data class OnBottomNavItemClick(val bottomNavItem: BottomNavItem) : MainScreenUiEvent
    data object OnToolbarNavItemClick : MainScreenUiEvent
}