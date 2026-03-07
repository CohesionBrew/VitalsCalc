package com.cohesionbrew.healthcalculator.presentation.screens.bodyfat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.domain.model.BodyFatCategory
import com.cohesionbrew.healthcalculator.domain.model.BodyFatMethod
import com.cohesionbrew.healthcalculator.generated.resources.*
import com.cohesionbrew.healthcalculator.presentation.components.health.getBodyFatCategoryColor
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun BodyFatScreen(uiStateHolder: BodyFatUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BodyFatScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BodyFatScreen(uiState: BodyFatUiState, onUiEvent: (BodyFatUiEvent) -> Unit) {
    val title = stringResource(Res.string.bodyfat_title)

    // Build subtitle showing profile data based on selected method (matches old app)
    val genderText = if (uiState.gender == "male") stringResource(Res.string.male) else stringResource(Res.string.female)
    val heightText = "${uiState.heightCm.toInt()} ${stringResource(Res.string.unit_cm)}"
    val subtitle = "$genderText, $heightText"

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            LandscapeLayout(
                title = title,
                subtitle = subtitle,
                uiState = uiState,
                onUiEvent = onUiEvent
            )
        } else {
            PortraitLayout(
                title = title,
                subtitle = subtitle,
                uiState = uiState,
                onUiEvent = onUiEvent
            )
        }
    }
}

// --- Portrait Layout (matches old StandardHealthScaffold PortraitLayout) ---

@Composable
private fun PortraitLayout(
    title: String,
    subtitle: String,
    uiState: BodyFatUiState,
    onUiEvent: (BodyFatUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HealthScreenTitle(text = title)

            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onBackground
            )

            // Input card (matches old MeasurementInputCard)
            MeasurementInputCard(
                uiState = uiState,
                onUiEvent = onUiEvent,
                modifier = Modifier.fillMaxWidth()
            )

            // Calculate button
            HealthActionButton(
                text = stringResource(Res.string.calculate),
                isLoading = uiState.isLoading,
                onClick = { onUiEvent(BodyFatUiEvent.OnCalculate) }
            )

            // Result card (only shown after calculation)
            if (uiState.isCalculated) {
                BodyFatResultCard(
                    uiState = uiState,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        // Ad banner slot would go here if needed
    }
}

// --- Landscape Layout (matches old StandardHealthScaffold LandscapeLayout) ---

@Composable
private fun LandscapeLayout(
    title: String,
    subtitle: String,
    uiState: BodyFatUiState,
    onUiEvent: (BodyFatUiEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Left Column: title + subtitle + inputs + button
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HealthScreenTitle(text = title)

                Text(
                    text = subtitle,
                    color = MaterialTheme.colorScheme.onBackground
                )

                MeasurementInputCard(
                    uiState = uiState,
                    onUiEvent = onUiEvent
                )

                HealthActionButton(
                    text = stringResource(Res.string.calculate),
                    isLoading = uiState.isLoading,
                    onClick = { onUiEvent(BodyFatUiEvent.OnCalculate) }
                )
            }

            // Right Column: results
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (uiState.isCalculated) {
                    BodyFatResultCard(uiState = uiState)
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(Res.string.tap_to_calculate),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        // Ad banner slot would go here if needed
    }
}

// --- MeasurementInputCard (matches old app's MeasurementInputCard) ---

@Composable
private fun MeasurementInputCard(
    uiState: BodyFatUiState,
    onUiEvent: (BodyFatUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val navyMethodLabel = stringResource(Res.string.bodyfat_method_navy)
    val rfmMethodLabel = stringResource(Res.string.bodyfat_method_rfm)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Method selection: label and chips inline (matches old app)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.label_method),
                    style = MaterialTheme.typography.titleMedium
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.method == BodyFatMethod.RFM,
                        onClick = { onUiEvent(BodyFatUiEvent.OnMethodChanged(BodyFatMethod.RFM)) },
                        label = { Text(rfmMethodLabel) }
                    )
                    FilterChip(
                        selected = uiState.method == BodyFatMethod.NAVY,
                        onClick = { onUiEvent(BodyFatUiEvent.OnMethodChanged(BodyFatMethod.NAVY)) },
                        label = { Text(navyMethodLabel) }
                    )
                }
            }

            // Method-specific measurement inputs
            when (uiState.method) {
                BodyFatMethod.NAVY -> NavyMethodInputs(uiState, onUiEvent)
                BodyFatMethod.RFM -> RfmMethodInputs(uiState, onUiEvent)
            }
        }
    }
}

