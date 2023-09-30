package com.example.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Board
import com.example.model.Login
import com.example.model.Token
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.HandleUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstagramViewModel @Inject constructor(
    private val fetchInstagramBoardUseCase: FetchInstagramBoardUseCase,
    private val fetchInstagramTokenUseCase: FetchInstagramTokenUseCase,
    private val handleUserInformationUseCase: HandleUserInformationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState

    private val _boardDTO = MutableSharedFlow<Board>()
    val boardDTO: SharedFlow<Board> = _boardDTO

    private val _token = MutableSharedFlow<Token>()
    val token: SharedFlow<Token> = _token

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken

    fun requestAccessToken(
        login: Login
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        try {
            fetchInstagramTokenUseCase.invoke(login)?.let { token ->
                _uiState.value = UiState.Success
                _token.emit(token)
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e)
        }
    }

    fun requestBoardItem(
        accessToken: String
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        try {
            fetchInstagramBoardUseCase.invoke(accessToken)?.let { boardDTO ->
                _uiState.value = UiState.Success
                _boardDTO.emit(boardDTO)
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e)
        }
    }

    fun saveUserAccessToken(accessToken: String) = viewModelScope.launch {
        handleUserInformationUseCase.save(accessToken)
    }

    companion object {
        private const val TAG = "InstagramViewModel"
    }
}

sealed class UiState {
    object Success: UiState()
    object Loading: UiState()
    data class Error(val e: Exception): UiState()
    object Empty: UiState()
}
