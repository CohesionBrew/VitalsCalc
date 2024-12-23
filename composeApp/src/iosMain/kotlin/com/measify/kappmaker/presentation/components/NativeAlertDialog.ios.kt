/**
 * Credits to MohamedRejeb for this ios implementation side in ios native dialog:
 *
 * https://github.com/MohamedRejeb/Calf/blob/main/calf-ui/
 */

package com.measify.kappmaker.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.interop.LocalUIViewController
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCAction
import platform.UIKit.*
import platform.objc.sel_registerName


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

    val currentUIViewController = LocalUIViewController.current
    val iosNativeDialogManager = remember(currentUIViewController) {
        IosNativeDialogManager(
            parentUIViewController = currentUIViewController,
            title = title,
            text = text,
            btnConfirmText = btnConfirmText,
            btnDismissText = btnDismissText,
            dismissOnClickOutside = dismissOnClickOutside,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
        )
    }

    LaunchedEffect(
        onConfirm,
        onDismiss,
        btnConfirmText,
        btnDismissText,
        title,
        text,
        dismissOnClickOutside
    ) {
        iosNativeDialogManager.onConfirm = onConfirm
        iosNativeDialogManager.onDismiss = onDismiss
        iosNativeDialogManager.btnConfirmText = btnConfirmText
        iosNativeDialogManager.btnDismissText = btnDismissText
        iosNativeDialogManager.title = title
        iosNativeDialogManager.text = text
        iosNativeDialogManager.dismissOnClickOutside = dismissOnClickOutside
    }

    DisposableEffect(Unit) {
        iosNativeDialogManager.showAlertDialog()
        onDispose {
            iosNativeDialogManager.dismiss()
        }
    }

}

/**
 * This class can't be private. Because when making it private, then dismissOnClickOutside will not work
 */
class IosNativeDialogManager(
    private val parentUIViewController: UIViewController,
    var onConfirm: () -> Unit,
    var onDismiss: () -> Unit,
    var btnConfirmText: String,
    var btnDismissText: String,
    var title: String,
    var text: String,
    var dismissOnClickOutside: Boolean
) {

    /**
     * Indicates whether the dialog is presented or not.
     */
    private var isPresented = false

    /**
     * Indicates whether the dialog is animating or not.
     */
    private var isAnimating = false

    /**
     * The ui view controller that is used to present the dialog.
     */
    private val dialogUIViewController: UIViewController by lazy {
        UIAlertController.alertControllerWithTitle(
            title = title,
            message = text,
            preferredStyle = UIAlertControllerStyleAlert
        ).apply {
            val confirmAction = UIAlertAction.actionWithTitle(
                title = btnConfirmText,
                style = UIAlertActionStyleDefault,
                handler = { onConfirm() }
            )
            addAction(confirmAction)

            if (btnDismissText.isEmpty().not()) {
                val cancelAction = UIAlertAction.actionWithTitle(
                    title = btnDismissText,
                    style = UIAlertActionStyleDestructive,
                    handler = { onDismiss() }
                )
                addAction(cancelAction)
            }
        }
    }

    /**
     * Lambda that dismisses the dialog.
     */
    private val onDismissLambda: (() -> Unit) = {
        dialogUIViewController.dismissViewControllerAnimated(
            flag = true,
            completion = {
                isPresented = false
                isAnimating = false
                onDismiss()
            }
        )
    }

    /**
     * Pointer to the [dismiss] method.
     */
    @OptIn(ExperimentalForeignApi::class)
    private val dismissPointer = sel_registerName("dismiss")

    /**
     * Dismisses the dialog.
     */
    @OptIn(BetaInteropApi::class)
    @ObjCAction
    fun dismiss() {
        if (!isPresented || isAnimating) return
        isAnimating = true
        onDismissLambda.invoke()
    }

    /**
     * Shows the dialog.
     */
    @OptIn(ExperimentalForeignApi::class, ExperimentalForeignApi::class)
    fun showAlertDialog() {
        if (isPresented || isAnimating) return
        isAnimating = true

        parentUIViewController.presentViewController(
            viewControllerToPresent = dialogUIViewController,
            animated = true,
            completion = {
                isPresented = true
                isAnimating = false

                if (dismissOnClickOutside) {
                    dialogUIViewController.view.superview?.setUserInteractionEnabled(true)
                    dialogUIViewController.view.superview?.addGestureRecognizer(
                        UITapGestureRecognizer(
                            target = this,
                            action = dismissPointer
                        )
                    )
                }
            }
        )
    }
}