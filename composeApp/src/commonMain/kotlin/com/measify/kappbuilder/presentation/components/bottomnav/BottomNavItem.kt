package com.measify.kappbuilder.presentation.components.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.measify.kappbuilder.generated.resources.Res
import com.measify.kappbuilder.generated.resources.bottom_nav_label_favorites
import com.measify.kappbuilder.generated.resources.bottom_nav_label_home
import com.measify.kappbuilder.generated.resources.bottom_nav_label_profile
import org.jetbrains.compose.resources.stringResource


data class BottomNavItem(
    val position: Int,
    val label: String,
    val icon: ImageVector? = null
) {
    companion object {

        @Composable
        fun items(): List<BottomNavItem> {
            return listOf(
                BottomNavItem(
                    position = 0,
                    label = stringResource(Res.string.bottom_nav_label_home),
                    icon = Icons.Default.Home
                ),
                BottomNavItem(
                    position = 1,
                    label = stringResource(Res.string.bottom_nav_label_favorites),
                    icon = Icons.Default.Favorite
                ),
                BottomNavItem(
                    position = 2,
                    label = stringResource(Res.string.bottom_nav_label_profile),
                    icon = Icons.Filled.AccountCircle
                ),
            )
        }
    }
}