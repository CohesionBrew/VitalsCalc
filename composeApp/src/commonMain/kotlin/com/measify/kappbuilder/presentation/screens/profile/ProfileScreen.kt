package com.measify.kappbuilder.presentation.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.measify.kappbuilder.domain.model.User
import com.measify.kappbuilder.presentation.components.DeleteUserConfirmationDialog
import com.measify.kappbuilder.presentation.components.ErrorDialog
import com.measify.kappbuilder.presentation.components.FullScreenLoading
import com.measify.kappbuilder.generated.resources.Res
import com.measify.kappbuilder.generated.resources.btn_delete_account
import com.measify.kappbuilder.generated.resources.btn_logout
import org.jetbrains.compose.resources.stringResource

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    uiStateHolder: ProfileUiStateHolder,
    onSignInRequired: () -> Unit
) {
    val uiState by uiStateHolder.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.signInActionRequired) {
        if (uiState.signInActionRequired)
            onSignInRequired()
    }
    if (uiState.deleteUserDialogShown) {
        DeleteUserConfirmationDialog(
            onConfirm = uiStateHolder::onConfirmDeleteAccount,
            onDismiss = uiStateHolder::onDismissDeleteUserConfirmationDialog
        )
    }
    ErrorDialog(text = uiState.errorMessage, onClickOk = { uiStateHolder.onErrorMessageShown() })
    if (uiState.isLoading) {
        FullScreenLoading()
    } else {
        val currentUser = uiState.user
        currentUser?.let {
            ProfileScreen(
                modifier = modifier,
                currentUser = it,
                onUiEvent = uiStateHolder::onUiEvent
            )
        }
    }
}

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    currentUser: User,
    onUiEvent: (ProfileScreenUiEvent) -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Email Address
        Text(
            text = currentUser.email ?: "",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        // User action buttons
        Spacer(modifier = Modifier.height(16.dp))
        UserActionButtonsContainer(onUiEvent = onUiEvent)
    }


}

@Composable
private fun UserActionButtonsContainer(onUiEvent: (ProfileScreenUiEvent) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        UserActionButton(
            text = stringResource(Res.string.btn_logout),
            icon = Icons.AutoMirrored.Filled.ExitToApp,
            onClick = {
                onUiEvent(ProfileScreenUiEvent.OnClickLogOut)
            }
        )

        UserActionButton(
            text = stringResource(Res.string.btn_delete_account),
            icon = Icons.Default.Delete,
            textColor = MaterialTheme.colorScheme.error,
            onClick = {
                onUiEvent(ProfileScreenUiEvent.OnClickDeleteAccount)
            }
        )

    }
}

@Composable
private fun UserActionButton(
    text: String,
    icon: ImageVector,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            modifier = Modifier.clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondaryContainer).padding(8.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            contentDescription = null,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = textColor,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            tint = MaterialTheme.colorScheme.onSecondaryContainer,
            contentDescription = null,
        )
    }
}
