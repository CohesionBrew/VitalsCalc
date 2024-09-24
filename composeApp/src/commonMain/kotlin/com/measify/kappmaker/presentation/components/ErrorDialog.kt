package com.measify.kappmaker.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.btn_ok
import com.measify.kappmaker.generated.resources.title_error_dialog
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorDialog(modifier: Modifier = Modifier, text: String?, onClickOk: () -> Unit) {
    if (text == null) return
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        titleContentColor = MaterialTheme.colorScheme.error,
        iconContentColor = MaterialTheme.colorScheme.error,
        textContentColor = MaterialTheme.colorScheme.onErrorContainer,
        modifier = modifier,
        onDismissRequest = { },
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error"
            )
        },
        title = { Text(text = stringResource(Res.string.title_error_dialog)) },
        text = { Text(text = text) },
        confirmButton = {
            Button(
                onClick = { onClickOk() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(text = stringResource(Res.string.btn_ok))
            }
        },
    )
}