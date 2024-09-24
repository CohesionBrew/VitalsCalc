package com.measify.kappmaker.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.btn_cancel
import com.measify.kappmaker.generated.resources.btn_delete
import com.measify.kappmaker.generated.resources.description_delete_user_dialog
import com.measify.kappmaker.generated.resources.title_delete_user_dialog
import org.jetbrains.compose.resources.stringResource


@Composable
fun DeleteUserConfirmationDialog(
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        icon = { Icon(imageVector = Icons.Filled.Delete, contentDescription = "Delete") },
        title = {
            Text(text = stringResource(Res.string.title_delete_user_dialog))
        },
        text = {
            Text(
                text = stringResource(Res.string.description_delete_user_dialog)
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(
                    text = stringResource(Res.string.btn_delete)
                )
            }

        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text(
                    text = stringResource(Res.string.btn_cancel)
                )
            }
        }
    )

}