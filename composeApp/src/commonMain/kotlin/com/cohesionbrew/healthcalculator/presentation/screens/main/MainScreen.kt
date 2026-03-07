package com.cohesionbrew.healthcalculator.presentation.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.snap
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.cohesionbrew.healthcalculator.designsystem.components.bottomnav.BottomNavigationBar
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.UiRes
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_back
import com.cohesionbrew.healthcalculator.util.extensions.isKeyboardOpen
import org.jetbrains.compose.resources.painterResource


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    mainScreenUiState: MainScreenUiState = MainScreenUiState(),
    onUiEvent: (MainScreenUiEvent) -> Unit = {},
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Top))
        ) {
            //Main Content
            Box(modifier = Modifier.weight(1f)) {
                content()

                // Overlay back button on non-root (calculator/detail) screens
                if (!mainScreenUiState.bottomNavUiState.isVisible) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 4.dp, top = 8.dp)
                            .size(48.dp)
                            .clip(CircleShape)
                            .clickable(
                                onClick = { onUiEvent(MainScreenUiEvent.OnToolbarNavItemClick) },
                                role = Role.Button,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = false, radius = 20.dp)
                            )
                            .padding(10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(UiRes.drawable.ic_back),
                            contentDescription = "Back"
                        )
                    }
                }
            }

            //BottomNavigation Bar
            val isBottomNavVisible =
                mainScreenUiState.bottomNavUiState.isVisible && !isKeyboardOpen()
            val bottomNavItems =
                remember { mainScreenUiState.bottomNavUiState.items.map { it.first } }
            AnimatedVisibility(isBottomNavVisible, enter = fadeIn(snap()), exit = fadeOut(snap())) {
                BottomNavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    items = bottomNavItems,
                    selectedIndex = mainScreenUiState.bottomNavUiState.selectedBottomNavIndex
                ) {
                    onUiEvent(MainScreenUiEvent.OnBottomNavItemClick(it))
                }
            }
        }
    }
}


