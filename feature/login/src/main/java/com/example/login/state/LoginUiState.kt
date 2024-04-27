package com.example.login.state

data class LoginUiState(
    val isLoading: Boolean = false,
    val isShowBoardFragment: Boolean = false,
    val accessToken: String = ""
)
