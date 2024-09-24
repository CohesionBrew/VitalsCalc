package com.measify.kappbuilder.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.measify.kappbuilder.generated.resources.Res
import com.measify.kappbuilder.generated.resources.btn_ok
import com.measify.kappbuilder.generated.resources.title_info
import org.jetbrains.compose.resources.stringResource


@Composable
fun MessageDialog(
    title: String = stringResource(Res.string.title_info),
    text: String?,
    modifier: Modifier = Modifier,
    onConfirm: () -> Unit,
) {
    if (text.isNullOrEmpty()) return
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { },
        icon = { Icon(imageVector = Icons.Filled.Info, contentDescription = "Info") },
        title = {
            Text(text = title)
        },
        text = {
            Text(
                text = text
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text(
                    text = stringResource(Res.string.btn_ok)
                )
            }

        }
    )

}