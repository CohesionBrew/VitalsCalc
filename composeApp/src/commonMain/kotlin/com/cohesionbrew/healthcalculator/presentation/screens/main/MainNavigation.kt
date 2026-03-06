package com.cohesionbrew.healthcalculator.presentation.screens.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.cohesionbrew.healthcalculator.generated.resources.Res
import com.cohesionbrew.healthcalculator.generated.resources.help_and_support
import com.cohesionbrew.healthcalculator.generated.resources.subscriptions
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_about
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_account
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_blood_pressure
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_bmi
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_bmr
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_body_fat
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_calculators
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_heart_rate
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_history
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_home
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_ideal_weight
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_more
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_premium
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_profile
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_references
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_settings
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_user_profile
import com.cohesionbrew.healthcalculator.generated.resources.title_screen_water_intake
import com.cohesionbrew.healthcalculator.generated.resources.title_sign_in
import com.cohesionbrew.healthcalculator.presentation.screens.about.AboutScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.account.AccountScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bloodpressure.BloodPressureScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bmi.BmiScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bmr.BmrScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.bodyfat.BodyFatScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.calculators.CalculatorsScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.heartrate.HeartRateScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.helpandsupport.HelpAndSupportScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.history.HistoryScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.home.HomeScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.idealweight.IdealWeightScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.more.MoreScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.paywall.PaywallScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.profile.ProfileScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.references.ReferencesScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.settings.SettingsScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.signin.SignInScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.subscriptions.SubscriptionsScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.userprofile.UserProfileScreenRoute
import com.cohesionbrew.healthcalculator.presentation.screens.waterintake.WaterIntakeScreenRoute
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = HomeScreenRoute()
    ) {
        // Bottom nav tabs
        composable<HomeScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_home)
            navBackStackEntry.toRoute<HomeScreenRoute>().Content()
        }
        composable<CalculatorsScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_calculators)
            navBackStackEntry.toRoute<CalculatorsScreenRoute>().Content()
        }
        composable<HistoryScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_history)
            navBackStackEntry.toRoute<HistoryScreenRoute>().Content()
        }
        composable<MoreScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_more)
            navBackStackEntry.toRoute<MoreScreenRoute>().Content()
        }

        // Calculator screens
        composable<BmiScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_bmi)
            navBackStackEntry.toRoute<BmiScreenRoute>().Content()
        }
        composable<BmrScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_bmr)
            navBackStackEntry.toRoute<BmrScreenRoute>().Content()
        }
        composable<HeartRateScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_heart_rate)
            navBackStackEntry.toRoute<HeartRateScreenRoute>().Content()
        }
        composable<BodyFatScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_body_fat)
            navBackStackEntry.toRoute<BodyFatScreenRoute>().Content()
        }
        composable<IdealWeightScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_ideal_weight)
            navBackStackEntry.toRoute<IdealWeightScreenRoute>().Content()
        }
        composable<WaterIntakeScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_water_intake)
            navBackStackEntry.toRoute<WaterIntakeScreenRoute>().Content()
        }
        composable<BloodPressureScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_blood_pressure)
            navBackStackEntry.toRoute<BloodPressureScreenRoute>().Content()
        }

        // More sub-screens
        composable<SettingsScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_settings)
            navBackStackEntry.toRoute<SettingsScreenRoute>().Content()
        }
        composable<UserProfileScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_user_profile)
            navBackStackEntry.toRoute<UserProfileScreenRoute>().Content()
        }
        composable<AboutScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_about)
            navBackStackEntry.toRoute<AboutScreenRoute>().Content()
        }
        composable<ReferencesScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_references)
            navBackStackEntry.toRoute<ReferencesScreenRoute>().Content()
        }

        // Auth & subscription screens
        composable<AccountScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_account)
            navBackStackEntry.toRoute<AccountScreenRoute>().Content()
        }
        composable<ProfileScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_profile)
            navBackStackEntry.toRoute<ProfileScreenRoute>().Content()
        }
        composable<SignInScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_sign_in)
            navBackStackEntry.toRoute<SignInScreenRoute>().Content()
        }
        composable<PaywallScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.title_screen_premium)
            navBackStackEntry.toRoute<PaywallScreenRoute>().Content()
        }
        composable<HelpAndSupportScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.help_and_support)
            navBackStackEntry.toRoute<HelpAndSupportScreenRoute>().Content()
        }
        composable<SubscriptionsScreenRoute> { navBackStackEntry ->
            navBackStackEntry.destination.label =
                stringResource(Res.string.subscriptions)
            navBackStackEntry.toRoute<SubscriptionsScreenRoute>().Content()
        }
    }
}
