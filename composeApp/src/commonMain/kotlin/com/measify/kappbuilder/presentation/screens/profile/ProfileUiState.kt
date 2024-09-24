package com.measify.kappbuilder.presentation.screens.profile

import com.measify.kappbuilder.domain.model.User

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val signInActionRequired: Boolean = false,
    val deleteUserDialogShown: Boolean = false,
    val errorMessage: String? = null
)

sealed interface ProfileScreenUiEvent {
    data object OnClickLogOut : ProfileScreenUiEvent
    data object OnClickDeleteAccount : ProfileScreenUiEvent
}