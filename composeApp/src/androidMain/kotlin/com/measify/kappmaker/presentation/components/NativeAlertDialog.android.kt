package com.measify.kappmaker.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
actual fun NativeAlertDialog(
    title: String,
    text: String,
    btnConfirmText: String,
    btnDismissText: String,
    dismissOnClickOutside: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    var isPresented by remember { mutableStateOf(true) }
    if (!isPresented) return
    AlertDialog(
        title = { Text(title) },
        text = { Text(text) },
        onDismissRequest = {
            if (dismissOnClickOutside) {
                onDismiss()
                isPresented = false
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm()
                isPresented = false
            }) {
                Text(btnConfirmText)
            }
        },
        dismissButton = {
            if (btnDismissText.isEmpty().not()) {
                TextButton(onClick = {
                    onDismiss()
                    isPresented = false
                }) {
                    Text(btnDismissText)
                }
            }
        },
    )
}