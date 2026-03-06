package com.cohesionbrew.healthcalculator.designsystem.components.bottomnav

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.UiRes
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.bottom_nav_label_favorites
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.bottom_nav_label_home
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.bottom_nav_label_profile
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_favorite
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_home
import com.cohesionbrew.healthcalculator.designsystem.generated.resources.ic_profile
import com.cohesionbrew.healthcalculator.designsystem.theme.AppTheme
import com.cohesionbrew.healthcalculator.designsystem.util.PreviewHelper
import io.github.robinpcrd.cupertino.adaptive.AdaptiveNavigationBar
import io.github.robinpcrd.cupertino.adaptive.ExperimentalAdaptiveApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    items: List<BottomNavItem>,
    selectedIndex: Int = 0,
    onClickItem: (BottomNavItem) -> Unit
) {
    AdaptiveNavigationBar(
        modifier = modifier,
        adaptation = {
            material {
                containerColor = AppTheme.colors.bottomNav.background
            }
            cupertino {
                isTranslucent = true
            }
        }
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedIndex == index
            NavigationBarItem(
                icon = {
                    item.icon?.let {
                        Icon(
                            modifier= Modifier.size(24.dp),
                            painter = painterResource(it),
                            contentDescription = stringResource(item.label),
                        )
                    }

                },
                label = {
                    Text(
                        text = stringResource(item.label),
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
internal fun BottomNavigationBarPreview() {
    PreviewHelper {

        val bottomNavItems = listOf(
            BottomNavItem(
                label = UiRes.string.bottom_nav_label_home,
                icon = UiRes.drawable.ic_home,
            ),
            BottomNavItem(
                label = UiRes.string.bottom_nav_label_favorites,
                icon = UiRes.drawable.ic_favorite,
            ),
            BottomNavItem(
                label = UiRes.string.bottom_nav_label_profile,
                icon = UiRes.drawable.ic_profile,
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
