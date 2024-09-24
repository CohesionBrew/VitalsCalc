package com.measify.kappbuilder.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.Navigator
import com.measify.kappbuilder.presentation.screens.favorite.FavoriteScreenRoute
import com.measify.kappbuilder.presentation.screens.home.HomeScreenRoute
import com.measify.kappbuilder.presentation.screens.paywall.PaywallScreenRoute
import com.measify.kappbuilder.presentation.screens.profile.ProfileScreenRoute
import com.measify.kappbuilder.util.ScreenRoute

class MainScreenRoute : Screen {

    @Composable
    override fun Content() {
        val startScreen = HomeScreenRoute()
        Navigator(startScreen) { navigator ->
            val currentScreen = navigator.lastItem
            val currentScreenTitle = (currentScreen as? ScreenRoute)?.title ?: ""
            val selectedBottomNavIndex = getBottomNavPositionByScreenRoute(currentScreen)

            val isBottomNavVisible =
                currentScreen is HomeScreenRoute
                        || currentScreen is ProfileScreenRoute
                        || currentScreen is FavoriteScreenRoute
            val mainScreenUiState = MainScreenUiState(
                bottomNavUiState = BottomNavUiState(
                    isVisible = isBottomNavVisible,
                    selectedBottomNavIndex = selectedBottomNavIndex
                ),
                toolbarUiState = ToolbarUiState(
                    isVisible = isBottomNavVisible.not() && currentScreen !is PaywallScreenRoute,
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
                            when (event.bottomNavItem.position) {
                                0 -> Unit //Already popped all the screens
                                else -> navigator.push(getScreenRouteByBottomNavPosition(event.bottomNavItem.position))
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


    private fun getScreenRouteByBottomNavPosition(position: Int): ScreenRoute {
        return when (position) {
            0 -> HomeScreenRoute()
            1 -> FavoriteScreenRoute()
            2 -> ProfileScreenRoute()
            else -> throw RuntimeException("ScreenRoute is not defined in position $position")
        }
    }

    private fun getBottomNavPositionByScreenRoute(screenRoute: Screen): Int {
        return when (screenRoute) {
            is HomeScreenRoute -> 0
            is FavoriteScreenRoute -> 1
            is ProfileScreenRoute -> 2
            else -> 0
        }
    }
}