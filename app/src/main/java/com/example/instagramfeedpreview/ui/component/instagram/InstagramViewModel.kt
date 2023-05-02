package com.example.instagramfeedpreview.ui.component.instagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.network.model.request.LoginDTO
import com.example.network.model.response.BoardDTO
import com.example.network.model.response.TokenDTO
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

    private val _boardDTO = MutableSharedFlow<BoardDTO>()
    val boardDTO: SharedFlow<BoardDTO> = _boardDTO

    private val _tokenDTO = MutableSharedFlow<TokenDTO>()
    val tokenDTO: SharedFlow<TokenDTO> = _tokenDTO

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken

    fun requestAccessToken(
        loginDTO: LoginDTO
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        try {
            fetchInstagramTokenUseCase.invoke(loginDTO)?.let { tokenDTO ->
                _uiState.value = UiState.Success
                _tokenDTO.emit(tokenDTO)
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

    fun getUserAccessToken() = viewModelScope.launch {
        _uiState.value = UiState.Loading
        try {
            handleUserInformationUseCase.get()?.let { accessToken ->
                _uiState.value = UiState.Success
                _accessToken.emit(accessToken)
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e)
        }
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
