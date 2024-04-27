package com.example.main

data class MainUiState(
    val isLoading: Boolean = false,
    val isShowBoardFragment: Boolean = false,
    val accessToken: String = "",
)
