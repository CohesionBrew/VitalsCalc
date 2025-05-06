package com.measify.kappmaker.presentation.components.bottomnav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.measify.kappmaker.presentation.components.PreviewHelper
import com.measify.kappmaker.presentation.theme.AppTheme
import com.measify.kappmaker.util.ScreenRoute
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    selectedIndex: Int = 0,
    onClickItem: (BottomNavItem) -> Unit
) {
    NavigationBar(
        modifier = modifier,
        containerColor = AppTheme.colors.bottomNav.background
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            NavigationBarItem(
                icon = {
                    item.icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = item.label,
                        )
                    }

                },
                label = {
                    Text(
                        text = item.label,
                        style = AppTheme.typography.bodyExtraSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AppTheme.colors.bottomNav.selectedTextIcon,
                    selectedTextColor = AppTheme.colors.bottomNav.selectedTextIcon,
                    indicatorColor = Color.Transparent,
                    unselectedIconColor = AppTheme.colors.bottomNav.unselectedTextIcon,
                    unselectedTextColor = AppTheme.colors.bottomNav.unselectedTextIcon,
                ),
                selected = isSelected,
                enabled = item.icon != null,
                onClick = { onClickItem(item) },
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    PreviewHelper {
        val emptyScreenRoute = object : ScreenRoute {
            @Composable
            override fun Content() {
            }
        }
        val bottomNavItems = listOf(
            BottomNavItem(
                label = "Home",
                icon = Icons.Default.Home,
                destination = emptyScreenRoute,
            ),
            BottomNavItem(
                label = "Favorite",
                icon = Icons.Default.Favorite,
                destination = emptyScreenRoute,
            ),
            BottomNavItem(
                label = "Profile",
                icon = Icons.Default.AccountCircle,
                destination = emptyScreenRoute,
            )
        )
        var currentSelectedIndex by remember { mutableStateOf(0) }
        BottomNavigationBar(
            selectedIndex = currentSelectedIndex,
            items = bottomNavItems,
            onClickItem = { clickedItem ->
                currentSelectedIndex = bottomNavItems.indexOfFirst {
                    it.label == clickedItem.label
                }
            }
        )
    }
}