package com.cohesionbrew.healthcalculator.root

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.cohesionbrew.healthcalculator.presentation.screens.main.MainScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.onboarding.OnBoardingScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.paywall.PaywallScreenRoute

val LocalNavigator = compositionLocalOf<NavHostController> {
    error("No LocalNavController provided")
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavigator provides navController) {
        NavHost(
            navController = navController,
            startDestination = OnBoardingScreenRoute()
        ) {
            composable<OnBoardingScreenRoute> { navBackStackEntry ->
                navBackStackEntry.toRoute<OnBoardingScreenRoute>().Content()
            }
            composable<MainScreenRoute> { navBackStackEntry ->
                navBackStackEntry.toRoute<MainScreenRoute>().Content()
            }
            composable<PaywallScreenRoute> { navBackStackEntry ->
                navBackStackEntry.toRoute<PaywallScreenRoute>().Content()
            }
        }
    }
}