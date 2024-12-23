package com.measify.kappmaker.presentation.components

import androidx.compose.runtime.Composable
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.btn_cancel
import com.measify.kappmaker.generated.resources.btn_ok
import org.jetbrains.compose.resources.stringResource


/**
 * Shows Native dialog. In Android AlertDialog, in iOS UIAlerts.
 *
 * @param title The title of the dialog.
 * @param text The text of the dialog.
 * @param btnConfirmText The text of the confirm button.
 * @param btnDismissText The text of the dismiss button.
 * @param dismissOnClickOutside Indicates should dismiss dialog when clicking outside.
 * @param onConfirm Lambda that is invoked when the confirm button is clicked.
 * @param onDismiss Lambda that is invoked when the dismiss button is clicked.
 */
@Composable
expect fun NativeAlertDialog(
    title: String,
    text: String,
    btnConfirmText: String = stringResource(Res.string.btn_ok),
    btnDismissText: String = stringResource(Res.string.btn_cancel),
    dismissOnClickOutside: Boolean = true,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit = {},
)