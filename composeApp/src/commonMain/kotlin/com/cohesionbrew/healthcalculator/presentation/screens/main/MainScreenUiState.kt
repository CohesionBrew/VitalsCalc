package com.cohesionbrew.healthcalculator.presentation.screens.main

import com.cohesionbrew.healthcalculator.designsystem.components.bottomnav.BottomNavItem
import com.cohesionbrew.healthcalculator.util.ScreenRoute


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