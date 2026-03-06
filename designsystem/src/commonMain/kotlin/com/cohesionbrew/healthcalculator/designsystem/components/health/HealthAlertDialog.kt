package com.cohesionbrew.healthcalculator.designsystem.components.health

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cohesionbrew.healthcalculator.designsystem.util.isIos
import io.github.robinpcrd.cupertino.theme.CupertinoTheme

@Composable
fun HealthAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissText: String? = null,
    isDestructive: Boolean = false
) {
    if (isIos) {
        CupertinoAlertDialog(
            title = title,
            message = message,
            confirmText = confirmText,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            dismissText = dismissText,
            isDestructive = isDestructive
        )
    } else {
        MaterialAlertDialog(
            title = title,
            message = message,
            confirmText = confirmText,
            onConfirm = onConfirm,
            onDismiss = onDismiss,
            dismissText = dismissText,
            isDestructive = isDestructive
        )
    }
}

@Composable
private fun CupertinoAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissText: String?,
    isDestructive: Boolean
) {
    val destructiveColor = Color(0xFFFF3B30)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        CupertinoTheme {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }

                    CupertinoAlertDivider()

                    if (dismissText != null) {
                        CupertinoAlertButton(
                            text = dismissText,
                            onClick = onDismiss,
                            isBold = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                        CupertinoAlertDivider()
                        CupertinoAlertButton(
                            text = confirmText,
                            onClick = onConfirm,
                            isBold = !isDestructive,
                            textColor = if (isDestructive) destructiveColor else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        CupertinoAlertButton(
                            text = confirmText,
                            onClick = onConfirm,
                            isBold = true,
                            textColor = if (isDestructive) destructiveColor else null,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CupertinoAlertButton(
    text: String,
    onClick: () -> Unit,
    isBold: Boolean,
    modifier: Modifier = Modifier,
    textColor: Color? = null
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = if (isBold) FontWeight.SemiBold else FontWeight.Normal
            ),
            color = textColor ?: MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun CupertinoAlertDivider() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.5.dp),
        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    ) {}
}

@Composable
private fun MaterialAlertDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissText: String?,
    isDestructive: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            if (isDestructive) {
                Button(
                    onClick = onConfirm,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(text = confirmText)
                }
            } else {
                TextButton(onClick = onConfirm) {
                    Text(text = confirmText)
                }
            }
        },
        dismissButton = dismissText?.let {
            {
                TextButton(onClick = onDismiss) {
                    Text(text = it)
                }
            }
        }
    )
}

enum class AlertButtonStyle {
    Default,
    Cancel,
    Destructive
}

data class AlertButton(
    val text: String,
    val onClick: () -> Unit,
    val style: AlertButtonStyle = AlertButtonStyle.Default
)

@Composable
fun HealthAlertDialogWithButtons(
    title: String,
    message: String,
    buttons: List<AlertButton>,
    onDismiss: () -> Unit
) {
    if (isIos) {
        CupertinoAlertDialogWithButtons(
            title = title,
            message = message,
            buttons = buttons,
            onDismiss = onDismiss
        )
    } else {
        MaterialAlertDialogWithButtons(
            title = title,
            message = message,
            buttons = buttons,
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun CupertinoAlertDialogWithButtons(
    title: String,
    message: String,
    buttons: List<AlertButton>,
    onDismiss: () -> Unit
) {
    val destructiveColor = Color(0xFFFF3B30)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        CupertinoTheme {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
                tonalElevation = 6.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = message,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }

                    CupertinoAlertDivider()

                    buttons.forEachIndexed { index, button ->
                        CupertinoAlertButton(
                            text = button.text,
                            onClick = button.onClick,
                            isBold = button.style == AlertButtonStyle.Cancel || buttons.size == 1,
                            textColor = when (button.style) {
                                AlertButtonStyle.Destructive -> destructiveColor
                                else -> null
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (index < buttons.lastIndex) {
                            CupertinoAlertDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MaterialAlertDialogWithButtons(
    title: String,
    message: String,
    buttons: List<AlertButton>,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                buttons.forEach { button ->
                    when (button.style) {
                        AlertButtonStyle.Destructive -> {
                            Button(
                                onClick = button.onClick,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                Text(text = button.text)
                            }
                        }
                        else -> {
                            TextButton(onClick = button.onClick) {
                                Text(text = button.text)
                            }
                        }
                    }
                }
            }
        }
    )
}
