package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType

fun formatDoubleDisplay(value: Double?): String =
    when {
        value == null -> ""
        value % 1.0 == 0.0 -> value.toInt().toString()
        else -> value.toString()
    }

@Composable
fun FormattedDoubleTextField(
    value: String?,
    onValueChange: (Double?) -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    textStyle: TextStyle = TextStyle.Default,
    suffix: @Composable (() -> Unit)? = null,
    allowNegative: Boolean = false,
) {
    var text by remember(value) {
        mutableStateOf(value ?: "")
    }

    val effectiveIme = if (keyboardOptions.imeAction == ImeAction.Default) ImeAction.Done else keyboardOptions.imeAction
    val focus = LocalFocusManager.current

    val effectiveActions = if (keyboardActions == KeyboardActions.Default) {
        KeyboardActions(onDone = { focus.clearFocus() })
    } else {
        keyboardActions
    }

    fun isValid(newText: String): Boolean {
        val pattern = if (allowNegative) "^-?\\d*\\.?\\d*$" else "^\\d*\\.?\\d*$"
        return newText.isEmpty() || newText.matches(Regex(pattern))
    }

    OutlinedTextField(
        value = text,
        onValueChange = { newText ->
            if (isValid(newText)) {
                text = newText
                val parsed = when {
                    newText.isEmpty() -> null
                    newText.endsWith(".") -> newText.dropLast(1).toDoubleOrNull()
                    else -> newText.toDoubleOrNull()
                }
                onValueChange(parsed)
            }
        },
        modifier = modifier,
        label = label,
        keyboardOptions = keyboardOptions.copy(imeAction = effectiveIme),
        keyboardActions = effectiveActions,
        singleLine = singleLine,
        textStyle = textStyle,
        suffix = suffix,
    )
}
