package com.cohesionbrew.healthcalculator.presentation.components.health

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.time.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearsUntil

@Composable
fun DobInputSection(
    dob: LocalDate,
    onDobChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    dobLabel: String = "Date of Birth",
    ageLabel: String = "Age: %d years",
    pickDateDescription: String = "Pick date"
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val formattedDob = remember(dob) {
        "${dob.month.number.toString().padStart(2, '0')}-${dob.day.toString().padStart(2, '0')}-${dob.year}"
    }

    val age = remember(dob) {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        dob.yearsUntil(now)
    }

    val currentYear = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Box {
            OutlinedTextField(
                value = formattedDob,
                onValueChange = {},
                readOnly = true,
                label = { Text(dobLabel) },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = pickDateDescription
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = { showDatePicker = true })
            )
        }

        Text(
            text = ageLabel.replace("%d", age.toString()),
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.End)
        )
    }

    AdaptiveDatePickerDialog(
        visible = showDatePicker,
        initialDate = dob,
        yearRange = 1920..currentYear,
        onDateSelected = { selectedDate ->
            onDobChange(selectedDate)
            showDatePicker = false
        },
        onDismiss = { showDatePicker = false }
    )
}
