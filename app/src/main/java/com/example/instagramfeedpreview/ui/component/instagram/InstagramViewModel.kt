package com.example.instagramfeedpreview.ui.component.instagram

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramfeedpreview.data.model.request.LoginDAO
import com.example.instagramfeedpreview.data.model.response.BoardDTO
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
        loginDAO: LoginDAO
    ) = viewModelScope.launch {
        _uiState.value = UiState.Loading

        try {
            instagramBoardFetchUseCase.invoke(loginDAO)?.let { boardDTO ->
                _boardDTO.emit(boardDTO)
                _uiState.value = UiState.Success
            } ?: Log.e(TAG, "문제 발생")
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
