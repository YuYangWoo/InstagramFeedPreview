package com.example.board.state

data class BoardUiState(
    val id: String = "",
    val mediaUrl: String = "",
    val onClick: () -> Unit = {}
)

data class NavigateBoardDetailUiState(
    val shouldNavigateBoardDetail: Boolean = false,
    val id: String = "",
    val mediaUrl: String = "",
)

sealed interface BoardUiEvent {
    data class OnNavigateBoardDetail(val id: String, val mediaUrl: String) : BoardUiEvent

    object OnClearNavigateBoardDetail : BoardUiEvent
}