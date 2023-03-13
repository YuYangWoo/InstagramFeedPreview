package com.example.instagramfeedpreview.ui.component.instagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramfeedpreview.data.model.response.BoardDTO
import com.example.instagramfeedpreview.data.model.response.TokenDTO
import com.example.instagramfeedpreview.data.repository.instagramRepository.InstagramRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstagramViewModel @Inject constructor(private val instagramRepository: InstagramRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState

    private val _tokenDTO = MutableSharedFlow<TokenDTO>()
    val tokenDTO: SharedFlow<TokenDTO> = _tokenDTO
    private val _boardDTO = MutableSharedFlow<BoardDTO>()
    val boardDTO: SharedFlow<BoardDTO> = _boardDTO

    fun requestToken(
        clientId: String,
        clientSecret: String,
        grantType: String,
        redirectUri: String,
        code: String
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        instagramRepository.fetchToken(clientId, clientSecret, grantType, redirectUri, code)
            .catch { e ->
                _uiState.value = UiState.Error(e as Exception)
            }
            .collect { data ->
                _tokenDTO.emit(data)
                _uiState.value = UiState.Success
            }
    }

    fun requestBoardInformation(
        accessToken: String
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        instagramRepository.fetchBoardInformation(accessToken)
            .catch { e ->
                _uiState.value = UiState.Error(e as Exception)
            }
            .collect { data ->
                _boardDTO.emit(data)
                _uiState.value = UiState.Success
            }
    }
}

sealed class UiState {
    object Success: UiState()
    object Loading: UiState()
    data class Error(val e: Exception): UiState()
    object Empty: UiState()
}
