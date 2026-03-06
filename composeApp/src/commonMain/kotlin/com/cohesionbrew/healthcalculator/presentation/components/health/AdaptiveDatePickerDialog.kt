package com.cohesionbrew.healthcalculator.presentation.components.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cohesionbrew.healthcalculator.designsystem.util.isIos
import io.github.robinpcrd.cupertino.CupertinoDatePicker
import io.github.robinpcrd.cupertino.DatePickerStyle
import io.github.robinpcrd.cupertino.ExperimentalCupertinoApi
import io.github.robinpcrd.cupertino.rememberCupertinoDatePickerState
import io.github.robinpcrd.cupertino.theme.CupertinoTheme
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCupertinoApi::class)
@Composable
fun AdaptiveDatePickerDialog(
    visible: Boolean,
    initialDate: LocalDate,
    yearRange: IntRange = 1920..2100,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    cancelLabel: String = "Cancel",
    confirmLabel: String = "OK"
) {
    if (!visible) return

    if (isIos) {
        CupertinoDatePickerSheet(
            initialDate = initialDate,
            yearRange = yearRange,
            onDateSelected = onDateSelected,
            onDismiss = onDismiss,
            cancelLabel = cancelLabel,
            confirmLabel = confirmLabel
        )
    } else {
        MaterialDatePickerDialog(
            initialDate = initialDate,
            onDateSelected = onDateSelected,
            onDismiss = onDismiss,
            cancelLabel = cancelLabel,
            confirmLabel = confirmLabel
        )
    }
}

@OptIn(ExperimentalCupertinoApi::class)
@Composable
private fun CupertinoDatePickerSheet(
    initialDate: LocalDate,
    yearRange: IntRange,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    cancelLabel: String,
    confirmLabel: String
) {
    val initialMillis = initialDate
        .atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    val datePickerState = rememberCupertinoDatePickerState(
        initialSelectedDateMillis = initialMillis,
        yearRange = yearRange
    )

    var currentSelection by remember { mutableStateOf(initialDate) }

    LaunchedEffect(datePickerState.selectedDateMillis) {
        currentSelection = Instant.fromEpochMilliseconds(datePickerState.selectedDateMillis)
            .toLocalDateTime(TimeZone.UTC)
            .date
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        CupertinoTheme {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .padding(16.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = cancelLabel,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        TextButton(
                            onClick = { onDateSelected(currentSelection) }
                        ) {
                            Text(
                                text = confirmLabel,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    CupertinoDatePicker(
                        state = datePickerState,
                        style = DatePickerStyle.Wheel(),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaterialDatePickerDialog(
    initialDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    cancelLabel: String,
    confirmLabel: String
) {
    val initialMillis = initialDate
        .atStartOfDayIn(TimeZone.currentSystemDefault())
        .toEpochMilliseconds()

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.fromEpochMilliseconds(millis)
                            .toLocalDateTime(TimeZone.UTC)
                            .date
                        onDateSelected(selectedDate)
                    }
                }
            ) {
                Text(confirmLabel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(cancelLabel)
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}
