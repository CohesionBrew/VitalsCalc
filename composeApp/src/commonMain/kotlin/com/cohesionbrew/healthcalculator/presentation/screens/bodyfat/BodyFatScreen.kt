package com.cohesionbrew.healthcalculator.presentation.screens.bodyfat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.ScreenWithToolbar
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthDropdownSelector
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthGenderSelectorToggle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.domain.model.BodyFatCategory
import com.cohesionbrew.healthcalculator.domain.model.BodyFatMethod
import com.cohesionbrew.healthcalculator.generated.resources.*
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun BodyFatScreen(uiStateHolder: BodyFatUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BodyFatScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BodyFatScreen(uiState: BodyFatUiState, onUiEvent: (BodyFatUiEvent) -> Unit) {
    ScreenWithToolbar(title = stringResource(Res.string.title_screen_body_fat), isScrollableContent = true, includeBottomInsets = false) {
        Column(modifier = Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            HealthScreenTitle(text = stringResource(Res.string.bodyfat_title))
            val navyMethodLabel = stringResource(Res.string.bodyfat_method_navy)
            val rfmMethodLabel = stringResource(Res.string.bodyfat_method_rfm)
            HealthDropdownSelector(
                caption = stringResource(Res.string.label_method),
                options = listOf(navyMethodLabel, rfmMethodLabel),
                selected = if (uiState.method == BodyFatMethod.NAVY) navyMethodLabel else rfmMethodLabel,
                onSelect = { onUiEvent(BodyFatUiEvent.OnMethodChanged(if (it == navyMethodLabel) BodyFatMethod.NAVY else BodyFatMethod.RFM)) }
            )
            HealthGenderSelectorToggle(
                maleLabel = stringResource(Res.string.male),
                femaleLabel = stringResource(Res.string.female),
                isMaleSelected = uiState.gender == "male",
                onMaleSelected = { onUiEvent(BodyFatUiEvent.OnGenderChanged("male")) },
                onFemaleSelected = { onUiEvent(BodyFatUiEvent.OnGenderChanged("female")) }
            )
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.heightCm),
                onValueChange = { onUiEvent(BodyFatUiEvent.OnHeightChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.height)) }, suffix = { Text(stringResource(Res.string.unit_cm)) },
                modifier = Modifier.fillMaxWidth()
            )
            FormattedDoubleTextField(
                value = formatDoubleDisplay(uiState.waistCm),
                onValueChange = { onUiEvent(BodyFatUiEvent.OnWaistChanged(it ?: 0.0)) },
                label = { Text(stringResource(Res.string.bodyfat_waist)) }, suffix = { Text(stringResource(Res.string.unit_cm)) },
                modifier = Modifier.fillMaxWidth()
            )
            if (uiState.method == BodyFatMethod.NAVY) {
                FormattedDoubleTextField(
                    value = formatDoubleDisplay(uiState.neckCm),
                    onValueChange = { onUiEvent(BodyFatUiEvent.OnNeckChanged(it ?: 0.0)) },
                    label = { Text(stringResource(Res.string.bodyfat_neck)) }, suffix = { Text(stringResource(Res.string.unit_cm)) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.gender == "female") {
                    FormattedDoubleTextField(
                        value = formatDoubleDisplay(uiState.hipCm),
                        onValueChange = { onUiEvent(BodyFatUiEvent.OnHipChanged(it ?: 0.0)) },
                        label = { Text(stringResource(Res.string.bodyfat_hip)) }, suffix = { Text(stringResource(Res.string.unit_cm)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            HealthActionButton(text = stringResource(Res.string.calculate), isLoading = uiState.isLoading, onClick = { onUiEvent(BodyFatUiEvent.OnCalculate) })
            if (uiState.isCalculated) {
                Spacer(modifier = Modifier.height(8.dp))
                BodyFatResultCard(uiState)
            }
        }
    }
}

/**
 * Returns the color for a body fat category.
 */
private fun getBodyFatCategoryColor(category: BodyFatCategory): Color {
    return when (category) {
        BodyFatCategory.ESSENTIAL -> Color(0xFF2196F3)  // Blue
        BodyFatCategory.ATHLETES -> Color(0xFF4CAF50)   // Green
        BodyFatCategory.FITNESS -> Color(0xFF4CAF50)    // Green
        BodyFatCategory.AVERAGE -> Color(0xFFFF9800)    // Orange
        BodyFatCategory.OBESE -> Color(0xFFF44336)      // Red
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
private fun BodyFatResultCard(uiState: BodyFatUiState) {
    val category = uiState.category ?: return
    val categoryLabel = getBodyFatCategoryLabel(category)
    val categoryColor = getBodyFatCategoryColor(category)
    val isMale = uiState.gender == "male"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
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
