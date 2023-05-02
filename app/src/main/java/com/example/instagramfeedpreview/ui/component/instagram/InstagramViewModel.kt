package com.example.instagramfeedpreview.ui.component.instagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usecase.FetchInstagramBoardUseCase
import com.example.usecase.FetchInstagramTokenUseCase
import com.example.usecase.HandleUserInformationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstagramViewModel @Inject constructor(
    private val fetchInstagramBoardUseCase: com.example.usecase.FetchInstagramBoardUseCase,
    private val fetchInstagramTokenUseCase: com.example.usecase.FetchInstagramTokenUseCase,
    private val handleUserInformationUseCase: com.example.usecase.HandleUserInformationUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState

    private val _boardDTO = MutableSharedFlow<com.example.network.model.response.BoardDTO>()
    val boardDTO: SharedFlow<com.example.network.model.response.BoardDTO> = _boardDTO

    private val _tokenDTO = MutableSharedFlow<com.example.network.model.response.TokenDTO>()
    val tokenDTO: SharedFlow<com.example.network.model.response.TokenDTO> = _tokenDTO

    private val _accessToken = MutableStateFlow("")
    val accessToken: StateFlow<String> = _accessToken

    fun requestAccessToken(
        loginDTO: com.example.network.model.request.LoginDTO
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
