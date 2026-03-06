package com.cohesionbrew.healthcalculator.presentation.screens.profile

import com.cohesionbrew.healthcalculator.auth.api.AuthRecentLoginRequiredException
import com.cohesionbrew.healthcalculator.data.repository.UserRepository
import com.cohesionbrew.healthcalculator.domain.exceptions.UnAuthorizedException
import com.cohesionbrew.healthcalculator.util.UiStateHolder
import com.cohesionbrew.healthcalculator.util.logging.AppLogger
import com.cohesionbrew.healthcalculator.util.uiStateHolderScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileUiStateHolder(private val userRepository: UserRepository) : UiStateHolder() {


    private val currentUserFlow = userRepository.currentUser
        .onEach { result ->
            result.onSuccess { user ->
                _uiState.update { it.copy(isLoading = false, user = user) }
            }.onFailure { error ->
                if (error is UnAuthorizedException) {
                    _uiState.update { it.copy(isLoading = false, user = null) }
                } else {
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
                }
            }
        }

    private val _uiState = MutableStateFlow(ProfileUiState(isLoading = true))
    val uiState: StateFlow<ProfileUiState> =
        combine(_uiState, currentUserFlow) { updatedUiState, _ -> updatedUiState }
            .stateIn(
                uiStateHolderScope,
                SharingStarted.Lazily,
                ProfileUiState(isLoading = true)
            )


    fun onErrorMessageShown() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onDismissDeleteUserConfirmationDialog() = uiStateHolderScope.launch {
        _uiState.update { it.copy(deleteUserDialogShown = false) }
    }

    fun onConfirmDeleteAccount() = uiStateHolderScope.launch {
        _uiState.update { it.copy(deleteUserDialogShown = false, isLoading = true) }
        userRepository.deleteAccount()
            .onSuccess {
                AppLogger.d("Account is deleted successfully")
                _uiState.update { it.copy(isLoading = false, user = null) }
            }
            .onFailure { error ->
                AppLogger.d("Account deletion failed ${error.message}")
                if (error is AuthRecentLoginRequiredException) {
                    _uiState.update { it.copy(isLoading = false, user = null) }
                } else
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
    }

    fun onUiEvent(event: ProfileScreenUiEvent) = uiStateHolderScope.launch {
        when (event) {
            ProfileScreenUiEvent.OnClickDeleteAccount -> {
                _uiState.update { it.copy(deleteUserDialogShown = true) }
            }
        }
    }

}