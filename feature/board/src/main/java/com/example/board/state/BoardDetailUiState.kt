package com.example.board.state

data class BoardDetailUiState(
    val isLoading: Boolean = false,
    val items: List<Item> = listOf(),
    val errorMessage: String = ""
) {
    data class Item(
        val id: String = "",
        val mediaUrl: String = ""
    )
}
