package com.cohesionbrew.healthcalculator.presentation.components.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Represents a calculator tile displayed on the Calculators grid screen.
 *
 * @property titleRes String resource for the calculator name (localization-ready)
 * @property icon Icon to display on the tile
 * @property route The navigation destination when the tile is tapped
 */
data class CalculatorTile(
    val titleRes: StringResource,
    val icon: ImageVector,
    val route: Any
)

/**
 * Responsive grid of calculator tiles matching the old VitalsCalc layout.
 *
 * Uses [GridCells.Adaptive] with 100.dp minimum for a responsive grid
 * that shows 2-4 columns depending on screen width. Each tile is a
 * square [ElevatedCard] with an icon and title.
 *
 * @param tiles List of calculator tiles to display
 * @param sectionTitle Optional section header spanning full width
 * @param onTileClick Callback when a tile is tapped
 * @param modifier Modifier for the grid
 */
@Composable
fun CalculatorTileGrid(
    tiles: List<CalculatorTile>,
    onTileClick: (CalculatorTile) -> Unit,
    modifier: Modifier = Modifier,
    sectionTitle: String? = null
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 100.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Section header spans full width
        if (sectionTitle != null) {
            item(
                span = { GridItemSpan(maxLineSpan) }
            ) {
                Text(
                    text = sectionTitle,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }

        items(
            items = tiles,
            key = { it.route::class.simpleName ?: it.hashCode() }
        ) { tile ->
            CalculatorTileCard(
                tile = tile,
                onClick = { onTileClick(tile) }
            )
        }
    }
}

/**
 * Individual calculator tile card.
 *
 * Displays an icon and name in a square elevated card.
 * Has a minimum 48dp touch target for accessibility.
 *
 * @param tile The calculator tile data
 * @param onClick Callback when the tile is tapped
 */
@Composable
private fun CalculatorTileCard(
    tile: CalculatorTile,
    onClick: () -> Unit
) {
    val title = stringResource(tile.titleRes)

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(1f) // Square tiles
            .semantics {
                contentDescription = "$title calculator"
                role = Role.Button
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = tile.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
