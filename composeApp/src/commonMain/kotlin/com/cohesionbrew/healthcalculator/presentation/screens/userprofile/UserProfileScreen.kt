package com.cohesionbrew.healthcalculator.presentation.screens.userprofile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cohesionbrew.healthcalculator.designsystem.components.health.AdaptiveWheelSelector
import com.cohesionbrew.healthcalculator.designsystem.components.health.DobInputSection
import com.cohesionbrew.healthcalculator.designsystem.components.health.FormattedDoubleTextField
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthActionButton
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthGenderSelectorToggle
import com.cohesionbrew.healthcalculator.designsystem.components.health.HealthMenuPagesHeaderText
import com.cohesionbrew.healthcalculator.designsystem.components.health.OutlinedGroupBox
import com.cohesionbrew.healthcalculator.designsystem.components.health.formatDoubleDisplay
import com.cohesionbrew.healthcalculator.generated.resources.*
import org.jetbrains.compose.resources.stringResource

@Composable
fun UserProfileScreen(uiStateHolder: UserProfileUiStateHolder) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()
    UserProfileScreen(uiState = uiState, onUiEvent = uiStateHolder::onUiEvent)
}

@Composable
fun UserProfileScreen(
    uiState: UserProfileUiState,
    onUiEvent: (UserProfileUiEvent) -> Unit
) {
    val feetOptions = (3..7).map { it.toString() }
    val inchesOptions = (0..11).map { it.toString() }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(6.dp, MaterialTheme.colorScheme.outline),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HealthMenuPagesHeaderText(
                    text = stringResource(Res.string.title_screen_profile)
                )

                HorizontalDivider(thickness = 1.dp)

                // Gender section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = stringResource(Res.string.select_gender),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = stringResource(Res.string.used_for_metabolic_calculations),
                            style = MaterialTheme.typography.bodySmall,
                            fontStyle = FontStyle.Italic,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HealthGenderSelectorToggle(
                        maleLabel = stringResource(Res.string.male),
                        femaleLabel = stringResource(Res.string.female),
                        isMaleSelected = uiState.gender == "male",
                        onMaleSelected = { onUiEvent(UserProfileUiEvent.OnGenderChanged(true)) },
                        onFemaleSelected = { onUiEvent(UserProfileUiEvent.OnGenderChanged(false)) }
                    )

                    Text(
                        text = stringResource(Res.string.select_sex_helptext),
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                HorizontalDivider(thickness = 1.dp)

                // Date of Birth section
                DobInputSection(
                    dob = uiState.dob,
                    onDobChange = { onUiEvent(UserProfileUiEvent.OnDobChanged(it)) },
                    dobLabel = stringResource(Res.string.date_of_birth),
                    cancelLabel = stringResource(Res.string.cancel),
                    confirmLabel = stringResource(Res.string.ok)
                )

                HorizontalDivider(thickness = 1.dp)

                // Height section
                if (uiState.useMetric) {
                    FormattedDoubleTextField(
                        value = formatDoubleDisplay(uiState.heightCm),
                        onValueChange = { onUiEvent(UserProfileUiEvent.OnHeightCmChanged(it)) },
                        label = { Text(stringResource(Res.string.profile_height_metric)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    OutlinedGroupBox(
                        label = stringResource(Res.string.height),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            AdaptiveWheelSelector(
                                options = feetOptions,
                                selectedOption = uiState.heightFeet,
                                onOptionSelected = { onUiEvent(UserProfileUiEvent.OnHeightFeetChanged(it)) },
                                optionLabel = { it },
                                label = stringResource(Res.string.feet),
                                modifier = Modifier.weight(1f)
                            )
                            AdaptiveWheelSelector(
                                options = inchesOptions,
                                selectedOption = uiState.heightInches,
                                onOptionSelected = { onUiEvent(UserProfileUiEvent.OnHeightInchesChanged(it)) },
                                optionLabel = { it },
                                label = stringResource(Res.string.inches),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                HorizontalDivider(thickness = 1.dp)

                // Save button
                HealthActionButton(
                    text = stringResource(Res.string.save_and_close),
                    isLoading = uiState.isSaving,
                    onClick = { onUiEvent(UserProfileUiEvent.OnSave) }
                )
            }
        }
    }
}
