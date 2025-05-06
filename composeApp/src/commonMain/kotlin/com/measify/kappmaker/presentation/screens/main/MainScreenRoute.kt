package com.measify.kappmaker.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.ic_back
import com.measify.kappmaker.presentation.components.bottomnav.BottomNavItem
import com.measify.kappmaker.presentation.screens.paywall.PaywallScreenRoute
import com.measify.kappmaker.root.LocalNavigator
import com.measify.kappmaker.util.ScreenRoute
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
class MainScreenRoute : ScreenRoute {


    @Composable
    override fun Content() {
        val navController = rememberNavController()
        CompositionLocalProvider(LocalNavigator provides navController) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            var currentScreenTitle by remember {
                mutableStateOf((currentDestination?.label ?: "").toString())
            }
            LaunchedEffect(currentDestination) {
                delay(100)
                currentScreenTitle = (currentDestination?.label ?: "").toString()
            }
            val bottomNavVisibleScreens = BottomNavItem.items().map { it.route }
            val toolbarHiddenScreens = listOf("PaywallScreenRoute")
            val isBottomNavVisible = bottomNavVisibleScreens.any {
                currentDestination?.route?.contains(it) == true
            }
            val isToolbarHidden = toolbarHiddenScreens.any {
                currentDestination?.route?.contains(it) == true
            }
            val selectedBottomNavIndex =
                getBottomNavPositionByScreenRoute(currentDestination?.route ?: "")
            val mainScreenUiState = MainScreenUiState(
                bottomNavUiState = BottomNavUiState(
                    isVisible = isBottomNavVisible,
                    selectedBottomNavIndex = selectedBottomNavIndex
                ),
                toolbarUiState = ToolbarUiState(
                    isVisible = isToolbarHidden.not(),
                    text = currentScreenTitle,
                    navigationIconRes = if (isBottomNavVisible) null else Res.drawable.ic_back
                ),
                contentPadding = 0.dp
            )

            MainScreen(
                mainScreenUiState = mainScreenUiState,
                onUiEvent = { event ->
                    when (event) {
                        is MainScreenUiEvent.OnBottomNavItemClick -> {
                            //TODO: Check later, for some reason saveState, and restoreState doesn't work
                            navController.navigate(event.bottomNavItem.destination) {
                                popUpTo(navController.graph.findStartDestination().id) {
//                                    saveState = true
                                }
                                launchSingleTop = true
//                                restoreState = true
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

    @Composable
    private fun getBottomNavPositionByScreenRoute(screenRoute: String): Int {
        return BottomNavItem.items()
            .indexOfFirst { screenRoute.contains(it.route) }
    }
}