package com.measify.kappmaker.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.measify.kappmaker.presentation.components.bottomnav.BottomNavItem
import com.measify.kappmaker.presentation.screens.favorite.FavoriteScreenRoute
import com.measify.kappmaker.presentation.screens.home.HomeScreenRoute
import com.measify.kappmaker.presentation.screens.paywall.PaywallScreenRoute
import com.measify.kappmaker.presentation.screens.profile.ProfileScreenRoute
import com.measify.kappmaker.util.ScreenRoute
import com.measify.kappmaker.util.logging.AppLogger

class MainScreenRoute : Screen {

    @Composable
    override fun Content() {
        val startScreen = HomeScreenRoute()
        Navigator(startScreen) { navigator ->
            val currentScreen = navigator.lastItem
            val currentScreenTitle = (currentScreen as? ScreenRoute)?.title ?: ""
            val selectedBottomNavIndex =
                getBottomNavPositionByScreenRoute(currentScreen as ScreenRoute?)

            val bottomNavVisibleScreens = BottomNavItem.items().map { it.destination }
            val toolbarHiddenScreens = bottomNavVisibleScreens + listOf(PaywallScreenRoute())

            val isBottomNavVisible = bottomNavVisibleScreens.any { it.key == currentScreen.key }
            val isToolbarHidden = toolbarHiddenScreens.any { it.key == currentScreen.key }
            val mainScreenUiState = MainScreenUiState(
                bottomNavUiState = BottomNavUiState(
                    isVisible = isBottomNavVisible,
                    selectedBottomNavIndex = selectedBottomNavIndex
                ),
                toolbarUiState = ToolbarUiState(
                    isVisible = isToolbarHidden.not(),
                    text = currentScreenTitle
                ),
                contentPadding = if (currentScreen is PaywallScreenRoute) 0.dp else 20.dp
            )

            MainScreen(
                mainScreenUiState = mainScreenUiState,
                onUiEvent = { event ->
                    when (event) {
                        is MainScreenUiEvent.OnBottomNavItemClick -> {
                            navigator.popUntilRoot()
                            when (event.bottomNavItem.destination.key) {
                                startScreen.key -> Unit //Already popped all the screens
                                else -> navigator.push(event.bottomNavItem.destination)
                            }
                        }

                        MainScreenUiEvent.OnToolbarNavItemClick -> {
                            navigator.pop()
                        }
                    }
                },

                content = { CurrentScreen() }
            )
        }
    }


    @Composable
    private fun getBottomNavPositionByScreenRoute(screenRoute: ScreenRoute?): Int {
        if (screenRoute == null) return -1
        return BottomNavItem.items()
            .indexOfFirst { it.destination.key == screenRoute.key }
    }
}