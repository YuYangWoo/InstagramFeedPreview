package com.example.login.state

import com.example.login.UiLogin

sealed interface LoginUiState {
    object Idle : LoginUiState

    object Loading : LoginUiState

    data class Success(val loginState: LoginState): LoginUiState {
        data class LoginState(
            val isShowBoardFragment: Boolean = false,
            val accessToken: String = "",
        )
    }

    data class Error(val errorState: ErrorState): LoginUiState {
        sealed interface ErrorState {
            data class NetworkError(override val message: String, override val cause: Throwable) : ErrorState, Exception()
            data class DefaultError(override val message: String, override val cause: Throwable) : ErrorState, Exception()
        }
    }
}

sealed interface LoginUiEvent {
    data class OnUpdateLoginInfo(val login: UiLogin) : LoginUiEvent
}