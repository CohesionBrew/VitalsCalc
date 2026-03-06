package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun HealthGenderSelectorToggle(
    modifier: Modifier = Modifier,
    maleLabel: String,
    femaleLabel: String,
    isMaleSelected: Boolean,
    onMaleSelected: () -> Unit,
    onFemaleSelected: () -> Unit
) {
    Row(
        modifier = modifier
            .width(intrinsicSize = IntrinsicSize.Min)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val maleColors = if (isMaleSelected) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.outlinedButtonColors()
        }

        Button(
            onClick = onMaleSelected,
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = if (isMaleSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(40.dp)
                )
                .selectable(
                    selected = isMaleSelected,
                    role = Role.RadioButton,
                    onClick = onMaleSelected
                ),
            colors = maleColors,
        ) {
            Text(maleLabel)
        }

        val isFemaleSelected = !isMaleSelected
        val femaleColors = if (isFemaleSelected) {
            ButtonDefaults.buttonColors()
        } else {
            ButtonDefaults.outlinedButtonColors()
        }

        Button(
            onClick = onFemaleSelected,
            modifier = Modifier
                .weight(1f)
                .border(
                    width = 1.dp,
                    color = if (isFemaleSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(40.dp)
                )
                .selectable(
                    selected = isFemaleSelected,
                    role = Role.RadioButton,
                    onClick = onFemaleSelected
                ),
            colors = femaleColors,
        ) {
            Text(femaleLabel)
        }
    }
}
