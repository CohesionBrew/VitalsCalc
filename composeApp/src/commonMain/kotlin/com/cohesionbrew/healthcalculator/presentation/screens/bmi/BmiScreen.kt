package com.cohesionbrew.healthcalculator.presentation.screens.bmi

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthOutlinedText
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthScreenTitle
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.generated.resources.*
import com.cohesionbrew.healthcalculator.presentation.components.health.getBmiCategoryColor
import com.cohesionbrew.healthcalculator.presentation.screens.bmi.components.BmiDynamicGraphicsChart
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt
import org.jetbrains.compose.resources.stringResource

@Composable
fun BmiScreen(uiStateHolder: BmiUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    BmiScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun BmiScreen(
    uiState: BmiUiState,
    onUiEvent: (BmiUiEvent) -> Unit
) {
    val title = stringResource(Res.string.bmi_calculator)

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isLandscape = maxWidth > maxHeight

        if (isLandscape) {
            LandscapeLayout(
                title = title,
                uiState = uiState,
                onUiEvent = onUiEvent
            )
        } else {
            PortraitLayout(
                title = title,
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
    uiState: BmiUiState,
    onUiEvent: (BmiUiEvent) -> Unit
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
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HealthScreenTitle(text = title)

            // Profile info subtitle (matches old app: "Age 100, H = 5 ft 11 in")
            if (uiState.age != null && uiState.heightDisplayText.isNotEmpty()) {
                Text(
                    text = stringResource(Res.string.bmi_profile_subtitle, uiState.age.toString(), uiState.heightDisplayText),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            BmiEntryCard(
                uiState = uiState,
                onUiEvent = onUiEvent,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            BmiDataCard(
                uiState = uiState,
                modifier = Modifier
            )
            Spacer(modifier = Modifier.height(8.dp))
            BmiDynamicGraphicsChart(currentBmi = uiState.bmi)
        }
        // Ad banner slot would go here if needed
    }
}

// --- Landscape Layout (matches old StandardHealthScaffold LandscapeLayout) ---

@Composable
private fun LandscapeLayout(
    title: String,
    uiState: BmiUiState,
    onUiEvent: (BmiUiEvent) -> Unit
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
            // Left Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HealthScreenTitle(text = title)

                // Profile info subtitle (matches old app: "Age 100, H = 5 ft 11 in")
                if (uiState.age != null && uiState.heightDisplayText.isNotEmpty()) {
                    Text(
                        text = stringResource(Res.string.bmi_profile_subtitle, uiState.age.toString(), uiState.heightDisplayText),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                BmiEntryCard(
                    uiState = uiState,
                    onUiEvent = onUiEvent,
                    modifier = Modifier.fillMaxWidth()
                )
                BmiDynamicGraphicsChart(currentBmi = uiState.bmi)
            }

            // Right Column
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BmiDataCard(
                    uiState = uiState,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        // Ad banner slot would go here if needed
    }
}

// --- BmiEntryCard (matches old app's BmiEntryCard) ---

@Composable
private fun BmiEntryCard(
    uiState: BmiUiState,
    onUiEvent: (BmiUiEvent) -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    var latestWeightValue by remember { mutableStateOf<Double?>(null) }
    var isFocused by remember { mutableStateOf(false) }

    val commitWeightChange = {
        latestWeightValue?.let { newWeight ->
            val currentWeight = uiState.weightKg
            if (newWeight != currentWeight) {
                onUiEvent(BmiUiEvent.OnWeightChanged(newWeight))
                onUiEvent(BmiUiEvent.OnCalculate)
            }
        }
    }

    val weightDisplay = formatDoubleDisplay(uiState.weightKg)
    val unitLabel = if (uiState.useMetric) {
        stringResource(Res.string.unit_kg)
    } else {
        stringResource(Res.string.unit_lbs)
    }

    Column(
        modifier = modifier
            .background(containerColor)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            FormattedDoubleTextField(
                value = weightDisplay,
                onValueChange = { newValue ->
                    if (newValue != null) {
                        latestWeightValue = newValue
                    }
                },
                modifier = Modifier
                    .width(120.dp)
                    .height(60.dp)
                    .onFocusChanged { focusState ->
                        if (isFocused && !focusState.isFocused) {
                            commitWeightChange()
                        }
                        isFocused = focusState.isFocused
                    },
                label = {
                    Text(
                        text = stringResource(Res.string.weight),
                        color = contentColor
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { commitWeightChange() }
                ),
                textStyle = TextStyle(
                    color = contentColor,
                    fontSize = 15.sp
                ),
                suffix = {
                    Row {
                        Text(text = unitLabel)
                    }
                },
                allowNegative = false
            )
        }

        HealthActionButton(
            text = stringResource(Res.string.calculate_bmi),
            isLoading = uiState.isLoading,
            onClick = {
                latestWeightValue?.let { onUiEvent(BmiUiEvent.OnWeightChanged(it)) }
                onUiEvent(BmiUiEvent.OnCalculate)
                focusManager.clearFocus()
            }
        )
    }
}

// --- BmiDataCard (matches old app's BmiDataCard) ---

@Composable
private fun BmiDataCard(
    uiState: BmiUiState,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    val unknownCategory = stringResource(Res.string.unknown_category)
    val categoryName = BmiUiState.categoryNames.getOrElse(uiState.categoryIndex) { unknownCategory }
    val categoryColor = getBmiCategoryColor(uiState.categoryIndex)
    val useMetric = uiState.useMetric
    val unit = if (useMetric) stringResource(Res.string.unit_kg) else stringResource(Res.string.unit_lbs)

    // Accessibility description
    val formattedBmi = ((uiState.bmi * 10).roundToInt() / 10.0).toString()
    val accessibilityDescription = "Your BMI is $formattedBmi, categorized as $categoryName"

    Column(
        modifier = modifier
            .background(containerColor)
            .padding(12.dp)
            .semantics(mergeDescendants = true) {
                contentDescription = accessibilityDescription
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top section: BMI value + Weight Status row
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.widthIn(max = 300.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Left column: Your BMI
                Column {
                    Text(
                        text = stringResource(Res.string.your_bmi),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = contentColor
                    )
                    Text(
                        text = formattedBmi,
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color = contentColor
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Right column: Weight Status + category + difference
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.weight_status),
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        color = contentColor
                    )

                    HealthOutlinedText(
                        modifier = Modifier.fillMaxWidth(),
                        text = categoryName,
                        color = categoryColor,
                        outlineColor = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.End
                    )

                    // Weight difference text
                    val diff = uiState.differenceFromHealthy
                    val differenceString = if (diff > 0) {
                        val formattedDiff = abs(diff).roundToInt()
                        stringResource(Res.string.bmi_diff_to_lose, formattedDiff.toString(), unit)
                    } else if (diff < 0) {
                        val formattedDiff = abs(diff).roundToInt()
                        stringResource(Res.string.bmi_diff_to_gain, formattedDiff.toString(), unit)
                    } else {
                        stringResource(Res.string.at_or_below_healthy_weight)
                    }

                    Text(
                        text = differenceString,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = contentColor,
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        // Bottom section: Healthy weight range
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.healthy_weight_range),
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                color = contentColor,
                textAlign = TextAlign.Center
            )

            val minDisplay = ceil(uiState.healthyMinKg).toInt()
            val maxDisplay = floor(uiState.healthyMaxKg).toInt()
            Text(
                text = "$minDisplay $unit - $maxDisplay $unit",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = contentColor,
                textAlign = TextAlign.Center
            )
        }
    }
}
