package com.measify.kappmaker.presentation.components.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.bottom_nav_label_favorites
import com.measify.kappmaker.generated.resources.bottom_nav_label_home
import com.measify.kappmaker.generated.resources.bottom_nav_label_profile
import com.measify.kappmaker.presentation.screens.favorite.FavoriteScreenRoute
import com.measify.kappmaker.presentation.screens.home.HomeScreenRoute
import com.measify.kappmaker.presentation.screens.profile.ProfileScreenRoute
import com.measify.kappmaker.util.ScreenRoute
import org.jetbrains.compose.resources.stringResource


data class BottomNavItem(
    val label: String,
    val icon: ImageVector? = null,
    val destination: ScreenRoute,
) {
    companion object {

        @Composable
        fun items(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    label = stringResource(Res.string.bottom_nav_label_home),
                    icon = Icons.Default.Home,
                    destination = HomeScreenRoute()
                ),
                BottomNavItem(
                    label = stringResource(Res.string.bottom_nav_label_favorites),
                    icon = Icons.Default.Favorite,
                    destination = FavoriteScreenRoute()
                ),
                BottomNavItem(
                    label = stringResource(Res.string.bottom_nav_label_profile),
                    icon = Icons.Filled.AccountCircle,
                    destination = ProfileScreenRoute()
                ),
            )
        }
    }
}