@Composable
private fun NavyMethodInputs(
    uiState: BodyFatUiState,
    onUiEvent: (BodyFatUiEvent) -> Unit
) {
    val unitLabel = stringResource(Res.string.unit_cm)

    // Waist and Neck side by side (matches old app layout)
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        FormattedDoubleTextField(
            value = formatDoubleDisplay(uiState.waistCm),
            onValueChange = { onUiEvent(BodyFatUiEvent.OnWaistChanged(it ?: 0.0)) },
            label = { Text(stringResource(Res.string.bodyfat_waist)) },
            suffix = { Text(unitLabel) },
            modifier = Modifier.weight(1f)
        )

        FormattedDoubleTextField(
            value = formatDoubleDisplay(uiState.neckCm),
            onValueChange = { onUiEvent(BodyFatUiEvent.OnNeckChanged(it ?: 0.0)) },
            label = { Text(stringResource(Res.string.bodyfat_neck)) },
            suffix = { Text(unitLabel) },
            modifier = Modifier.weight(1f)
        )
    }

    // Hip measurement required for females - half width for visual consistency (matches old app)
    if (uiState.gender == "female") {
        Row(modifier = Modifier.fillMaxWidth()) {
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.hipCm),
                onValueChange = { onUiEvent(BodyFatUiEvent.OnHipChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.bodyfat_hip)) },
                suffix = { Text(unitLabel) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun RfmMethodInputs(
    uiState: BodyFatUiState,
    onUiEvent: (BodyFatUiEvent) -> Unit
) {
    val unitLabel = stringResource(Res.string.unit_cm)

    // Half width for visual consistency with Navy method (matches old app)
    Row(modifier = Modifier.fillMaxWidth()) {
        FormattedDoubleTextField(
            value = formatDoubleDisplay(uiState.waistCm),
            onValueChange = { onUiEvent(BodyFatUiEvent.OnWaistChanged(it ?: 0.0)) },
            label = { Text(stringResource(Res.string.bodyfat_waist)) },
            suffix = { Text(unitLabel) },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

// --- BodyFatResultCard (matches old app's BodyFatResultCard) ---

@Composable
private fun BodyFatResultCard(
    uiState: BodyFatUiState,
    modifier: Modifier = Modifier
) {
    val category = uiState.category ?: return
    val categoryLabel = getBodyFatCategoryLabel(category)
    val categoryColor = getBodyFatCategoryColor(category)
    val isMale = uiState.gender == "male"

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.bodyfat_result_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Large percentage display
            Text(
                text = "${((uiState.resultPercent * 10).roundToInt() / 10.0)}%",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Category indicator with colored dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .clip(CircleShape)
                        .background(categoryColor)
                )
                Text(
                    text = categoryLabel,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider()

            // Category reference table
            BodyFatCategoryReferenceTable(isMale = isMale, currentCategory = category)
        }
    }
}

/**
 * Returns the localized label for a body fat category.
 */
@Composable
private fun getBodyFatCategoryLabel(category: BodyFatCategory): String {
    return when (category) {
        BodyFatCategory.ESSENTIAL -> stringResource(Res.string.bodyfat_category_essential)
        BodyFatCategory.ATHLETES -> stringResource(Res.string.bodyfat_category_athletes)
        BodyFatCategory.FITNESS -> stringResource(Res.string.bodyfat_category_fitness)
        BodyFatCategory.AVERAGE -> stringResource(Res.string.bodyfat_category_average)
        BodyFatCategory.OBESE -> stringResource(Res.string.bodyfat_category_obese)
    }
}

@Composable
private fun BodyFatCategoryReferenceTable(isMale: Boolean, currentCategory: BodyFatCategory) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = if (isMale) stringResource(Res.string.bodyfat_categories_male) else stringResource(Res.string.bodyfat_categories_female),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(4.dp))

        val categories = if (isMale) {
            listOf(
                Triple(BodyFatCategory.ESSENTIAL, stringResource(Res.string.bodyfat_category_essential), "2-5%"),
                Triple(BodyFatCategory.ATHLETES, stringResource(Res.string.bodyfat_category_athletes), "6-13%"),
                Triple(BodyFatCategory.FITNESS, stringResource(Res.string.bodyfat_category_fitness), "14-17%"),
                Triple(BodyFatCategory.AVERAGE, stringResource(Res.string.bodyfat_category_average), "18-24%"),
                Triple(BodyFatCategory.OBESE, stringResource(Res.string.bodyfat_category_obese), "25%+")
            )
        } else {
            listOf(
                Triple(BodyFatCategory.ESSENTIAL, stringResource(Res.string.bodyfat_category_essential), "10-13%"),
                Triple(BodyFatCategory.ATHLETES, stringResource(Res.string.bodyfat_category_athletes), "14-20%"),
                Triple(BodyFatCategory.FITNESS, stringResource(Res.string.bodyfat_category_fitness), "21-24%"),
                Triple(BodyFatCategory.AVERAGE, stringResource(Res.string.bodyfat_category_average), "25-31%"),
                Triple(BodyFatCategory.OBESE, stringResource(Res.string.bodyfat_category_obese), "32%+")
            )
        }

        categories.forEach { (cat, label, range) ->
            val isCurrentCategory = cat == currentCategory
            val textColor = if (isCurrentCategory) {
                getBodyFatCategoryColor(cat)
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            }
            val textWeight = if (isCurrentCategory) FontWeight.Bold else FontWeight.Normal

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(getBodyFatCategoryColor(cat))
                    )
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = textWeight,
                        color = textColor
                    )
                }
                Text(
                    text = range,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = textWeight,
                    color = textColor
                )
            }
        }
    }
}
