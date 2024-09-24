package com.measify.kappbuilder.presentation.screens.profile

import com.measify.kappbuilder.data.repository.UserRepository
import com.measify.kappbuilder.domain.exceptions.UnAuthorizedException
import com.measify.kappbuilder.util.UiStateHolder
import com.measify.kappbuilder.util.logging.AppLogger
import com.measify.kappbuilder.util.uiStateHolderScope
import dev.gitlive.firebase.auth.FirebaseAuthRecentLoginRequiredException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileUiStateHolder(private val userRepository: UserRepository) : UiStateHolder {


    private val currentUserFlow = userRepository.getUser().onEach { result ->
        result.onSuccess { user ->
            _uiState.update {
                it.copy(
                    isLoading = false,
                    user = user,
                    signInActionRequired = false
                )
            }
        }.onFailure { error ->
            if (error is UnAuthorizedException) {
                _uiState.update { it.copy(isLoading = false, signInActionRequired = true) }
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
                SharingStarted.WhileSubscribed(5000),
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
                _uiState.update { it.copy(signInActionRequired = true, isLoading = false) }
            }
            .onFailure { error ->
                AppLogger.d("Account deletion failed ${error.message}")
                if (error is FirebaseAuthRecentLoginRequiredException) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            signInActionRequired = true,
                        )
                    }
                } else
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
    }

    fun onUiEvent(event: ProfileScreenUiEvent) = uiStateHolderScope.launch {
        when (event) {
            ProfileScreenUiEvent.OnClickLogOut -> {
                _uiState.update { it.copy(isLoading = true) }
                userRepository.logOut()
                _uiState.update { it.copy(signInActionRequired = true, isLoading = false) }
            }

            ProfileScreenUiEvent.OnClickDeleteAccount -> {
                _uiState.update { it.copy(deleteUserDialogShown = true) }
            }
        }
    }

}