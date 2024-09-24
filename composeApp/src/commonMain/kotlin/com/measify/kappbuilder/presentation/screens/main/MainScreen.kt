package com.measify.kappbuilder.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.measify.kappbuilder.presentation.components.AppToolbar
import com.measify.kappbuilder.presentation.components.bottomnav.BottomNavItem
import com.measify.kappbuilder.presentation.components.bottomnav.BottomNavigationBar


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    mainScreenUiState: MainScreenUiState = MainScreenUiState(),
    onUiEvent: (MainScreenUiEvent) -> Unit = {},
) {
    Box(modifier = modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            //AppBar
            if (mainScreenUiState.toolbarUiState.isVisible) {
                AppToolbar(
                    title = mainScreenUiState.toolbarUiState.text,
                    onNavigationIconClick = { onUiEvent(MainScreenUiEvent.OnToolbarNavItemClick) }
                )
            }
            //Main Content
            Box(modifier = Modifier.weight(1f).padding(mainScreenUiState.contentPadding)) {
                content()
            }
            //BottomNavigation Bar
            if (mainScreenUiState.bottomNavUiState.isVisible) {
                BottomNavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    items = BottomNavItem.items(),
                    selectedIndex = mainScreenUiState.bottomNavUiState.selectedBottomNavIndex
                ) {
                    onUiEvent(MainScreenUiEvent.OnBottomNavItemClick(it))
                }
            }
        }
    }
}


