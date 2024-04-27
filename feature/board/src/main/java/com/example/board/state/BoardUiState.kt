package com.example.board.state

data class BoardUiState(
    val id: String = "",
    val mediaUrl: String = "",
    val onClick: () -> Unit = {}
)