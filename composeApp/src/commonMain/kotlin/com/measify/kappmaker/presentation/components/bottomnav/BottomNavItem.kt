package com.measify.kappmaker.presentation.components.bottomnav

import androidx.compose.runtime.Composable
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.bottom_nav_label_favorites
import com.measify.kappmaker.generated.resources.bottom_nav_label_home
import com.measify.kappmaker.generated.resources.bottom_nav_label_profile
import com.measify.kappmaker.generated.resources.ic_favorite
import com.measify.kappmaker.generated.resources.ic_home
import com.measify.kappmaker.generated.resources.ic_profile
import com.measify.kappmaker.presentation.screens.account.AccountScreenRoute
import com.measify.kappmaker.presentation.screens.favorite.FavoriteScreenRoute
import com.measify.kappmaker.presentation.screens.home.HomeScreenRoute
import com.measify.kappmaker.util.ScreenRoute
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.stringResource


data class BottomNavItem(
    val label: String,
    val icon: DrawableResource? = null,
    val destination: ScreenRoute,
    val route: String = ""
) {
    companion object {

        @Composable
        fun items(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    label = stringResource(Res.string.bottom_nav_label_home),
                    icon = Res.drawable.ic_home,
                    destination = HomeScreenRoute(),
                    route="HomeScreenRoute"
                ),
                BottomNavItem(
                    label = stringResource(Res.string.bottom_nav_label_favorites),
                    icon = Res.drawable.ic_favorite,
                    destination = FavoriteScreenRoute(),
                    route="FavoriteScreenRoute"
                ),
                BottomNavItem(
                    label = stringResource(Res.string.bottom_nav_label_profile),
                    icon = Res.drawable.ic_profile,
                    destination = AccountScreenRoute(),
                    route = "AccountScreenRoute"
                ),
            )
        }
    }
}