package com.cohesionbrew.healthcalculator.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cohesionbrew.healthcalculator.designsystem.components.bottomnav.BottomNavItem
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.UiRes
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_calculator
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_history
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_home
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_more
import com.cohesionbrew.healthcalculator.generated.resources.bottom_nav_label_calculators
import com.cohesionbrew.healthcalculator.generated.resources.bottom_nav_label_history
import com.cohesionbrew.healthcalculator.generated.resources.bottom_nav_label_home
import com.cohesionbrew.healthcalculator.generated.resources.bottom_nav_label_more
import com.cohesionbrew.healthcalculator.presentation.screens.calculators.CalculatorsScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.history.HistoryScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.home.HomeScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.more.MoreScreenRoute
import com.cohesionbrew.healthcalculator.root.LocalNavigator
import com.cohesionbrew.healthcalculator.util.ScreenRoute
import kotlinx.serialization.Serializable

@Serializable
class MainScreenRoute : ScreenRoute {


    @Composable
    override fun Content() {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavigator provides navController) {
            val bottomNavItemsWithDestinations = remember { getBottomNavItemsWithDestination() }
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            val bottomNavVisibleScreens =
                remember(bottomNavItemsWithDestinations) { bottomNavItemsWithDestinations.map { it.first.route } }
            val isBottomNavVisible = bottomNavVisibleScreens.any {
                currentDestination?.route?.contains(it) == true
            }

            val selectedBottomNavIndex =
                bottomNavItemsWithDestinations.getPositionByRouteId(currentDestination?.route)
            val mainScreenUiState = MainScreenUiState(
                bottomNavUiState = BottomNavUiState(
                    items = bottomNavItemsWithDestinations,
                    isVisible = isBottomNavVisible,
                    selectedBottomNavIndex = selectedBottomNavIndex,
                )
            )

            MainScreen(
                mainScreenUiState = mainScreenUiState,
                onUiEvent = { event ->
                    when (event) {
                        is MainScreenUiEvent.OnBottomNavItemClick -> {
                            val screenRoute =
                                bottomNavItemsWithDestinations.getScreenRouteByRouteId(event.bottomNavItem.route)
                            navController.navigate(screenRoute) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }

                        MainScreenUiEvent.OnToolbarNavItemClick -> {
                            navController.navigateUp()
                        }
                    }
                },

                content = {
                    MainNavigation(navController)
                }
            )

        }
    }

    private fun getBottomNavItemsWithDestination(): List<Pair<BottomNavItem, ScreenRoute>> {

        //MAKE SURE route strings are same as ScreenRoute class names. Ex: HomeScreenRoute.
        return listOf(
            BottomNavItem(
                label = com.cohesionbrew.healthcalculator.generated.resources.Res.string.bottom_nav_label_home,
                icon = UiRes.drawable.ic_home,
                route = "HomeScreenRoute"
            ) to HomeScreenRoute(),
            BottomNavItem(
                label = com.cohesionbrew.healthcalculator.generated.resources.Res.string.bottom_nav_label_calculators,
                icon = UiRes.drawable.ic_calculator,
                route = "CalculatorsScreenRoute"
            ) to CalculatorsScreenRoute(),
            BottomNavItem(
                label = com.cohesionbrew.healthcalculator.generated.resources.Res.string.bottom_nav_label_history,
                icon = UiRes.drawable.ic_history,
                route = "HistoryScreenRoute"
            ) to HistoryScreenRoute(),
            BottomNavItem(
                label = com.cohesionbrew.healthcalculator.generated.resources.Res.string.bottom_nav_label_more,
                icon = UiRes.drawable.ic_more,
                route = "MoreScreenRoute"
            ) to MoreScreenRoute()
        )
    }

    private fun List<Pair<BottomNavItem, ScreenRoute>>.getPositionByRouteId(route: String?): Int {
        val index = indexOfFirst { route?.contains(it.first.route) == true }
        return if (index >= 0) index else 0
    }

    private fun List<Pair<BottomNavItem, ScreenRoute>>.getScreenRouteByRouteId(route: String?): ScreenRoute {
        return this.firstOrNull { it.first.route == route }?.second
            ?: HomeScreenRoute() //Default to Home Screen
    }
}