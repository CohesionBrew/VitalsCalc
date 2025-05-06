package com.measify.kappmaker.presentation.components.modals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import com.measify.kappmaker.generated.resources.Res
import com.measify.kappmaker.generated.resources.btn_cancel
import com.measify.kappmaker.generated.resources.btn_delete
import com.measify.kappmaker.generated.resources.description_delete_user_dialog
import com.measify.kappmaker.generated.resources.ic_delete
import com.measify.kappmaker.generated.resources.subtitle_delete_user_dialog
import com.measify.kappmaker.generated.resources.title_delete_user_dialog
import com.measify.kappmaker.presentation.components.AppButton
import com.measify.kappmaker.presentation.components.PreviewHelper
import com.measify.kappmaker.presentation.theme.AppTheme
import org.jetbrains.compose.resources.stringResource

enum class DeleteUserConfirmationStyle {
    DIALOG,
    MODALBOTTOMSHEET,
}

@Composable
fun DeleteUserConfirmation(
    style: DeleteUserConfirmationStyle = DeleteUserConfirmationStyle.MODALBOTTOMSHEET,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    when (style) {
        DeleteUserConfirmationStyle.DIALOG -> {
            DeleteUserConfirmationDialog(onConfirm = onConfirm, onDismiss = onDismiss)
        }

        DeleteUserConfirmationStyle.MODALBOTTOMSHEET -> {
            DeleteUserConfirmationModal(onConfirm = onConfirm, onDismiss = onDismiss)
        }
    }
}

@Composable
private fun DeleteUserConfirmationModal(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {

    //For Delete, we reverse buttons
    AppModalBottomSheet(
        title = stringResource(Res.string.title_delete_user_dialog),
        titleColor = AppTheme.colors.status.error,
        btnDismissText = stringResource(Res.string.btn_cancel),
        btnConfirmText = stringResource(Res.string.btn_delete),
        onConfirm = onConfirm,
        onDismiss = onDismiss,
        reverseButtonsOrder = true
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.groupedVerticalElementSpacing)
        ) {
            Text(
                text = stringResource(Res.string.subtitle_delete_user_dialog),
                style = AppTheme.typography.h5,
                color = AppTheme.colors.text.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = stringResource(Res.string.description_delete_user_dialog),
                style = AppTheme.typography.bodyLarge,
                color = AppTheme.colors.text.secondary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DeleteUserConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    val dialogType = DialogType.ERROR
    AppDialog(
        type = dialogType,
        title = stringResource(Res.string.title_delete_user_dialog),
        text = stringResource(Res.string.description_delete_user_dialog),
        btnDismissText = stringResource(Res.string.btn_cancel),
        btnConfirmText = stringResource(Res.string.btn_delete),
        image = {
            AppDialogImage(dialogType = dialogType, icon = Res.drawable.ic_delete)
        },
        onConfirm = onConfirm,
        onDismiss = onDismiss
    )
}

@Composable
fun DeleteUserConfirmationPreview() {
    PreviewHelper {
        var style by remember {
            mutableStateOf<DeleteUserConfirmationStyle?>(null)
        }
        AppButton("Show Delete User Confirmation(Modal)") {
            style = DeleteUserConfirmationStyle.MODALBOTTOMSHEET
        }
        AppButton("Show Delete User Confirmation(Dialog)") {
            style = DeleteUserConfirmationStyle.DIALOG
        }

        style?.let {
            DeleteUserConfirmation(
                style = it,
                onConfirm = {
                    style = null
                },
                onDismiss = {
                    style = null
                }
            )
        }
    }
}