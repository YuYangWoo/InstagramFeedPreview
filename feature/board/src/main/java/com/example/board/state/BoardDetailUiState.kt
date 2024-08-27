package com.example.board.state

sealed interface BoardDetailUiState {
    data class Error(val errorState: ErrorState): BoardDetailUiState {
        sealed interface ErrorState {
            data class NetworkError(override val message: String, override val cause: Throwable) : ErrorState, Exception()

            data class DefaultError(override val message: String, override val cause: Throwable) : ErrorState, Exception()
        }
    }

    object Loading : BoardDetailUiState

    data class Success(val items: List<Item> = listOf()) : BoardDetailUiState

    data class Item(
        val id: String = "",
        val mediaUrl: String = ""
    )
}
