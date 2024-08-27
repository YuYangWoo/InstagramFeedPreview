package com.example.main

sealed interface MainUiState {
    object Loading : MainUiState

    data class Success(val isShowBoardFragment: Boolean, val accessToken: String) : MainUiState

    data class Error(val errorState: ErrorState): MainUiState {
        sealed interface ErrorState {
            data class NetworkError(override val message: String, override val cause: Throwable) : ErrorState, Exception()
            data class DefaultError(override val message: String, override val cause: Throwable) : ErrorState, Exception()
        }
    }

}
