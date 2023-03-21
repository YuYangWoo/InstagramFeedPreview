package com.example.instagramfeedpreview.ui.component.instagram

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramfeedpreview.data.model.response.BoardDTO
import com.example.instagramfeedpreview.data.model.response.TokenDTO
import com.example.instagramfeedpreview.data.repository.instagramRepository.InstagramRepositoryImpl
import com.example.instagramfeedpreview.domain.usecase.InstagramBoardFetchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InstagramViewModel @Inject constructor(private val instagramBoardFetchUseCase: InstagramBoardFetchUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Empty)
    val uiState: StateFlow<UiState> = _uiState

    private val _boardDTO = MutableSharedFlow<BoardDTO>()
    val boardDTO: SharedFlow<BoardDTO> = _boardDTO

    fun requestInstagramFeedItem(
        clientId: String,
        clientSecret: String,
        grantType: String,
        redirectUri: String,
        code: String
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        try {
            instagramBoardFetchUseCase.invoke(clientId, clientSecret, grantType, redirectUri, code)?.let { boardDTO ->
                _boardDTO.emit(boardDTO)
                _uiState.value = UiState.Success
            }
        } catch (e: Exception) {
            _uiState.value = UiState.Error(e)
        }
    }
}

sealed class UiState {
    object Success: UiState()
    object Loading: UiState()
    data class Error(val e: Exception): UiState()
    object Empty: UiState()
}
